package com.zhx.lib_updeta_app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

/**
 * Created by zhx on 2018/5/8.
 */

public class CommitUtils {

    /**
     * 版本更新比较三个版本（配置时要注意但是最新版本不能小于最小版本）
     *
     * @param current_version 最新版本
     * @param min_version     最小版本
     * @return
     */
    public static int VersionComparison(String current_version, String min_version, String my_version) {

//        状态   1：必须更新  2：可以不更新  ;0：不要更新
        int staet = 0;

//        最新版本
        String[] current_version_arrt = current_version.split("\\.");
//        最低要求版本
        String[] min_version_arrt = min_version.split("\\.");
//        当前版本
        String[] my_version_arrt = my_version.split("\\.");

        int size_1 = Math.max(my_version_arrt.length, min_version_arrt.length);
        int size = Math.max(current_version_arrt.length, size_1);
        for (int i = 0; i < size; i++) {
            staet = VersionComparison(i, current_version_arrt, min_version_arrt, my_version_arrt);
            if (staet != -1) {
                break;
            }
        }
        if (staet == -1) {
            staet = 0;
        }

        return staet;
    }


    /**
     * 辅助VersionComparison（）
     *
     * @param i
     * @param current_version_arrt
     * @param min_version_arrt
     * @param my_version_arrt
     * @return
     */
    private static int VersionComparison(int i, String[] current_version_arrt, String[] min_version_arrt, String[] my_version_arrt) {
//        状态   1：必须更新  2：可以不更新  ;0：不要更新
        int staet = -1;
        int int_current_version, int_min_version, int_my_version;
        if (current_version_arrt.length > i) {
            int_current_version = Integer.valueOf(current_version_arrt[i]);
        } else {
            int_current_version = 0;
        }
        if (min_version_arrt.length > i) {
            int_min_version = Integer.valueOf(min_version_arrt[i]);
        } else {
            int_min_version = 0;
        }
        if (my_version_arrt.length > i) {
            int_my_version = Integer.valueOf(my_version_arrt[i]);
        } else {
            int_my_version = 0;
        }
        if (int_my_version < int_current_version) {
            staet = 2;
            if (int_my_version < int_min_version) {
                staet = 1;
            }
            return staet;
        }
        if (int_my_version < int_min_version && int_my_version < int_current_version) {
            staet = 1;
        } else if (int_my_version >= int_min_version && int_my_version < int_current_version) {
            staet = 2;
        } else if (int_my_version > int_min_version && int_my_version > int_current_version) {
            staet = 0;
        } else if (int_my_version == int_min_version && int_my_version == int_current_version) {
            staet = -1;
        }


        return staet;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 屏幕宽
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 屏幕高
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
