package com.example.coursera_task;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button btn_download;
    public static String text_load = "Loading..";
    public static String text_ready = "Ready";
    private ProgressBar progressBar;
    private TextView tv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_download = findViewById(R.id.download_button);
        progressBar = findViewById(R.id.progressBar);
        tv_loading = findViewById(R.id.tv_loading);

        btn_download.setOnClickListener(v -> {
            btn_download.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            tv_loading.setText(text_load);
            launchTask();
        });
    }
    private void launchTask() {
        Runnable runnable = () -> {
            Log.d("TAG", "background thread is working");
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
                btn_download.post(() -> {
                    btn_download.setEnabled(true);
                });
                progressBar.post(()-> {
                    progressBar.setVisibility(View.INVISIBLE);
                });
                tv_loading.post(()->{
                    tv_loading.setText(text_ready);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("TAG", "background thread had completed work");
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tag", "Activity is destroyed");
    }
}