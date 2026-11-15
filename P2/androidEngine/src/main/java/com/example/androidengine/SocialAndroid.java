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

public class SocialAndroid implements ISocial {
    private final Activity act;

    public SocialAndroid(Activity act) {
        this.act = act;
    }
    @Override
    public void sendMessage(String text) {
        shareContent(text, null, null, null, "Share text");
    }

    @Override
    public void sendMessage(String text, ImageEng img, String titleImg, String imageDesc) {
        shareContent(text, img, titleImg, imageDesc, "Share image");
    }

    @Override
    public void createPost(String text, ImageEng img) {
        if (img == null) {
            sendMessage(text);
            return;
        }

        ImageAndroid imgA = (ImageAndroid) img;
        Bitmap bitmap = imgA.getBitmap();

        // Build options dynamically based on which apps are installed
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
                shareToPackage(bitmap, "com.facebook.katana", "fb_post",text);
            });
        }

        labels.add("More apps...");
        actions.add(() -> sendMessage(text, img, "post_image", text));

        // Must run on UI thread because ButtonComponent.update() runs on GameLoopThread
        act.runOnUiThread(() -> {
            new android.app.AlertDialog.Builder(act)
                    .setTitle("Share your result")
                    .setItems(labels.toArray(new String[0]), (dialog, which) -> actions.get(which).run())
                    .show();
        });
    }

    private boolean isAppInstalled(String packageName) {
        try {
            act.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void copyCaptionToClipboard(String text) {
        if (text == null || text.trim().isEmpty()) return;
        act.runOnUiThread(() -> {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) act.getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("Game Result", text));
            android.widget.Toast.makeText(act, "Caption copied! Paste it into your post.", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

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

    private Uri saveBitmapToCache(Bitmap bitmap, String titleImg) {
        if (bitmap == null) return null;

        File imagesFolder = new File(act.getCacheDir(), "shared_images");
        if (!imagesFolder.exists() && !imagesFolder.mkdirs()) {
            return null;
        }

        // Sanitize titleImg for a safe filename, or fallback to a default name
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

    private void launchOrFallback(Intent targetedIntent, Uri imageUri, String fallbackTitle) {
        if (targetedIntent.resolveActivity(act.getPackageManager()) != null) {
            act.startActivity(targetedIntent);
        } else {
            // Fallback if Instagram/Facebook is not installed
            Intent fallback = new Intent(Intent.ACTION_SEND);
            fallback.setType("image/png");
            fallback.putExtra(Intent.EXTRA_STREAM, imageUri);
            fallback.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            act.startActivity(Intent.createChooser(fallback, fallbackTitle));
        }
    }
    private Bitmap createShareCard(Bitmap icon, String text) {
        int cardSize = 1080;
        int padding = 80;

        Bitmap card = Bitmap.createBitmap(cardSize, cardSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(card);

        // 1. Background color (dark slate makes the white circular icon stand out)
        canvas.drawColor(Color.parseColor("#24293E"));

        // 2. Scale and center the icon in the upper half
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

        // 3. Render wrapped, centered text below the icon
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