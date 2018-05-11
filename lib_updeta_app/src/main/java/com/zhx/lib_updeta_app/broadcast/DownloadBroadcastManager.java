package com.zhx.lib_updeta_app.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.zhx.lib_updeta_app.R;
import com.zhx.lib_updeta_app.config.UpdateApp;
import com.zhx.lib_updeta_app.utils.NotificationUtils;

import java.io.File;

/**
 * 广播接收监听
 * Created by zhx on 2018/5/9.
 */

public class DownloadBroadcastManager extends BroadcastReceiver {
    private Context mContext;
    private File saveFile;

    public DownloadBroadcastManager() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        if ("rate".equals(action)) {//下载进度
            int reat = intent.getIntExtra("rate", 0);
            setNotification(reat);
        } else if ("status".equals(action)) { //下载状态
            saveFile = new File(intent.getStringExtra("saveFile") == null ? "" : intent.getStringExtra("saveFile"));
            int status = intent.getIntExtra("status", UpdateApp.DOWNLOADSERVICE_STATUS_NO);
            checkoutStatus(status);
        }
    }

    private void checkoutStatus(int status) {
        if (status == UpdateApp.DOWNLOADSERVICE_STATUS_OK) {
            Intent install = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                Uri apkUri = FileProvider.getUriForFile(mContext, "com.zhx.lib_updeta_app.fileprovider", saveFile);//在AndroidManifest中的android:authorities值
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                mContext.startActivity(install);
            } else {
                install.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(install);
            }

            // 当下载完毕，更新通知栏，且当点击通知栏时，安装APK
            PendingIntent updatePendingIntent = PendingIntent.getActivity(mContext, 0, install, 0);

            NotificationUtils.getNotificationBuilder(mContext)
                    .setContentTitle(mContext.getResources().getString(R.string.app_name))
                    .setContentText("下载完成,点击安装")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(updatePendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                NotificationUtils.getNotificationManager(mContext).notify(0, NotificationUtils.getNotificationBuilder(mContext).build());// 把通知发布出去
            }

        } else {
            // 下载失败
            NotificationUtils.getNotificationBuilder(mContext)
                    .setContentTitle(mContext.getResources().getString(R.string.app_name))
                    .setContentText("下载失败,网络连接超时")
                    .setContentIntent(NotificationUtils.getPendingIntent(mContext));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                NotificationUtils.getNotificationManager(mContext).notify(0, NotificationUtils.getNotificationBuilder(mContext).build());// 把通知发布出去
            }
        }
        //关闭回收广播，静态类变量
        UpdateApp.clear();
    }


    private void setNotification(int rate) {
        NotificationUtils.getNotificationBuilder(mContext)
                .setContentTitle(mContext.getResources().getString(R.string.app_name) + " 正在下载更新")
                .setContentText(rate + "%")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentIntent(NotificationUtils.getPendingIntent(mContext))
                .setProgress(100, rate, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationUtils.getNotificationManager(mContext).notify(0, NotificationUtils.getNotificationBuilder(mContext).build());// 把通知发布出去
        }

    }
}