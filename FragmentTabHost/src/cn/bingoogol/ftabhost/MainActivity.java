package cn.bingoogol.ftabhost;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import cn.bingoogol.ftabhost.fragment.FragmentOne;
import cn.bingoogol.ftabhost.fragment.FragmentThree;
import cn.bingoogol.ftabhost.fragment.FragmentTwo;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    private FragmentTabHost mTabHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreate");
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        // 添加tab名称和图标
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1");
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2");
        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3");
        tab1.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签一"));
        tab2.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签二"));
        tab3.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签三"));
        mTabHost.addTab(tab1, FragmentOne.class, null);
        mTabHost.addTab(tab2, FragmentTwo.class, null);
        mTabHost.addTab(tab3, FragmentThree.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                Log.i(TAG, "tabId:" + tabId);
            }
        });
    }

    private View getIndicatorView(int icon, String name) {
        View view = View.inflate(this, R.layout.item_tab, null);
        view.findViewById(R.id.iv_icon).setBackgroundResource(icon);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(name);
        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG,"onStop");
    }
}