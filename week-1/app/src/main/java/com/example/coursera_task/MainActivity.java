package com.example.coursera_task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    BoundService mService;
    public static int progress_changing = 0;
    private ProgressBroadCastReceiver receiver;
    public static String tag_broadcast_intent = "com.example.coursera_task.UPDATE_PROGRESS";
    public static final String progress_bar_tag = "progress_bar_tag";
    private Intent intent;
    private boolean isBound;
    private Button button_drop_progress;

    // Здесь необходимо создать соединение с сервисом
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // используем mService экземпляр класса для доступа к публичному LocalService
            BoundService.LocalService localService = (BoundService.LocalService) service;
            mService = localService.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        button_drop_progress = findViewById(R.id.button_drop_progress);

        // Регистрируем приемник и передаем ему фильтр для получения сообщений от BoundService
        IntentFilter filter = new IntentFilter(tag_broadcast_intent);
        receiver = new ProgressBroadCastReceiver();
        registerReceiver( receiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Привязываем MainActivity к BoundService  и запускаем сервис
        intent = new Intent(this, BoundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Отвязываем сервис
        unbindService(serviceConnection);
        isBound = false;
    }

    @Override
    protected void onDestroy() {
        // завершаем работу приемника когда заканчивает работу MainActivity
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    class ProgressBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Обрабатываем полученные данные от BoundService
            intent.getIntExtra("progress", progress_changing);
            if(progress_changing == 100) {
                Toast.makeText(MainActivity.this,"Загрузка завершена", Toast.LENGTH_SHORT).show();
            }
            progressBar.setProgress(progress_changing);

            button_drop_progress.setOnClickListener(v -> {
                mService.dropProgress();
                progressBar.setProgress(progress_changing);
            });
        }
    }
}