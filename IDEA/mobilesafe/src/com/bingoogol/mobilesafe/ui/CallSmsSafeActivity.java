package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.db.dao.BlackNumberDao;
import com.bingoogol.mobilesafe.domain.BlackNumberInfo;

import java.util.List;

/**
 * Created by bingoogol@sina.com on 14-3-23.
 */
public class CallSmsSafeActivity extends Activity {
    private static final String TAG = "CallSmsSafeActivity";
    private ListView lv_callsms_safe;
    private BlackNumberDao dao;
    private List<BlackNumberInfo> infos;
    private View loading;
    /**
     * 一共有多少页
     */
    private int totalPageNumber = 0;
    /**
     * 初始页码
     */
    private int pagenumber = 0;
    /**
     * 每一次最多获取的数据
     */
    private int maxSize = 20;

    private CallSmsAdapter adapter;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            loading.setVisibility(View.INVISIBLE);
            if (adapter == null) {
                adapter = new CallSmsAdapter();
                lv_callsms_safe.setAdapter(adapter);
            } else {
                // 通知数据适配器数据更新.
                adapter.notifyDataSetChanged();
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
        /**
         * 给listview设置一个滚动状态的监听器
         */
        lv_callsms_safe.setOnScrollListener(new AbsListView.OnScrollListener() {

            /**
             * 当滚动状态变化
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
                        // 代表的是listview停止滚动了.
                        int lastPosition = lv_callsms_safe.getLastVisiblePosition(); // 19
                        int size = infos.size(); // 20
                        if (lastPosition == (size - 1)) {
                            // 滚动到最后面了.
                            // 加载下一页的数据
                            Log.i(TAG, "加载更多的数据");
                            pagenumber++;
                            if (pagenumber > totalPageNumber) {
                                Toast.makeText(getApplicationContext(), "没有更多的数据了",
                                        1).show();
                                // TextView tv = new
                                // TextView(getApplicationContext());
                                // tv.setText("没有了...");
                                // lv_callsms_safe.addFooterView(tv);
                                // lv_callsms_safe.setAdapter(adapter);
                                // lv_callsms_safe.setSelection(infos.size()-1);
                                return;
                            }
                            fillData();
                        }

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 触摸滚动

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING: // 滑翔状态.
                        break;
                }
            }

            /**
             * 滚动的时候调用
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        loading = findViewById(R.id.loading);
        dao = new BlackNumberDao(this);
        loading.setVisibility(View.VISIBLE);

        totalPageNumber = dao.getTotalPageNumber(maxSize);

        fillData();
    }

    private void fillData() {
        loading.setVisibility(View.VISIBLE);

        new Thread() {
            public void run() {
                /**
                 * 检查集合是否为空 ,如果不为空 新的数据添加到集合的里面.
                 */
                if (infos == null) {
                    infos = dao.findBlackNumberByPage(pagenumber, maxSize);
                } else {
                    infos.addAll(dao.findBlackNumberByPage(pagenumber, maxSize));
                }
                handler.sendEmptyMessage(0);
            };
        }.start();
    }

    private class CallSmsAdapter extends BaseAdapter {

        private static final String TAG = "CallSmsAdapter";

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            // 减少 布局文件转化成view对象的次数 减少了对象的创建 整个listview里面一共有多个相对布局 9个
            if (convertView != null && convertView instanceof RelativeLayout) {// 有被回收掉的历史缓存view对象.
                // Log.i(TAG, "使用缓存view对象");
                view = convertView;
                // 在使用缓存view对象的时候 不需要重复寻找孩子了 只需要获取到原来存放的孩子id的引用
                holder = (ViewHolder) view.getTag();

            } else {
                // Log.i(TAG, "创建新的view对象:" + position);
                view = View.inflate(getApplicationContext(),
                        R.layout.call_sms_item, null);
                // 2减少孩子控件的查找次数
                holder = new ViewHolder();// 创建一个容器 容器 存放孩子id的引用.
                holder.tv_mode = (TextView) view
                        .findViewById(R.id.tv_callsms_mode);
                holder.tv_number = (TextView) view
                        .findViewById(R.id.tv_callsms_number);
                holder.iv_callsms_delete = (ImageView) view
                        .findViewById(R.id.iv_callsms_delete);
                // 把holder对象跟view对象关联在一起.
                view.setTag(holder);
            }

            final BlackNumberInfo info = infos.get(position);
            String number = info.getNumber();
            holder.tv_number.setText("号码:" + number);
            String mode = info.getMode();

            holder.iv_callsms_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 知道被点击条目对应的电话.
                    String number = info.getNumber();
                    // 数据库移除黑名单号码
                    dao.delete(number);
                    // 界面上移除黑名单号码
                    infos.remove(info);
                    // 通知界面更新
                    adapter.notifyDataSetChanged();
                }
            });

            if ("1".equals(mode)) {
                holder.tv_mode.setText("全部拦截");
            } else if ("2".equals(mode)) {
                holder.tv_mode.setText("电话拦截");
            } else if ("3".equals(mode)) {
                holder.tv_mode.setText("短信拦截");
            }
            return view;
        }

    }

    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_callsms_delete;
    }

    /**
     * 添加一个黑名单号码
     */
    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View contentView = View.inflate(getApplicationContext(),
                R.layout.dialog_add_blacknumber, null);
        final EditText et_number = (EditText) contentView
                .findViewById(R.id.et_callsms_blacknumber);
        final RadioGroup rg_mode = (RadioGroup) contentView
                .findViewById(R.id.rg_mode);


        contentView.findViewById(R.id.bt_cancle).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        contentView.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String number = et_number.getText().toString().trim();
                        String mode = "1";
                        switch (rg_mode.getCheckedRadioButtonId()) {
                            case R.id.rb_all:
                                mode = "1";
                                break;
                            case R.id.rb_phone:
                                mode = "2";
                                break;
                            case R.id.rb_sms:
                                mode = "3";
                                break;
                        }
                        if(TextUtils.isEmpty(number)){
                            Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0).show();
                            return;
                        }
                        //把数据添加到数据库  //被加载数据库的最后一个条目.
                        dao.add(number, mode);
                        //显示在界面上.
                        BlackNumberInfo blackinfo = new BlackNumberInfo();
                        blackinfo.setMode(mode);
                        blackinfo.setNumber(number);
                        infos.add(0, blackinfo);
                        //通知界面更新
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        dialog.setView(contentView, 0, 0, 0, 0);
        dialog.show();
    }

}
