package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.domain.TaskInfo;
import com.bingoogol.mobilesafe.engine.TaskInfoProvider;
import com.bingoogol.mobilesafe.util.ToastUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingoogol@sina.com on 14-4-5.
 */
public class TaskManagerActivity extends Activity {
    private TextView tv_process_count;
    private TextView tv_mem_info;
    private int runningProcessCount;
    private long totalMem;
    private long availMem;

    private ProgressBar loading;

    private List<TaskInfo> userTaskInfos;

    private List<TaskInfo> systemTaskInfos;

    private ListView lv_taskmanager;

    private TaskManagerAdapter adapter;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            loading.setVisibility(View.INVISIBLE);
            adapter = new TaskManagerAdapter();
            // 设置listview的数据适配器.
            lv_taskmanager.setAdapter(adapter);
        }

        ;
    };
    private TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        runningProcessCount = getRunningProcessCount();

        totalMem = getTotalMemSize();
        availMem = getAvailMemSize();
        tv_status = (TextView) findViewById(R.id.tv_status);
        lv_taskmanager = (ListView) findViewById(R.id.lv_taskmanager);

        loading = (ProgressBar) findViewById(R.id.loading);

        setTitle();

        fillData();

        lv_taskmanager.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (userTaskInfos != null && systemTaskInfos != null) {
                    if (firstVisibleItem >= (userTaskInfos.size() + 1)) {
                        tv_status.setText("系统进程:" + systemTaskInfos.size()
                                + "个");
                    } else {
                        tv_status.setText("用户进程:" + userTaskInfos.size() + "个");
                    }

                }
            }
        });

        lv_taskmanager.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 点击的条目所对应的数据.
                TaskInfo taskInfo = (TaskInfo) lv_taskmanager
                        .getItemAtPosition(position);

                if (taskInfo.getPackname().equals(getPackageName())) {
                    return;
                }

                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_checked);
                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                    cb.setChecked(false);
                } else {
                    taskInfo.setChecked(true);
                    cb.setChecked(true);
                }

            }
        });

    }

    private void setTitle() {
        tv_process_count.setText("运行中的进程:" + runningProcessCount + "个");
        tv_mem_info.setText("可用/总内存:"
                + Formatter.formatFileSize(this, availMem) + "/"
                + Formatter.formatFileSize(this, totalMem));
    }

    private void fillData() {
        loading.setVisibility(View.VISIBLE);

        new Thread() {
            public void run() {
                List<TaskInfo> taskInfos = TaskInfoProvider
                        .getTaskInfos(TaskManagerActivity.this);
                userTaskInfos = new ArrayList<TaskInfo>();
                systemTaskInfos = new ArrayList<TaskInfo>();
                for (TaskInfo taskInfo : taskInfos) {
                    if (taskInfo.isUserTask()) {
                        userTaskInfos.add(taskInfo);
                    } else {
                        systemTaskInfos.add(taskInfo);
                    }
                }

                handler.sendEmptyMessage(0);

            }

            ;
        }.start();

    }

    /**
     * 获取正在运行的进程的个数
     *
     * @return
     */
    private int getRunningProcessCount() {
        // PackageManager 程序管理器 管理静态的信息.
        // ActivityManager 进程管理器 任务管理器
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }

    /**
     * 获取可用的内存信息
     *
     * @return long byte 单位 大小
     */
    private long getAvailMemSize() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取中的内存信息
     *
     * @return long byte 单位 大小
     */
    private long getTotalMemSize() {
        // ActivityManager am = (ActivityManager)
        // getSystemService(ACTIVITY_SERVICE);
        // MemoryInfo outInfo = new MemoryInfo();
        // am.getMemoryInfo(outInfo);
        // return outInfo.totalMem;
        try {
            FileInputStream fis = new FileInputStream("/proc/meminfo");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String result = br.readLine();
            br.close();
            fis.close();
            // MemTotal: 513000 kB
            StringBuffer sb = new StringBuffer();
            for (char c : result.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString()) * 1024;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 全部选择
     *
     * @param view
     */
    public void selectAll(View view) {
        for (TaskInfo info : userTaskInfos) {
            if (info.getPackname().equals(getPackageName())) {
                info.setChecked(false);
            } else {
                info.setChecked(true);
            }
        }
        for (TaskInfo info : systemTaskInfos) {
            info.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 全部反选
     *
     * @param view
     */
    public void deSelectAll(View view) {
        for (TaskInfo info : userTaskInfos) {
            info.setChecked(!info.isChecked());
            if (info.getPackname().equals(getPackageName())) {
                info.setChecked(false);
            }
        }
        for (TaskInfo info : systemTaskInfos) {
            info.setChecked(!info.isChecked());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 杀死选中的进程
     */
    public void killAll(View view) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int killcount = 0;
        long savedMem = 0;
        List<TaskInfo> killedInfos = new ArrayList<TaskInfo>();
        for (TaskInfo info : userTaskInfos) {
            if (info.isChecked()) {
                am.killBackgroundProcesses(info.getPackname());
                killcount++;
                savedMem += info.getMemsize();
                killedInfos.add(info);
            }
        }
        for (TaskInfo info : systemTaskInfos) {
            if (info.isChecked()) {
                am.killBackgroundProcesses(info.getPackname());
                killcount++;
                savedMem += info.getMemsize();
                killedInfos.add(info);
            }
        }
        // Toast.makeText(
        // this,
        // "杀死了" + killcount + "个进程\n" + "释放了"
        // + Formatter.formatFileSize(this, savedMem) + "的内存", 0)
        // .show();
        ToastUtil.makeText(
                this,
                "杀死了" + killcount + "个进程\n" + "释放了"
                        + Formatter.formatFileSize(this, savedMem) + "的内存");
        runningProcessCount -= killcount;
        availMem += savedMem;
        // fillData(); 影响用户体验 得到的真实的数据
        setTitle();
        for (TaskInfo info : killedInfos) {
            if (info.isUserTask()) {
                userTaskInfos.remove(info);
            } else {
                systemTaskInfos.remove(info);
            }
        }
        // 通知界面更新. 自己修改了 集合里面的数据 不会让listview回到开头
        adapter.notifyDataSetChanged();

    }

    static class ViewHolder {
        ImageView iv;
        TextView tv_name;
        TextView tv_mem;
        CheckBox cb;
    }

    private class TaskManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            SharedPreferences sp = getSharedPreferences("config",
                    Context.MODE_PRIVATE);
            if (sp.getBoolean("showsystem", true)) {
                return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
            } else {
                return userTaskInfos.size() + 1;
            }
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0) {
                return false;
            } else if (position == (userTaskInfos.size() + 1)) {
                return false;
            }
            return true;
        }

        @Override
        public Object getItem(int position) {
            TaskInfo taskInfo = null;
            if (position == 0) {
                return null;
            } else if (position == (userTaskInfos.size() + 1)) {
                return null;
            } else if (position <= userTaskInfos.size()) {// 用户进程
                int newposition = position - 1;
                taskInfo = userTaskInfos.get(newposition);
            } else {// 系统进程
                int newposition = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemTaskInfos.get(newposition);
            }
            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskInfo taskInfo = null;
            if (position == 0) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.BLUE);
                tv.setText("用户进程:" + userTaskInfos.size() + "个");
                return tv;
            } else if (position == (userTaskInfos.size() + 1)) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.BLUE);
                tv.setText("系统进程:" + systemTaskInfos.size() + "个");
                return tv;
            } else if (position <= userTaskInfos.size()) {// 用户进程
                int newposition = position - 1;
                taskInfo = userTaskInfos.get(newposition);
            } else {// 系统进程
                int newposition = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemTaskInfos.get(newposition);
            }
            View view;
            ViewHolder holder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.task_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_task_icon);
                holder.tv_mem = (TextView) view.findViewById(R.id.tv_task_mem);
                holder.tv_name = (TextView) view
                        .findViewById(R.id.tv_task_name);
                holder.cb = (CheckBox) view.findViewById(R.id.cb_checked);
                view.setTag(holder);
            }
            if (taskInfo.getPackname().equals(getPackageName())) {
                holder.cb.setVisibility(View.INVISIBLE);
            } else {
                holder.cb.setVisibility(View.VISIBLE);
            }
            holder.iv.setImageDrawable(taskInfo.getTaskIcon());
            holder.tv_mem.setText("占用内存:"
                    + Formatter.formatFileSize(getApplicationContext(),
                    taskInfo.getMemsize()));
            holder.tv_name.setText(taskInfo.getAppName());
            holder.cb.setChecked(taskInfo.isChecked());

            return view;
        }

    }
}