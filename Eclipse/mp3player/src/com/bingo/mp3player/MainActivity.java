package com.bingo.mp3player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        //得到TabHost对象，针对TabActivity的操作通常都由这个对象完成
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, RemoteMp3ListActivity.class);
        //生成一个TabSpec对象，这个对象代表了一页
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
        //设置该页的Indicator（指示器的意思）
        remoteSpec.setIndicator("Remote", res.getDrawable(android.R.drawable.stat_sys_download));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);
        
        Intent localIntent = new Intent();
        localIntent.setClass(this, LocalMp3ListActivity.class);
        TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
        localSpec.setIndicator("Local", res.getDrawable(android.R.drawable.stat_sys_download_done));
        localSpec.setContent(localIntent);
        tabHost.addTab(localSpec);
    }
    
}
