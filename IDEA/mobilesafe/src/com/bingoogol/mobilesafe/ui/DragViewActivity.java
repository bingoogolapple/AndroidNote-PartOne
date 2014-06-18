package com.bingoogol.mobilesafe.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.util.Logger;
import net.tsz.afinal.FinalActivity;

/**
 * @author bingoogol@sina.com 14-2-18.
 */
public class DragViewActivity extends FinalActivity {

    private static final String TAG = "DragViewActivity";
    private ImageView iv_drag;
    private TextView tv_drag;

    private int screenHeight;
    private int screenWidth;

    private SharedPreferences sp;

    long[] mHits = new long[2];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        // 整个手机的高度，保护状态栏
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        tv_drag = (TextView) findViewById(R.id.tv_drag);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        int lastx = sp.getInt("lastx", 0);
        int lasty = sp.getInt("lasty", 0);

        Logger.i(TAG, "距离顶部:" + lasty + "--" + "距离左边的距离:" + lastx);

        // 获取状态栏的高度,控件屏幕上的y坐标减去控件在窗体上的y坐标
        //iv_drag.getLocationOnScreen();
        //iv_drag.getLocationInWindow();


        // 注意：在oncreate方法里面调用layout方法不会生效
        // 因为在oncreate方法执行的时候view对象还没有被显示出来
        // int h = iv_drag.getBottom() - iv_drag.getTop();
        // int w = iv_drag.getRight() - iv_drag.getLeft();
        // iv_drag.layout(lastx, lasty, lastx+w, lasty+h);

        // 在view对象渲染的第一个阶段生效
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_drag
                .getLayoutParams();
        params.leftMargin = lastx;
        params.topMargin = lasty;
        iv_drag.setLayoutParams(params);

        iv_drag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 数组中所有的值左移动一个位置
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                // 最后一个位置的值为当前时间
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    Logger.i(TAG, "双击事件.");
                    int w = iv_drag.getRight() - iv_drag.getLeft();

                    int l = screenWidth / 2 - w / 2;
                    int r = screenWidth / 2 + w / 2;

                    iv_drag.layout(l, iv_drag.getTop(), r, iv_drag.getBottom());
                    int lastx = iv_drag.getLeft();
                    int lasty = iv_drag.getTop();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("lastx", lastx);
                    editor.putInt("lasty", lasty);
                    editor.commit();
                }
            }
        });

        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指第一次触摸到屏幕
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        Logger.i(TAG, "开始坐标:" + startX + "-" + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指移动
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();

                        int dx = newX - startX;
                        int dy = newY - startY;
                        Logger.i(TAG, "移动了:" + dx + "-" + dy);

                        // 计算出来控件原来的位置
                        int l = iv_drag.getLeft();
                        int r = iv_drag.getRight();
                        int t = iv_drag.getTop();
                        int b = iv_drag.getBottom();

                        int newt = t + dy;
                        int newb = b + dy;
                        int newl = l + dx;
                        int newr = r + dx;

                        if (newl < 0 || newt < 0 || newr > screenWidth
                                || newb > screenHeight) {
                            break;
                        }

                        // 计算textview有多高
                        int tv_height = tv_drag.getBottom() - tv_drag.getTop();

                        if (newt > screenHeight / 2) {
                            // 图片在下方，文字在上方
                            tv_drag.layout(tv_drag.getLeft(), 0,
                                    tv_drag.getRight(), tv_height);
                        } else {
                            // 图片在上方，文字在下方，估计状态栏高度为40
                            tv_drag.layout(tv_drag.getLeft(), screenHeight
                                    - tv_height - 40, tv_drag.getRight(),
                                    screenHeight - 40);
                        }

                        // 鏇存柊iv鍦ㄥ睆骞曠殑浣嶇疆.
                        iv_drag.layout(newl, newt, newr, newb);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        Logger.i(TAG, "移动时的新坐标:" + startX + "-" + startY);

                        break;
                    case MotionEvent.ACTION_UP: // 手指离开屏幕时
                        int lastx = iv_drag.getLeft();
                        int lasty = iv_drag.getTop();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", lastx);
                        editor.putInt("lasty", lasty);
                        editor.commit();
                        break;
                }
                // 返回false不销毁掉事件，该控件所注册的点击事件会帮助销毁该事件（既注册点击事件，又注册触摸时间时返回false）
                // 返回true则该控件注册的点击事件不会生效，必须自己销毁掉
                return false;
            }
        });
    }
}