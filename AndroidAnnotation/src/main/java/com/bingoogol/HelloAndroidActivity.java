package com.bingoogol;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class HelloAndroidActivity extends Activity {

    @AfterViews
    public void afterViewInjection() {
        Toast.makeText(this,"呵呵",Toast.LENGTH_LONG).show();
    }
}

