package com.bingoogol.mobilesafe.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.service.CallSmsSafeService;
import com.bingoogol.mobilesafe.service.PhoneStatusService;
import com.bingoogol.mobilesafe.service.WatchDogService;
import com.bingoogol.mobilesafe.ui.sub.SettingView;
import com.bingoogol.mobilesafe.util.ServiceStatusUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-16.
 */
public class SettingActivity extends FinalActivity {
    private SharedPreferences sp;

    @ViewInject(id = R.id.sv_setting_showaddress)
    private SettingView sv_setting_showaddress;
    @ViewInject(id = R.id.sv_setting_autoupdate)
    private SettingView sv_setting_autoupdate;
    @ViewInject(id = R.id.sv_setting_callsmssafe)
    private SettingView sv_setting_callsmssafe;
    @ViewInject(id = R.id.sv_setting_applock)

    private SettingView sv_setting_applock;
    private Intent watchDogService;
    private Intent callSmsServiceIntent;

    private Intent showAddressIntent;

    @ViewInject(id = R.id.rl_setting_changebg)
    private View rl_setting_changebg;
    @ViewInject(id = R.id.tv_setting_bg)
    private TextView tv_setting_bg;
    @ViewInject(id = R.id.rl_setting_changexy)
    private View rl_setting_changexy;

    private String[] items = {"半透明", "活力橙", "卫士蓝", "苹果绿", "金属灰"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_setting);

        showAddressIntent = new Intent(this, PhoneStatusService.class);

        sv_setting_showaddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sv_setting_showaddress.isChecked()) {
                    stopService(showAddressIntent);
                    sv_setting_showaddress.setChecked(false);
                } else {
                    sv_setting_showaddress.setChecked(true);
                    startService(showAddressIntent);
                }
            }
        });

        boolean autoupdate = sp.getBoolean("autoupdate", false);
        sv_setting_autoupdate.setChecked(autoupdate);
        sv_setting_autoupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if (sv_setting_autoupdate.isChecked()) {
                    sv_setting_autoupdate.setChecked(false);
                    editor.putBoolean("autoupdate", false);
                } else {
                    sv_setting_autoupdate.setChecked(true);
                    editor.putBoolean("autoupdate", true);
                }
                editor.commit();
            }
        });


        int witch = sp.getInt("which", 0);
        tv_setting_bg.setText(items[witch]);
        rl_setting_changebg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showChangeBgDialog();
            }
        });

        rl_setting_changexy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DragViewActivity.class);
                startActivity(intent);
            }
        });
        callSmsServiceIntent = new Intent(this, CallSmsSafeService.class);
        sv_setting_callsmssafe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sv_setting_callsmssafe.isChecked()) {
                    sv_setting_callsmssafe.setChecked(false);
                    stopService(callSmsServiceIntent);
                } else {
                    sv_setting_callsmssafe.setChecked(true);
                    startService(callSmsServiceIntent);
                }
            }
        });
        watchDogService = new Intent(this,WatchDogService.class);

        sv_setting_applock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(sv_setting_applock.isChecked()){
                    sv_setting_applock.setChecked(false);
                    stopService(watchDogService);
                }else{
                    sv_setting_applock.setChecked(true);
                    startService(watchDogService);
                }

            }
        });


    }

    protected void showChangeBgDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.notification);
        builder.setTitle("选择来电归属地样式");

        int witch = sp.getInt("which", 0);
        builder.setSingleChoiceItems(items, witch, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("which", which);
                editor.commit();
                dialog.dismiss();
                tv_setting_bg.setText(items[which]);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.show();

    }

    @Override
    protected void onStart() {
        //保证用户所看见的数据是正确的
        if (ServiceStatusUtils.isServiceRunning(this, "com.bingoogol.mobilesafe.service.PhoneStatusService")) {
            sv_setting_showaddress.setChecked(true);
        } else {
            sv_setting_showaddress.setChecked(false);
        }
        if (ServiceStatusUtils.isServiceRunning(this, "com.bingoogol.mobilesafe.service.CallSmsSafeService")) {
            sv_setting_callsmssafe.setChecked(true);
        } else {
            sv_setting_callsmssafe.setChecked(false);
        }
        if (ServiceStatusUtils.isServiceRunning(this, "com.bingoogol.mobilesafe.service.WatchDogService")) {
            sv_setting_applock.setChecked(true);
        } else {
            sv_setting_applock.setChecked(false);
        }
        super.onStart();
    }

}
