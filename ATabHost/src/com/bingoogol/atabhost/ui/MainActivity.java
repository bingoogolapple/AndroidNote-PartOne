package com.bingoogol.atabhost.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import com.bingoogol.atabhost.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabOne").setIndicator("one").setContent(new Intent(this, OneActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tabTwo").setIndicator("two").setContent(new Intent(this, TwoActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tabThree").setIndicator("three").setContent(new Intent(this, ThreeActivity.class)));
    }
}
