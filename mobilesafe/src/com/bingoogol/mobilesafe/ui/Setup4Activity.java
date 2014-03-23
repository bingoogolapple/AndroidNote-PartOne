package com.bingoogol.mobilesafe.ui;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.receiver.MyAdminReceiver;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-13.
 */
public class Setup4Activity extends BaseSetupActivity {

    @ViewInject(id = R.id.cb_setup4_status)
    private CheckBox cb_setup4_status;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_setup4);
        boolean protecting = sp.getBoolean("protecting",false);
        cb_setup4_status.setChecked(protecting);
        if(protecting) {
            cb_setup4_status.setText("防盗保护已开启");
        } else {
            cb_setup4_status.setText("防盗保护已关闭");
        }
        cb_setup4_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cb_setup4_status.setText("防盗保护已开启");
                } else {
                    cb_setup4_status.setText("防盗保护已关闭");
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }

    public void activeAdmin(View view) {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName mDeviceAdminSample = new ComponentName(this,
                MyAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "95%的用户都开启了");
        startActivity(intent);

    }

    @Override
    public void showNext() {
        // 设置一个flag说明设置想到已经完成了
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isconfig",true);
        editor.commit();

        Intent intent = new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
    }
}