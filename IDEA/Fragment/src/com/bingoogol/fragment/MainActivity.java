package com.bingoogol.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bingoogol.fragment.demo1.Demo1Activity;
import com.bingoogol.fragment.demo2.Demo2Activity;
import com.bingoogol.fragment.demo3.Demo3Activity;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_demo1:
                Intent demo1 = new Intent(this, Demo1Activity.class);
                startActivity(demo1);
                break;
            case R.id.btn_main_demo2:
                Intent demo2 = new Intent(this, Demo2Activity.class);
                startActivity(demo2);
                break;
            case R.id.btn_main_demo3:
                Intent demo3 = new Intent(this, Demo3Activity.class);
                startActivity(demo3);
                break;
        }
    }
}
