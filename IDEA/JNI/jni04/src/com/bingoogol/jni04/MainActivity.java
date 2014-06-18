package com.bingoogol.jni04;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.bingoogol.jni04.engine.DataProvider;

public class MainActivity extends Activity implements View.OnClickListener {
    static {
        System.loadLibrary("jni04");
    }

    private Button btn_main_jni1;
    private Button btn_main_jni2;
    private Button btn_main_jni3;
    private Button btn_main_jni4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_main_jni1 = (Button) findViewById(R.id.btn_main_jni1);
        btn_main_jni2 = (Button) findViewById(R.id.btn_main_jni2);
        btn_main_jni3 = (Button) findViewById(R.id.btn_main_jni3);
        btn_main_jni4 = (Button) findViewById(R.id.btn_main_jni4);
        btn_main_jni1.setOnClickListener(this);
        btn_main_jni2.setOnClickListener(this);
        btn_main_jni3.setOnClickListener(this);
        btn_main_jni4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        DataProvider dataProvider = new DataProvider();
        switch (v.getId()) {
            case R.id.btn_main_jni1:
                Toast.makeText(this, dataProvider.add(2,3) + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_jni2:
                Toast.makeText(this, dataProvider.sayHelloInc("bingo"), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_jni3:
                int[] arr = new int[]{1,2,3,4,5};
                dataProvider.intMethod(arr);
                for(int i = 0;i < arr.length; i++) {
                    Log.i("jni","java中   " + arr[i]);
                }
                break;
            case R.id.btn_main_jni4:
                Toast.makeText(this, DataProvider.sub(2,3) + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
