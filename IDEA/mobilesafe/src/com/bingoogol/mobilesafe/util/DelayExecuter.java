package com.bingoogol.mobilesafe.util;

import android.os.Handler;

public abstract class DelayExecuter {

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //主线程里面执行的代码
            onPostExecute();
        };
    };

    /**
     * 延时之后调用的方法  运行在主线程
     */
    public abstract void  onPostExecute();

    /**
     * 延时执行一个代码
     * @param delayMillis 时间的毫秒值
     */
    public void  execute(long delayMillis){
        handler.sendEmptyMessageDelayed(999, delayMillis);
    }

}