package com.example.androidengine;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

/**
 * Android implementation of {@link NotificationInterface} using WorkManager and NotificationManagerCompat.
 */
public class NotificationAndroid extends NotificationInterface {

    /**
     * Background Worker handling local notification creation and dispatch.
     */
    public static class NotificationWorker extends Worker {
        /** Channel ID string for Android NotificationChannel. */
        final String CHANNEL_ID = "Notifications";

        /**
         * Constructs NotificationWorker.
         *
         * @param context Application Context
         * @param workerParams Worker parameters
         */
        public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        /**
         * Performs background work: creates channel and notifies Android notification manager.
         *
         * @return WorkManager Result (success/failure)
         */
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        @NonNull
        @Override
        public Result doWork() {
            var app = getApplicationContext();
            int workId = getInputData().getInt("WorkId", 0);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NotificationChannel" + workId, importance);
            channel.setDescription("meow");
            var notificationManager = NotificationManagerCompat.from(app);
            notificationManager.createNotificationChannel(channel);

            Intent intent = app.getPackageManager().getLaunchIntentForPackage(getInputData().getString("Package"));
            intent.putExtra(getInputData().getString("Tag"), getInputData().getString("Value"));
            PendingIntent contentIntent = PendingIntent.getActivity(app, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(app, CHANNEL_ID)
                    .setContentTitle(getInputData().getString("Title"))
                    .setContentText(getInputData().getString("Body"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(IconCompat.createWithResource(app, getInputData().getInt("Icon", 0)))
                    .setAutoCancel(true);

            notificationManager.notify(workId, builder.build());

            return Result.success();
        }
    }

    /** Hosting AppCompatActivity instance. */
    AppCompatActivity app;

    /** Work ID counter for unique work names. */
    int workId = 0;

    /**
     * Constructs NotificationAndroid with AppCompatActivity context.
     *
     * @param _app hosting AppCompatActivity
     */
    public NotificationAndroid(AppCompatActivity _app) {
        app = _app;
        WorkManager.getInstance(app).cancelAllWork();
    }

    /**
     * Sets the small icon resource ID for notifications.
     *
     * @param si small icon resource ID
     */
    @Override
    public void setSmallIcon(int si) {
        smallIcon = si;
    }

    /**
     * Inserts notification definition at specified index.
     *
     * @param data NotificationData definition
     * @param idx target insertion index
     */
    public void createNotification(NotificationData data, int idx) {
        notifs.add(idx, data);
    }

    /**
     * Schedules a notification to trigger after a delay in seconds using WorkManager.
     *
     * @param idx notification index in list
     * @param sec delay in seconds
     */
    @Override
    public void scheduleNotification(int idx, int sec) {
        NotificationData data = notifs.get(idx);
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
        requestData.putString("Package", app.getPackageName());

        request.setInputData(requestData.build());
        WorkManager.getInstance(app).enqueueUniqueWork(
                "NotifWork_" + workId,
                ExistingWorkPolicy.REPLACE,
                request.build()
        );
        workId++;
    }
}
