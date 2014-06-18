package com.bingoogol.mobilesafe.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.db.dao.CommonNumDao;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bingoogol@sina.com 14-2-20.
 */
public class CommonNumActivity extends FinalActivity {

    @ViewInject(id = R.id.elv_common_num)
    private ExpandableListView elv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_num);

        elv.setAdapter(new ExpandAdapter());

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                TextView tv = (TextView) v;

                String number = tv.getText().toString().trim().split("\n")[1];
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                return false;
            }
        });

    }

    private class ExpandAdapter extends BaseExpandableListAdapter {
        private List<String> groupNames;
        private Map<Integer, List<String>> childrenInfos = new HashMap<Integer, List<String>>();
        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            int x = 0;
            int y = 0;
            if(x == y) {
                x = 8;
            } else {
                y = 5;
            }
            super.onGroupExpanded(groupPosition);
        }

        /**
         * 返回有多个组
         */
        @Override
        public int getGroupCount() {
            groupNames = CommonNumDao.getGroupNames();
            return groupNames.size();
        }

        /**
         * 返回每个组有多少个孩子
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            List<String> childNames = null;
            if (childrenInfos.containsKey(groupPosition)) {// 集合里面已经有缓存数据了.
                childNames = childrenInfos.get(groupPosition);
            } else {
                // 集合里面还没有缓存数据 ,获取数据放在集合里面
                childNames = CommonNumDao
                        .getChildrenNamesByPosition(groupPosition);
                childrenInfos.put(groupPosition, childNames);
            }
            return childNames.size();
        }

        /**
         * 返回某个位置组对应的对象
         */
        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        /**
         * 返回某个组里面某个孩子的对象
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        /**
         * 获取分组的id
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取孩子的id
         */

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /**
         * 返回某个位置 分组对应的view对象
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplicationContext());
            tv.setTextColor(Color.RED);
            tv.setTextSize(22);
            // tv.setText("        "+CommonNumDao.getGroupNameByPosition(groupPosition));
            // 查询数据库
            // 查询内存
            tv.setText("        " + groupNames.get(groupPosition));
            return tv;
        }

        /**
         * 返回某个位置的孩子信息
         */
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplicationContext());
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            tv.setText(CommonNumDao.getChildNameByPosition(groupPosition,
                    childPosition));
            return tv;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
