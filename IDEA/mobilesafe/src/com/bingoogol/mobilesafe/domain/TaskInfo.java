package com.bingoogol.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by bingoogol@sina.com on 14-4-5.
 */
public class TaskInfo {
    private Drawable taskIcon;
    private String appName;
    private String packname;
    private long memsize;
    private boolean checked;


    private boolean userTask;


    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    @Override
    public String toString() {
        return "TaskInfo [appName=" + appName + ", packname=" + packname
                + ", memsize=" + memsize + "]";
    }

    public Drawable getTaskIcon() {
        return taskIcon;
    }

    public void setTaskIcon(Drawable taskIcon) {
        this.taskIcon = taskIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
