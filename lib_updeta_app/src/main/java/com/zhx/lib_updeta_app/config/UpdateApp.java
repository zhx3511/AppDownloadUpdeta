package com.zhx.lib_updeta_app.config;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.zhx.lib_updeta_app.DialogUpdetaFragment;
import com.zhx.lib_updeta_app.broadcast.DownloadBroadcastManager;
import com.zhx.lib_updeta_app.service.DownloadService;
import com.zhx.lib_updeta_app.utils.CommitUtils;

import java.io.Serializable;

/**
 * 配置更新下载
 * 作者：张旭
 * 邮箱：804554496@qq.com
 * Created by zhx on 2018/5/8.
 * <p>
 * 适配Android8.0 需要用户授权android.permission.REQUEST_INSTALL_PACKAGES
 * 根据自己定义的授权框架
 */
public class UpdateApp {
    private static Activity activity;

    /**
     * 下载成功
     */
    public static final int DOWNLOADSERVICE_STATUS_OK = 1;

    /**
     * 下载失败
     */
    public static final int DOWNLOADSERVICE_STATUS_NO = 2;

    private LocalBroadcastManager localBroadcastManager;
    private DownloadBroadcastManager mDownloadBroadcastManager;


    public UpdateApp(VersionInfo versionInfo) {
        int status =
                CommitUtils.VersionComparison(versionInfo.newVersion, versionInfo.newVersion, CommitUtils.getVersionName(activity));
        if (status > 0) { //需要更新
            //注册广播接收器
            localBroadcastManager = LocalBroadcastManager.getInstance(activity);
            mDownloadBroadcastManager = new DownloadBroadcastManager();
            localBroadcastManager.registerReceiver(mDownloadBroadcastManager, new IntentFilter("status"));
            localBroadcastManager.registerReceiver(mDownloadBroadcastManager, new IntentFilter("rate"));
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            ft.add(DialogUpdetaFragment.newInstance(versionInfo), "DialogUpdetaFragment");
            ft.commit();
        }
    }


    public static class VersionInfo implements Serializable {
        public String newVersion; // 最新版本
        public String url; // 下载地址
        public String remark;//更新内容
        public boolean isForceUpdeta = false;
        public String download_dialog_content;
        public String download_dialog_titile;
        public String download_dialog_but_no;
        public String download_dialog_but_ok;

        public VersionInfo(Activity activitys) {
            activity = activitys;
        }

        /**
         * 设置最新版本
         *
         * @param newVersion 例如 1.0.0
         * @return
         */
        public VersionInfo setNewVersion(String newVersion) {
            this.newVersion = newVersion;
            return this;
        }

        /**
         * 设置下载链接
         *
         * @param url
         * @return
         */
        public VersionInfo setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置备注
         *
         * @param remark
         * @return
         */
        public VersionInfo setRemark(String remark) {
            this.remark = remark;
            return this;
        }


        /**
         * 是否强制更新
         *
         * @param isForceUpdeta
         * @return
         */
        public VersionInfo setForceUpdeta(boolean isForceUpdeta) {
            this.isForceUpdeta = isForceUpdeta;
            return this;
        }

        /**
         * 设置更新对话框标题
         *
         * @param download_dialog_titile
         * @return
         */
        public VersionInfo setDownload_dialog_titile(String download_dialog_titile) {
            this.download_dialog_titile = download_dialog_titile;
            return this;
        }

        /**
         * 设置更新对话框内容
         *
         * @param download_dialog_content
         * @return
         */
        public VersionInfo setDownload_dialog_content(String download_dialog_content) {
            this.download_dialog_content = download_dialog_content;
            return this;
        }

        /**
         * 设置更新对话框取消按钮文案
         *
         * @param download_dialog_but_no
         * @return
         */
        public VersionInfo setDownload_dialog_but_no(String download_dialog_but_no) {
            this.download_dialog_but_no = download_dialog_but_no;
            return this;
        }

        /**
         * 设置更新对话框更新按钮文案
         *
         * @param download_dialog_but_ok
         * @return
         */
        public VersionInfo setDownload_dialog_but_ok(String download_dialog_but_ok) {
            this.download_dialog_but_ok = download_dialog_but_ok;
            return this;
        }

        public UpdateApp build() {
            if (this.url.equals(""))
                throw new IllegalArgumentException("versionInfo.url == ''");
            if (this.newVersion.equals(""))
                throw new IllegalArgumentException("versionInfo.newVersion == ''");
            return new UpdateApp(this);
        }
    }


    /**
     * 启动服务，开始下载
     */
    public static void startUpdata(VersionInfo mVersionInfo) {
        Intent intent = new Intent(activity, DownloadService.class);
        intent.putExtra("mVersionInfo", mVersionInfo);
        activity.startService(intent);
    }


    public void clear() {
        activity = null;
        //取消注册广播,防止内存泄漏
        localBroadcastManager.unregisterReceiver(mDownloadBroadcastManager);
    }

}
