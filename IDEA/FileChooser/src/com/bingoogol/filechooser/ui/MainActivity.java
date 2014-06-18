package com.bingoogol.filechooser.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bingoogol.filechooser.R;
import com.bingoogol.filechooser.util.Constants;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private TextView tv_main_result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main_result = (TextView) findViewById(R.id.tv_main_result);
    }

    public void showFileChooser(View view) {
        Intent intent = new Intent(this, FileChooserActivity.class);
        startActivityForResult(intent, Constants.requestCode.SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case Constants.requestCode.SELECT_FILE:
                    String filePath = data.getStringExtra("filePath");
                    tv_main_result.setText(filePath);
                    break;
            }
        }
    }
}
