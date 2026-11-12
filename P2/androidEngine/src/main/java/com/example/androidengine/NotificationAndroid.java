package com.example.androidengine;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.example.engine.NotificationData;
import com.example.engine.NotificationInterface;

public class NotificationAndroid extends NotificationInterface {
    final String CHANNEL_ID = "Notifications";
    AppCompatActivity app;
    NotificationManagerCompat notificationManager;
    int nextNotifIdx = 0;
    int smallIcon;
    public void setSmallIcon(int si){
        smallIcon = si;
    }
    public NotificationAndroid(AppCompatActivity _app){
        app = _app;

        // Create channel
        int importance = NotificationManager. IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID , "NotificationChannel", importance);
        channel.setDescription("meow");
        notificationManager = NotificationManagerCompat.from(app);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void createNotification(String _title, String _body) {
        notifs.add(new NotificationData(_title,_body));
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void scheduleNotification(int i, int sec) {
        Intent intent = new Intent(app.getBaseContext(), app.getClass());
        PendingIntent contentIntent = PendingIntent. getActivity(app, 0, intent,
                PendingIntent. FLAG_IMMUTABLE | PendingIntent. FLAG_UPDATE_CURRENT);

        NotificationData data = notifs.get(i);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(app, CHANNEL_ID)
                .setContentTitle(data.title)
                .setContentText(data.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setSmallIcon(IconCompat.createWithResource(app,smallIcon));
        notificationManager.notify(nextNotifIdx,builder.build());
        ++nextNotifIdx;
    }
}
