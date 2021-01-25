package com.example.coursera_task;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private Context context;
    private final String CHANNEL_ID = "channel1";
    private final int NOTIFY_ID = 45;
    private ArrayList<Long> list;

    public MyBroadCastReceiver(Context context, ArrayList<Long> list) {
        this.context = context;
        this.list = list;
    }

    public void onReceive(Context context, Intent intent) {
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        list.remove(referenceId);

        if (list.isEmpty()) {
            createNotificationChannel();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Google_Icon")
                            .setContentText("Download completed");


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, mBuilder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
