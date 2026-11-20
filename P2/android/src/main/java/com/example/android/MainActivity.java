package com.example.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidengine.AndroidEngine;
import com.example.androidengine.NotificationAndroid;
import com.example.engine.NotificationData;
import com.example.game.testing.Scenes.MainMenuScene;
import com.example.game.testing.Scenes.RubyRewardScene;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.constants.NotificationId;

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

        Intent intent = getIntent();
        eng.setScene(getIntent().hasExtra(Constants.NOTIF_EXTRA) ?
                new RubyRewardScene(eng,Integer.parseInt(getIntent().getStringExtra(Constants.NOTIF_EXTRA))) :
                new MainMenuScene(eng));

        eng.getNotifs().setSmallIcon(R.drawable.homunculus);
        eng.getNotifs().createNotification(
                new NotificationData("MCD", "Vuelve a mochilacohetonadisfrutar AHORA!!!",
                        Constants.NOTIF_EXTRA,Constants.RUBY_REWARD),
                NotificationId.NOTIFICATION_DEFAULT.ordinal());
    }

    @Override
    protected void onResume() {
        eng.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        eng.getNotifs().scheduleNotification(NotificationId.NOTIFICATION_DEFAULT.ordinal(),5);
        eng.pause();
        super.onPause();
    }
}
