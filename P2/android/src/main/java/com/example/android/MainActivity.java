package com.example.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.NotificationAndroid;
import com.example.game.testing.Scenes.MainMenuScene;

public class MainActivity extends AppCompatActivity {
    AndroidEngine eng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},0);

        eng = new AndroidEngine(this, R.id.mainSurfaceView,R.id.adView, 600,400);
        eng.setScene(new MainMenuScene(eng));
        eng.resume();
        eng.getNotifs().setSmallIcon(R.drawable.homunculus);
        eng.getNotifs().createNotification("buh","guh");
        eng.getNotifs().scheduleNotification(0,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
