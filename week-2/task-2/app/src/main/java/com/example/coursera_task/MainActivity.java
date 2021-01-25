package com.example.coursera_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String HTTP_RESOURCE = "https://i1.wp.com/www.gadgetsaint.com/wp-content/uploads/2016/11/cropped-web_hi_res_512.png";
    public static final int REQUEST_PERMISSIONS = 11;
    private Button button_setImage;
    private ImageView imageView;
    private Button button_download;
    private EditText editText;
    public DownloadManager downloadManager;
    public ArrayList<Long> list = new ArrayList<>();
    public long refId;
    public DownloadImage downloading;
    private AlertDialog.Builder alertDialog;
    private boolean permissionResponse = false;
    private MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText_Url);
        button_download = findViewById(R.id.button_download);
        imageView = findViewById(R.id.image_view);
        button_setImage = findViewById(R.id.button_setImage);

        button_download.setOnClickListener(downloadClick);
        button_setImage.setOnClickListener(setImageClick);

        // Показ диалогового окна и запрос на разрешения доступа к медиа файлам
        alertDialog = new AlertDialog.Builder(this);
        requestPermission();

        // Запуск фоновой загрузки
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloading  = new DownloadImage(this, refId, downloadManager);

        // Регистрация приемника
        myBroadCastReceiver = new MyBroadCastReceiver(this, list);
        registerReceiver(myBroadCastReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private View.OnClickListener downloadClick = v -> {
       if (permissionResponse) {
           writeToStoragePermissionRequest();
       }
       list.add(refId);
       editText.setText(HTTP_RESOURCE);
       button_setImage.setEnabled(true);
    };

    private View.OnClickListener setImageClick = v-> {
       String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Google_Icon.png";
       File imageFile = new File(path);
       if(imageFile.exists()) {
           Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
           imageView.setImageBitmap(myBitmap);
       }
    };

    private void writeToStoragePermissionRequest() {
        if (isWritePermissionGranted()) {
            refId = downloading.downloadImage();
        }
        else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            alertDialog.setMessage("Без разрешения для доступа к хранилищу невозможно произвести загрузку")
                    .setPositiveButton("Ок", (dialog, which) -> {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }).show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        }
    }

    private boolean isWritePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionResponse = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReceiver);
    }


}