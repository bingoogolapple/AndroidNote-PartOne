package com.bingoogol.mobilesafe.ui;

import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.util.Logger;
import net.tsz.afinal.FinalActivity;

/**
 * @author bingoogol@sina.com 14-2-13.
 */
public abstract class BaseSetupActivity extends FinalActivity {
    protected static final String TAG = "BaseSetupActivity";

    protected SharedPreferences sp ;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX) < 100) {
                    Logger.i(TAG,"滑动的太慢，不是有效的事件");
                    return true;
                }
                if(e2.getRawX() - e1.getRawX() > 200) {
                    pre(null);
                    return true;
                }
                if(e1.getRawX() - e2.getRawX() > 200) {
                    next(null);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        initLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void next(View view){
        showNext();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }

    public void pre(View view){
        showPre();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    public abstract void initLayout();

    public abstract void showNext();

    public abstract void showPre();
}
