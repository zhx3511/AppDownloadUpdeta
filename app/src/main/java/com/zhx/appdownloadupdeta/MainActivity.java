package com.zhx.appdownloadupdeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhx.lib_updeta_app.config.UpdateApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UpdateApp c = new UpdateApp.VersionInfo(this)
                .setUrl("http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk")
                .setNewVersion("1.1.0")
                .build();

    }
}
