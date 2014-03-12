package com.bingoogol.resource.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.bingoogol.resource.R;

/**
 * @author bingoogol@sina.com 14-2-9.
 */
public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                finish();
                startActivity(homeIntent);

            }
        },500);
    }
}
