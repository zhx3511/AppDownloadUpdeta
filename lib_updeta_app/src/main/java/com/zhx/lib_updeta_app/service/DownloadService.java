package com.zhx.lib_updeta_app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.zhx.lib_updeta_app.config.UpdateApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 开启子线程的服务下载APK，下载完毕后通过广播通知安装替换
 * Created by zhx on 2018/5/8.
 */
public class DownloadService extends IntentService {

    private UpdateApp.VersionInfo mVersionInfo;
    private LocalBroadcastManager localBroadcastManager;
    private File saveFile;
    private File saveDir;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mVersionInfo = (UpdateApp.VersionInfo) intent.getSerializableExtra("mVersionInfo");

        try {
            download();
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent1 = new Intent("status");
            intent1.putExtra("status", UpdateApp.DOWNLOADSERVICE_STATUS_NO);
            intent.putExtra("saveFile", saveFile.getPath());
            localBroadcastManager.sendBroadcast(intent1);
        }
    }

    /**
     * 开始下载
     */
    private void download() throws Exception {

        saveDir = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString() + "/apk/");
        saveFile = new File(saveDir, "shangjiayihao.apk");

        if (saveDir != null && !saveDir.exists()) {
            saveDir.mkdirs();
        }
        if (saveFile != null && !saveFile.exists()) {
            saveFile.createNewFile();
        }
        int currentSize = 0;
        int downloadCount = 0;
        int updateTotalSize = 0;
        long totalSize = 0;
        int rate = 0;// 下载完成比例
        HttpURLConnection httpConnection = null;
        InputStream mInputStream = null;
        FileOutputStream mFileOutputStream = null;

        try {
            URL url = new URL(mVersionInfo.url);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            httpConnection.setConnectTimeout(200000);
            httpConnection.setReadTimeout(200000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) { //下载失败
                throw new Exception("404");
            }
            mInputStream = httpConnection.getInputStream();
            mFileOutputStream = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[1024 * 1024 * 3];
            int readsize = 0;
            while ((readsize = mInputStream.read(buffer)) != -1) {
                mFileOutputStream.write(buffer, 0, readsize);
                totalSize += readsize;// 已经下载的字节数
                rate = (int) (totalSize * 100 / updateTotalSize);// 当前下载进度
                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ((downloadCount == 0) || rate - 0 > downloadCount) {
                    downloadCount += 1;
                    Intent intent = new Intent("rate");
                    intent.putExtra("rate", rate);
                    localBroadcastManager.sendBroadcast(intent);
                }
            }
            Intent intent = new Intent("status");
            intent.putExtra("status", UpdateApp.DOWNLOADSERVICE_STATUS_OK);
            intent.putExtra("saveFile", saveFile.getPath());
            localBroadcastManager.sendBroadcast(intent);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mFileOutputStream != null) {
                mFileOutputStream.close();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
