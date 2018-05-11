package com.zhx.lib_updeta_app;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhx.lib_updeta_app.base.BaseDialogFragment;
import com.zhx.lib_updeta_app.config.UpdateApp;
import com.zhx.lib_updeta_app.utils.CommitUtils;

/**
 * 是否更新对话框
 * Created by zhx on 2018/5/8.
 */

public class DialogUpdetaFragment extends BaseDialogFragment implements View.OnClickListener {



    public static DialogUpdetaFragment newInstance(UpdateApp.VersionInfo mVersionInfos) {
        mVersionInfo = mVersionInfos;
        DialogUpdetaFragment instance = new DialogUpdetaFragment();
        return instance;
    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mVersionInfo = (UpdateApp.VersionInfo) getArguments().getSerializable("mVersionInfo");

        ((TextView) view.findViewById(R.id.download_dialog_titile)).setText(mVersionInfo.download_dialog_titile == null ? "检测到有新版本" : mVersionInfo.download_dialog_titile);
        ((TextView) view.findViewById(R.id.download_dialog_content)).setText(mVersionInfo.download_dialog_content == null ? "" : mVersionInfo.download_dialog_content);
        ((TextView) view.findViewById(R.id.download_dialog_but_no)).setText(mVersionInfo.download_dialog_but_no == null ? "暂时不更新" : mVersionInfo.download_dialog_but_no);
        ((TextView) view.findViewById(R.id.download_dialog_but_ok)).setText(mVersionInfo.download_dialog_but_ok == null ? "立即更新" : mVersionInfo.download_dialog_but_ok);
        ((TextView) view.findViewById(R.id.download_dialog_but_ok)).setOnClickListener(this);
        if (!mVersionInfo.isForceUpdeta) {
            ((TextView) view.findViewById(R.id.download_dialog_but_no)).setOnClickListener(this);
            ((TextView) view.findViewById(R.id.download_dialog_but_no)).setVisibility(View.VISIBLE);

        } else {
            ((TextView) view.findViewById(R.id.download_dialog_but_no)).setVisibility(View.GONE);
        }

        if (mVersionInfo.view != null) {//弹出布局有外面定义传递进来
            ((LinearLayout) view.findViewById(R.id.layout_view)).removeAllViews();
            ((LinearLayout) view.findViewById(R.id.layout_view)).addView(mVersionInfo.view);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_updeta, container, false);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.download_dialog_but_no) {
            this.dismiss();
        } else if (i == R.id.download_dialog_but_ok) {
            this.dismiss();
            UpdateApp.startUpdata(mVersionInfo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVersionInfo = null;
    }
}
