package com.bingoogol.fragment.demo3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.bingoogol.fragment.R;

/**
 * @author bingoogol@sina.com 14-2-25.
 */
public class Demo3Activity extends Activity {
    // extends FragmentActivity
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);
        // fm = getSupportFragmentManager();
        // ft = fm.beginTransaction();
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.content, new JunShiFragment());
        ft.commit();
    }

    public void onClick(View v) {
        ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_demo3_junshi:
                ft.replace(R.id.content, new JunShiFragment());
                // ft.replace(R.id.content, new JunShiFragmentS(), "junshifragment");
                // ft.addToBackStack("junshifragment");
                break;
            case R.id.btn_demo3_shehui:
                ft.replace(R.id.content, new SheHuiFragment());
                break;
            case R.id.btn_demo3_shenghuo:
                ft.replace(R.id.content, new ShengHuoFragment());
                break;
            case R.id.btn_demo3_yule:
                ft.replace(R.id.content, new YuLeFragment());
                break;
        }
        ft.commit();
    }
}
