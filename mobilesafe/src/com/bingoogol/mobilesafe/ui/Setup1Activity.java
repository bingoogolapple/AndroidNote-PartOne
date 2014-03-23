package com.bingoogol.mobilesafe.ui;

import android.content.Intent;
import android.os.Bundle;
import com.bingoogol.mobilesafe.R;
import net.tsz.afinal.FinalActivity;

/**
 * @author bingoogol@sina.com 14-2-13.
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showNext() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPre() {

    }
}