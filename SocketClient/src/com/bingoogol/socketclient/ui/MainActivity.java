package com.bingoogol.socketclient.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import com.bingoogol.socketclient.R;
import com.bingoogol.socketclient.ui.DownloadActivity;
import com.bingoogol.socketclient.ui.SearchActivity;
import com.bingoogol.socketclient.ui.UploadActivity;

public class MainActivity extends TabActivity {
    private static final String TAG = "MainActivity";
    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = getTabHost();
        TabHost.TabSpec searchTab = mTabHost.newTabSpec("searchTab").setIndicator("搜索").setContent(new Intent(this, SearchActivity.class));
        TabHost.TabSpec uploadTab = mTabHost.newTabSpec("uploadTab").setIndicator("上传").setContent(new Intent(this,UploadActivity.class));
        TabHost.TabSpec downloadTab = mTabHost.newTabSpec("downloadTab").setIndicator("下载").setContent(new Intent(this,DownloadActivity.class));

        mTabHost.addTab(searchTab);
        mTabHost.addTab(uploadTab);
        mTabHost.addTab(downloadTab);
    }
}