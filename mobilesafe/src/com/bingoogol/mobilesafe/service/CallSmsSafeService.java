package com.bingoogol.mobilesafe.service;

import android.app.Service;
import android.content.*;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.bingoogol.mobilesafe.db.dao.BlackNumberDao;
import com.bingoogol.mobilesafe.util.Logger;

import java.lang.reflect.Method;

/**
 * Created by bingoogol@sina.com on 14-3-30.
 */
public class CallSmsSafeService extends Service {
    private InnerSmsReceiver receiver;
    private BlackNumberDao dao;

    private TelephonyManager tm;
    private PhoneListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    //在服务里面创建一个 短信到来的广播接受者
    private class InnerSmsReceiver extends BroadcastReceiver {

        private static final String TAG = "InnerSmsReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {

            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj : objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                //检查sender是否在黑名单列表里面 检查拦截模式. 短信拦截 全部拦截
                String body = smsMessage.getMessageBody();

                if(body.contains("fapiao")){
                    Logger.i(TAG,"发现发票短信,垃圾短信拦截");
                    abortBroadcast();
                }
                String mode = dao.findMode(sender);
                if("1".equals(mode)||"3".equals(mode)){
                    Logger.i(TAG, "发现黑名单短信,拦截");
                    abortBroadcast();
                    //TODO:把短信的内容 和 电话号码 时间 存起来.
                    //定义出来一个私有的数据库
                }
            }

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumberDao(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new PhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        receiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);

    }


    private class PhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    String mode = dao.findMode(incomingNumber);
                    if("1".equals(mode)||"2".equals(mode)){
                        Logger.i("PhoneListener","挂断电话");
                        endCall();
                        //呼叫记录不是立刻产生的.
                        //不能立刻的删除呼叫记录. 注册一个内容观察者 观察呼叫记录的变化
                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(),incomingNumber));

                    }
                    break;

            }

        }


    }


    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;

        public CallLogObserver(Handler handler,String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            Logger.i("CallLogObserver","观察到了呼叫记录内容变化...");
            //内容观察者 已经完成了使命
            //取消内容观察者的注册
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }

    }


    /**
     * 挂断电话.
     * 调用系统隐藏的api 挂断电话
     */
    private void endCall() {
        //1.获取电话服务的代理对象 IBinder
        try {
            Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", new Class[]{String.class});
            IBinder iBinder = (IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //呼死你  3天

    /**
     * 删除呼叫记录
     * @param incomingNumber
     */
    public void deleteCallLog(String incomingNumber) {
        ContentResolver reslover = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        reslover.delete(uri, "number=?", new String[]{incomingNumber});
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
        super.onDestroy();
    }
}
