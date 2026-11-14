package com.example.androidengine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.engine.ISocial;
import com.example.engine.ImageEng;

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
        ImageAndroid imgA = (ImageAndroid)img;
        String imageUri = MediaStore.Images.Media.insertImage(
                act.getContentResolver(), imgA.getBitmap(), titleImg, imageDesc
        );
        if (imageUri == null) return; // Prevents NPE if insertion fails

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(intent, "Share image"));
    }
}