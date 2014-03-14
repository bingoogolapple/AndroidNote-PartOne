package com.bingoogol.a4aexample;

import android.os.Bundle;
import android.widget.TextView;
import com.bingoogol.a4a.A4AActivity;
import com.bingoogol.a4a.A4ALayout;
import com.bingoogol.a4a.A4AView;

@A4ALayout(R.layout.main)
public class MyActivity extends A4AActivity {
    @A4AView
    private TextView tv_main_test;
    @Override
    protected void afterAutowire(Bundle savedInstanceState) {
        tv_main_test.setText("呵呵");
    }
}
