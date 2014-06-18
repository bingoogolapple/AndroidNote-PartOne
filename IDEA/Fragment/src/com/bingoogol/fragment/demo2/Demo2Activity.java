package com.bingoogol.fragment.demo2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import com.bingoogol.fragment.R;
import com.bingoogol.fragment.demo1.LeftFragment;
import com.bingoogol.fragment.demo1.RightFragment;

/**
 * @author bingoogol@sina.com 14-2-25.
 */
public class Demo2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.判断当前手机的朝向
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.i("bingo", "rotation:" + rotation);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //android.R.id.content代表当前activity的界面，也可以指定布局文件最外部元素的id
        if (rotation == Surface.ROTATION_90) {
            //水平方向
            LandscapeFragment landscapeFragment = new LandscapeFragment();
            ft.replace(android.R.id.content, landscapeFragment);
            //可以在另一个fragment中通过tag获取
//			ft.replace(containerViewId, fragment, tag);
        } else {
            //竖直方向
            PortraitFragment portraitFragment = new PortraitFragment();
            ft.replace(android.R.id.content, portraitFragment);
        }
        ft.commit();
    }
}
