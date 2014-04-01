package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.db.dao.AppLockDao;
import com.bingoogol.mobilesafe.domain.AppInfo;
import com.bingoogol.mobilesafe.engine.AppInfoProvider;
import com.bingoogol.mobilesafe.util.DelayExecuter;

import java.util.ArrayList;
import java.util.List;

public class ApplockActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_locked;
    private LinearLayout ll_unlock;

    private TextView tv_locked;
    private TextView tv_unlock;

    private ListView lv_locked;
    private ListView lv_unlock;

    private TextView tv_unlock_count;
    private TextView tv_locked_count;

    private AppLockAdapter unlockAdapter;
    private AppLockAdapter lockedAdapter;


    private View loading;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            loading.setVisibility(View.INVISIBLE);
            unlockAdapter = new AppLockAdapter(true);
            lv_unlock.setAdapter(unlockAdapter);
            lockedAdapter = new AppLockAdapter(false);
            lv_locked.setAdapter(lockedAdapter);
        };
    };

    /**
     * 所有的未加锁的应用程序集合
     */
    private List<AppInfo> unlockAppInfos;

    /**
     * 所有的已加锁的应用程序集合
     */
    private List<AppInfo> lockedAppInfos;

    private AppLockDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        dao = new AppLockDao(this);
        ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        tv_locked = (TextView) findViewById(R.id.tv_locked);
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);

        tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
        tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);

        tv_locked.setOnClickListener(this);
        tv_unlock.setOnClickListener(this);

        lv_locked = (ListView) findViewById(R.id.lv_locked);
        lv_unlock = (ListView) findViewById(R.id.lv_unlock);

        loading = findViewById(R.id.loading);
        fillData();


        /**
         * 设置未加锁条目的点击事件
         */
        lv_unlock.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                //动画播放是需要一定的时间的.
                Animation an = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appitem_remove_unlock);
                view.startAnimation(an);



                //子线程延迟一定的时间,主线程更新ui

                DelayExecuter executer = new DelayExecuter() {

                    @Override
                    public void onPostExecute() {
                        AppInfo appInfo = unlockAppInfos.get(position);
                        unlockAppInfos.remove(appInfo);
                        lockedAppInfos.add(appInfo);
                        //添加到已经加锁的数据库
                        dao.add(appInfo.getPackname());
                        //通知数据适配器更新,刚才的这个view对象已经不再了.比的条目复用这个view对象
                        lockedAdapter.notifyDataSetChanged();
                        unlockAdapter.notifyDataSetChanged();

                    }
                };

                executer.execute(300);




            }
        });
        //已加锁条目的点击事件
        lv_locked.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {


                Animation an = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appitem_remove_locked);
                view.startAnimation(an);

                DelayExecuter executer = new DelayExecuter() {

                    @Override
                    public void onPostExecute() {
                        AppInfo appInfo = lockedAppInfos.get(position);
                        lockedAppInfos.remove(appInfo);
                        unlockAppInfos.add(appInfo);
                        //从已经加锁的数据库删除
                        dao.delete(appInfo.getPackname());
                        //通知数据适配器更新
                        lockedAdapter.notifyDataSetChanged();
                        unlockAdapter.notifyDataSetChanged();

                    }
                };
                executer.execute(300);

            }
        });




    }

    private void fillData() {
        loading.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                List<AppInfo> allAppinfos = AppInfoProvider
                        .getAppInfos(ApplockActivity.this);
                //初始化未加锁的程序集合
                unlockAppInfos = new ArrayList<AppInfo>();
                //初始化已加锁程序集合
                lockedAppInfos = new ArrayList<AppInfo>();


                for(AppInfo appInfo : allAppinfos){
                    if(dao.find(appInfo.getPackname())){
                        lockedAppInfos.add(appInfo);
                    }else{
                        unlockAppInfos.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            };
        }.start();

    }

    private class AppLockAdapter extends BaseAdapter {
        private boolean unlockFlag;

        /**
         * 构造方法
         *
         * @param unlockFlag
         *            true 未加锁的数据适配器 false 已加锁的数据适配器
         */
        public AppLockAdapter(boolean unlockFlag) {
            this.unlockFlag = unlockFlag;
        }

        @Override
        public int getCount() {
            if (unlockFlag) {// 未加锁应用
                tv_unlock_count.setText("未加锁应用" + unlockAppInfos.size() + "个");
                return unlockAppInfos.size();

            } else {// 已加锁应用
                tv_locked_count.setText("已加锁应用" + lockedAppInfos.size() + "个");
                return lockedAppInfos.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo;
            ViewHolder holder;

            View view;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.app_lock_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_applock_icon);
                holder.iv_status = (ImageView) view
                        .findViewById(R.id.iv_status);
                holder.tv_name = (TextView) view
                        .findViewById(R.id.tv_applock_name);
                view.setTag(holder);
            }
            if (unlockFlag) {// 未加锁应用
                appInfo = unlockAppInfos.get(position);
                holder.iv_status.setImageResource(R.drawable.lock);
            } else {// 已加锁应用
                appInfo = lockedAppInfos.get(position);
                holder.iv_status.setImageResource(R.drawable.unlock);
            }

            holder.iv.setImageDrawable(appInfo.getAppIcon());
            holder.tv_name.setText(appInfo.getAppName());

            return view;
        }

    }

    static class ViewHolder {
        ImageView iv;
        TextView tv_name;
        ImageView iv_status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_locked:
                tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                ll_locked.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);

                break;
            case R.id.tv_unlock:
                tv_locked.setBackgroundResource(R.drawable.tab_right_default);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);

                ll_unlock.setVisibility(View.VISIBLE);
                ll_locked.setVisibility(View.GONE);
                break;
        }

    }
}