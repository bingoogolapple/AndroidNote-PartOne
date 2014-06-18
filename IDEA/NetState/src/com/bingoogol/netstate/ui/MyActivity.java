package com.bingoogol.netstate.ui;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.bingoogol.netstate.R;
import com.bingoogol.netstate.receiver.ConnectivityChangeReceiver;
import com.bingoogol.netstate.util.ConnectivityUtil;
import com.bingoogol.netstate.util.ToastUtil;

public class MyActivity extends Activity implements OnClickListener {
    private Button isConnectedBtn;
    private Button isWifiConnectedBtn;
    private ConnectivityChangeReceiver connectivityChangeReceiver = new ConnectivityChangeReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        registerConnectivityChangeReceiver();
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityChangeReceiver);
    }

    private void registerConnectivityChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityChangeReceiver,intentFilter);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        isConnectedBtn = (Button) this.findViewById(R.id.isConnected);
        isWifiConnectedBtn = (Button) this.findViewById(R.id.isWifiConnected);
    }

    /**
     * 给组件添加事件监听器
     */
    private void setListener() {
        isConnectedBtn.setOnClickListener(this);
        isWifiConnectedBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.isConnected:
                if(ConnectivityUtil.isConnected(this)) {
                    ToastUtil.makeText(this,"已链接网络");
                } else {
                    ToastUtil.makeText(this,"未链接网络");
                }
                break;
            case R.id.isWifiConnected:
                if(ConnectivityUtil.isWifiConnected(this)) {
                    ToastUtil.makeText(this,"已链接wifi");
                } else {
                    ToastUtil.makeText(this,"未链接wifi");
                }
                break;
        }
    }
}
