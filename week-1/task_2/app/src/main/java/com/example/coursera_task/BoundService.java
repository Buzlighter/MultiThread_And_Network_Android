package com.example.coursera_task;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BoundService extends Service {
    private final IBinder binder = new LocalService();
    private ScheduledExecutorService scheduledExecutorService;
    private Intent broadcastIntent;

    public class LocalService extends Binder {
        BoundService getService(){
            return BoundService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        // Создаем поток и заносим его в пул
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        updateProgress();
    }

    public void updateProgress() {
        // Увеличиваем данные в потоке с определенным интервалом и отправляем сообщение в MainActivity
        broadcastIntent = new Intent();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if(MainActivity.progress_changing < 100) {
                MainActivity.progress_changing += 5;
                broadcastIntent.putExtra("progress", MainActivity.progress_changing);
                broadcastIntent.setAction(MainActivity.tag_broadcast_intent);
                sendBroadcast(broadcastIntent);
            }
        }, 1000, 200, TimeUnit.MILLISECONDS);
    }


    public void dropProgress() {
        if (MainActivity.progress_changing >= 50)
            MainActivity.progress_changing -= 50;
        else {
            MainActivity.progress_changing = 0;
        }
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}