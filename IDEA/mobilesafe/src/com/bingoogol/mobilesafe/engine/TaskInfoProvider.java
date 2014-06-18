package com.bingoogol.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingoogol@sina.com on 14-4-5.
 */
public class TaskInfoProvider {
    /**
     * 获取正在运行的进程信息
     * @param context
     * @return
     */
    public  static List<TaskInfo> getTaskInfos(Context context){
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo : processInfos){
            TaskInfo taskInfo = new TaskInfo();
            String packname = processInfo.processName;
            taskInfo.setPackname(packname);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0){
                    taskInfo.setUserTask(false);
                }else{
                    taskInfo.setUserTask(true);
                }
                String appName = applicationInfo.loadLabel(pm).toString();
                taskInfo.setAppName(appName);
                Drawable taskIcon = applicationInfo.loadIcon(pm);
                taskInfo.setTaskIcon(taskIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                System.out.println(packname+"当前应用程序不是标准的apk");
                taskInfo.setAppName(packname);
                taskInfo.setTaskIcon(context.getResources().getDrawable(R.drawable.default_icon));
            }

            long memsize = am.getProcessMemoryInfo(new int[]{processInfo.pid})[0].getTotalPrivateDirty()*1024;
            taskInfo.setMemsize(memsize);
            taskInfos.add(taskInfo);
        }

        return taskInfos;
    }
}
