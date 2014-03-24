package com.bingoogol.mobilesafe.service;

import android.app.Service;
import android.content.*;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.db.dao.AddressDao;
import com.bingoogol.mobilesafe.util.Logger;
import com.bingoogol.mobilesafe.util.ToastUtil;

/**
 * @author bingoogol@sina.com 14-2-16.
 */
public class PhoneStatusService  extends Service {
    private static final String TAG = "PhoneStatusService";
    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private InnerReceiver receiver;
    private WindowManager wm;
    private SharedPreferences sp;

    /**
     * 全局的显示在界面上的view对象
     */
    private View view;

    private class InnerReceiver extends BroadcastReceiver {

        private static final String TAG = "InnerReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i(TAG, "我是服务内部的广播接收者：我接收到了广播事件");
            String address = AddressDao.getAddress(getResultData());
//            ToastUtil.makeText(context,address);
            showAddress(address);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        // 采用代码的方式注册广播接收者
        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);

        // 注册一个监听器监听电话的状态
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }



    @Override
    public void onDestroy() {
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {

                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    ToastUtil.makeText(getApplicationContext(), "归属地:" + address);
                    showAddress(address);
                    break;
                // 空闲状态 电话挂断
                case TelephonyManager.CALL_STATE_IDLE:
                    if (view != null) {
                        wm.removeView(view);
                        view = null;
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private WindowManager.LayoutParams params;

    private void showAddress(String address) {
        Logger.i(TAG,"显示来电归属地");
        view = View.inflate(this, R.layout.call_location, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();

                        int dx = newX - startX;
                        int dy = newY - startY;

                        params.x +=dx;
                        params.y +=dy;

                        if(params.x<0){
                            params.x = 0;
                        }
                        if(params.y<0){
                            params.y = 0;
                        }
                        if(params.x>wm.getDefaultDisplay().getWidth()){
                            params.x = wm.getDefaultDisplay().getWidth();
                        }
                        if(params.y>wm.getDefaultDisplay().getHeight()){
                            params.y = wm.getDefaultDisplay().getHeight();
                        }
                        wm.updateViewLayout(view, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();


                        break;
                    case MotionEvent.ACTION_UP:
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", params.x);
                        editor.putInt("lasty", params.y);
                        editor.commit();
                        break;
                }
                return true;
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
        tv.setText(address);
        int which = sp.getInt("which", 0);
        int[] bgs = new int[] { R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_green, R.drawable.call_locate_gray };

        view.setBackgroundResource(bgs[which]);
        params =  new WindowManager.LayoutParams();
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.x = sp.getInt("lastx", 0);
        params.y = sp.getInt("lasty", 0);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        wm.addView(view, params);
    }
}