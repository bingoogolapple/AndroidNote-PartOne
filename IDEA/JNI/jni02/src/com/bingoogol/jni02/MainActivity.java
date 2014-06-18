package com.bingoogol.jni02;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.bingoogol.jni02.engine.JNIProvider;

public class MainActivity extends Activity implements View.OnClickListener {

    static {
        System.loadLibrary("jni02");
    }

    private Button btn_main_jni1;
    private Button btn_main_jni2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_main_jni1 = (Button) findViewById(R.id.btn_main_jni1);
        btn_main_jni2 = (Button) findViewById(R.id.btn_main_jni2);
        btn_main_jni1.setOnClickListener(this);
        btn_main_jni2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        JNIProvider jniProvider = new JNIProvider();
        switch (v.getId()) {
            case R.id.btn_main_jni1:
                Toast.makeText(this,jniProvider.helloFromC(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_jni2:
                Toast.makeText(this,jniProvider.hello_from_c(),Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
