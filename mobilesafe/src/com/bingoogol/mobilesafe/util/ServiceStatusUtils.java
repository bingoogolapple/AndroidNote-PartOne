package com.bingoogol.mobilesafe.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * @author bingoogol@sina.com 14-2-16.
 */
public class ServiceStatusUtils {

    /**
     * 检测服务的运行状态
     * @param context
     * @param servicename 服务的完整类名
     * @return
     */
    public static boolean isServiceRunning(Context context, String servicename){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos =	am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info: infos){
            if(servicename.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
