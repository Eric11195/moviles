package com.example.androidengine;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.engine.NotificationData;
import com.example.engine.NotificationInterface;

import java.util.concurrent.TimeUnit;

public class NotificationAndroid extends NotificationInterface {
    public static class NotificationWorker extends Worker {
        final String CHANNEL_ID = "Notifications";

        public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        @NonNull
        @Override
        public Result doWork() {
            var app = getApplicationContext();
            int workId = getInputData().getInt("WorkId", 0);

            // Create channel
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NotificationChannel"+workId, importance);
            channel.setDescription("meow");
            var notificationManager = NotificationManagerCompat.from(app);
            notificationManager.createNotificationChannel(channel);

            Intent intent = new Intent(app, app.getClass());
            intent.putExtra(getInputData().getString("Message"), getInputData().getString("Value"));
            PendingIntent contentIntent = PendingIntent.getActivity(app, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(app, CHANNEL_ID)
                    .setContentTitle(getInputData().getString("Title"))
                    .setContentText(getInputData().getString("Body"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(IconCompat.createWithResource(app, getInputData().getInt("Icon", 0)));

            notificationManager.notify(workId, builder.build());

            return Result.success();
        }
    }

    AppCompatActivity app;
    int workId = 0;

    public NotificationAndroid(AppCompatActivity _app) {
        app = _app;
        WorkManager.getInstance(app).cancelAllWork();
    }

    @Override
    public void setSmallIcon(int si) {
        smallIcon = si;
    }

    @Override
    public void createNotification(NotificationData data) {
        notifs.add(data);
    }

    @Override
    public void scheduleNotification(int i, int sec) {
        NotificationData data = notifs.get(i);
        OneTimeWorkRequest.Builder request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(sec, TimeUnit.SECONDS);

        var requestData = new Data.Builder();
        requestData.putString("Title", data.title);
        requestData.putString("Body", data.body);
        requestData.putString("Tag", data.tag);
        requestData.putString("Value", data.value);
        requestData.putInt("Icon", smallIcon);
        requestData.putInt("Interval", sec);
        requestData.putInt("WorkId", workId);

        request.setInputData(requestData.build());
        WorkManager.getInstance(app).enqueueUniqueWork(
                "NotifWork_" + workId,
                ExistingWorkPolicy.REPLACE,
                request.build()
        );
        workId++;
    }
}
