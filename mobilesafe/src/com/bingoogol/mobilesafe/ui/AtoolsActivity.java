package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bingoogol.mobilesafe.R;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class AtoolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }


    public void addressQuery(View view){
        Intent intent = new Intent(this,NumberQueryActivity.class);
        startActivity(intent);
    }

    public void commonNumberQuery(View view){
        Intent intent = new Intent(this,CommonNumActivity.class);
        startActivity(intent);

    }
}
