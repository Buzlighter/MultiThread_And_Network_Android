package com.example.coursera_task;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadImage extends MainActivity {
    private Uri Download_Uri;
    private long refId;
    private DownloadManager downloadManager;

    public Context context;
    public DownloadImage(Context context, long refId, DownloadManager downloadManager) {
        this.context = context;
        this.refId = refId;
        this.downloadManager = downloadManager;
    }

    public long downloadImage() {
        Download_Uri = Uri.parse(HTTP_RESOURCE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Google_Icon.png");
        request.setDescription("Google_Icon.png");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,  "/Google_Icon.png");
        refId = downloadManager.enqueue(request);
        return refId;
    }
}
