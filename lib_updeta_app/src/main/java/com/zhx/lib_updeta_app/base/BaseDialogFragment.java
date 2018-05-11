package com.zhx.lib_updeta_app.base;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zhx.lib_updeta_app.R;
import com.zhx.lib_updeta_app.config.UpdateApp;
import com.zhx.lib_updeta_app.utils.CommitUtils;

/**
 * Created by zhx on 2018/5/10.
 */

public abstract class BaseDialogFragment extends DialogFragment {


    protected static UpdateApp.VersionInfo mVersionInfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(false);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = CommitUtils.getWidth(getActivity());
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(attributes);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.shape_rectangle_black_2d2d2d_bg);
    }

}
