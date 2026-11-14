package com.example.androidengine;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.engine.ISocial;
import com.example.engine.ImageEng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SocialAndroid implements ISocial {
    private final Activity act;

    public SocialAndroid(Activity act) {
        this.act = act;
    }

    @Override
    public void sendMessage(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        act.startActivity(Intent.createChooser(intent, "Share text"));
    }

    @Override
    public void sendMessage(String text, ImageEng img, String titleImg, String imageDesc) {
        ImageAndroid imgA = (ImageAndroid) img;
        Uri imageUri = saveBitmapToCache(imgA.getBitmap(), titleImg);
        if (imageUri == null) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        // Enables image thumbnail preview in the Android 10+ Sharesheet
        intent.setClipData(ClipData.newRawUri(imageDesc, imageUri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        act.startActivity(Intent.createChooser(intent, "Share image"));
    }

    @Override
    public void createPost(String text, ImageEng img) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (img != null) {
            ImageAndroid imgA = (ImageAndroid) img;
            Uri imageUri = saveBitmapToCache(imgA.getBitmap(), "post_image");
            if (imageUri == null) return;

            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.setClipData(ClipData.newRawUri(text, imageUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setType("text/plain");
        }

        if (text != null && !text.isEmpty()) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }

        act.startActivity(Intent.createChooser(intent, "Create post"));
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
}