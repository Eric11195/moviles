package com.example.androidengine;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.core.content.FileProvider;

import com.example.engine.ISocial;
import com.example.engine.ImageEng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Android implementation of {@link ISocial} using Android Intents, FileProvider, and system shares.
 */
public class SocialAndroid implements ISocial {

    /** Hosting Activity context. */
    private final Activity act;

    /**
     * Constructs SocialAndroid with hosting Activity.
     *
     * @param act hosting Activity instance
     */
    public SocialAndroid(Activity act) {
        this.act = act;
    }

    /**
     * Shares plain text message via system share chooser.
     *
     * @param text message text content
     */
    @Override
    public void sendMessage(String text) {
        shareContent(text, null, null, null, "Share text");
    }

    /**
     * Shares image and text content via system share chooser.
     *
     * @param text message text content
     * @param img attached image asset
     * @param titleImg title for cached image
     * @param imageDesc description for image clip
     */
    @Override
    public void sendMessage(String text, ImageEng img, String titleImg, String imageDesc) {
        shareContent(text, img, titleImg, imageDesc, "Share image");
    }

    /**
     * Creates a social post dialog providing options for Instagram Story, Instagram Post, Facebook, or system chooser.
     *
     * @param text post text content
     * @param img image asset
     */
    @Override
    public void createPost(String text, ImageEng img) {
        if (img == null) {
            sendMessage(text);
            return;
        }

        ImageAndroid imgA = (ImageAndroid) img;
        Bitmap bitmap = imgA.getBitmap();

        List<String> labels = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();

        if (isAppInstalled("com.instagram.android")) {
            labels.add("Instagram Story");
            actions.add(() -> {
                copyCaptionToClipboard(text);
                shareToInstagramStory(bitmap, text);
            });

            labels.add("Instagram Post");
            actions.add(() -> {
                copyCaptionToClipboard(text);
                shareToPackage(bitmap, "com.instagram.android", "ig_post", text);
            });
        }

        if (isAppInstalled("com.facebook.katana")) {
            labels.add("Facebook");
            actions.add(() -> {
                copyCaptionToClipboard(text);
                shareToPackage(bitmap, "com.facebook.katana", "fb_post", text);
            });
        }

        labels.add("More apps...");
        actions.add(() -> sendMessage(text, img, "post_image", text));

        act.runOnUiThread(() -> {
            new android.app.AlertDialog.Builder(act)
                    .setTitle("Share your result")
                    .setItems(labels.toArray(new String[0]), (dialog, which) -> actions.get(which).run())
                    .show();
        });
    }

    /**
     * Checks whether a package is installed on the device.
     *
     * @param packageName package name string
     * @return true if installed, false otherwise
     */
    private boolean isAppInstalled(String packageName) {
        try {
            act.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Copies caption text to system clipboard.
     *
     * @param text text to copy
     */
    private void copyCaptionToClipboard(String text) {
        if (text == null || text.trim().isEmpty()) return;
        act.runOnUiThread(() -> {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) act.getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("Game Result", text));
            android.widget.Toast.makeText(act, "Caption copied! Paste it into your post.", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Shares image card to a specific package name.
     *
     * @param bitmap source icon bitmap
     * @param packageName target app package name
     * @param fileName file name prefix for cache
     * @param extraText caption text
     */
    private void shareToPackage(Bitmap bitmap, String packageName, String fileName, String extraText) {
        Bitmap extra = createShareCard(bitmap, extraText);
        Uri imageUri = saveBitmapToCache(extra, fileName);
        if (imageUri == null) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setPackage(packageName);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(intent);
    }

    /**
     * Shares image card directly to Instagram Story.
     *
     * @param bitmap source icon bitmap
     * @param text caption text
     */
    private void shareToInstagramStory(Bitmap bitmap, String text) {
        Bitmap extra = createShareCard(bitmap, text);
        Uri imageUri = saveBitmapToCache(extra, "ig_story");
        if (imageUri == null) return;

        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
        intent.setDataAndType(imageUri, "image/png");
        intent.setPackage("com.instagram.android");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.grantUriPermission("com.instagram.android", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(intent);
    }

    /**
     * Shares content using Android Intent chooser.
     *
     * @param text message text
     * @param img image asset
     * @param titleImg title for cached file
     * @param imageDesc description for image clip
     * @param chooserTitle dialog title
     */
    private void shareContent(String text, ImageEng img, String titleImg, String imageDesc, String chooserTitle) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        boolean hasText = text != null && !text.trim().isEmpty();

        if (hasText) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.putExtra(Intent.EXTRA_SUBJECT, text);
        }

        if (img != null) {
            ImageAndroid imgA = (ImageAndroid) img;
            Bitmap extra = createShareCard(imgA.getBitmap(), text);
            Uri imageUri = saveBitmapToCache(extra, titleImg);
            if (imageUri == null) return;

            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.setClipData(ClipData.newRawUri(
                    imageDesc != null ? imageDesc : "Shared image",
                    imageUri
            ));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setType("text/plain");
        }

        Intent chooser = Intent.createChooser(intent, chooserTitle);
        if (img != null) {
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        act.startActivity(chooser);
    }

    /**
     * Saves bitmap to internal cache directory and returns a content Uri via FileProvider.
     *
     * @param bitmap Bitmap instance to save
     * @param titleImg file name prefix
     * @return FileProvider Uri or null on failure
     */
    private Uri saveBitmapToCache(Bitmap bitmap, String titleImg) {
        if (bitmap == null) return null;

        File imagesFolder = new File(act.getCacheDir(), "shared_images");
        if (!imagesFolder.exists() && !imagesFolder.mkdirs()) {
            return null;
        }

        String safeName = (titleImg != null && !titleImg.trim().isEmpty())
                ? titleImg.replaceAll("[^a-zA-Z0-9._-]", "_")
                : "shared_image";
        File file = new File(imagesFolder, safeName + ".png");

        try (FileOutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return FileProvider.getUriForFile(
                act,
                act.getPackageName() + ".fileprovider",
                file
        );
    }

    /**
     * Helper launching targeted Intent or falling back to chooser.
     *
     * @param targetedIntent targeted package Intent
     * @param imageUri Uri of image
     * @param fallbackTitle title for fallback chooser
     */
    private void launchOrFallback(Intent targetedIntent, Uri imageUri, String fallbackTitle) {
        if (targetedIntent.resolveActivity(act.getPackageManager()) != null) {
            act.startActivity(targetedIntent);
        } else {
            Intent fallback = new Intent(Intent.ACTION_SEND);
            fallback.setType("image/png");
            fallback.putExtra(Intent.EXTRA_STREAM, imageUri);
            fallback.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            act.startActivity(Intent.createChooser(fallback, fallbackTitle));
        }
    }

    /**
     * Generates a composite share card bitmap (1080x1080) with centered icon and caption text.
     *
     * @param icon icon bitmap
     * @param text caption text
     * @return 1080x1080 share card Bitmap
     */
    private Bitmap createShareCard(Bitmap icon, String text) {
        int cardSize = 1080;
        int padding = 80;

        Bitmap card = Bitmap.createBitmap(cardSize, cardSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(card);

        canvas.drawColor(Color.parseColor("#24293E"));

        if (icon != null) {
            int targetIconSize = 380;
            float scale = Math.min(
                    (float) targetIconSize / icon.getWidth(),
                    (float) targetIconSize / icon.getHeight()
            );
            int scaledW = Math.round(icon.getWidth() * scale);
            int scaledH = Math.round(icon.getHeight() * scale);

            Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, scaledW, scaledH, true);
            float iconLeft = (cardSize - scaledW) / 2f;
            float iconTop = 140f;
            canvas.drawBitmap(scaledIcon, iconLeft, iconTop, null);

            if (scaledIcon != icon) {
                scaledIcon.recycle();
            }
        }

        if (text != null && !text.trim().isEmpty()) {
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(64f);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            int textWidth = cardSize - (padding * 2);
            StaticLayout layout = StaticLayout.Builder
                    .obtain(text, 0, text.length(), textPaint, textWidth)
                    .setAlignment(Layout.Alignment.ALIGN_CENTER)
                    .setLineSpacing(12f, 1.0f)
                    .build();

            canvas.save();
            float textTop = (icon != null)
                    ? 580f
                    : (cardSize - layout.getHeight()) / 2f;
            canvas.translate(padding, textTop);
            layout.draw(canvas);
            canvas.restore();
        }

        return card;
    }
}
