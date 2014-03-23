package com.bingoogol.mobilesafe.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.receiver.OutCallReceiver;
import com.bingoogol.mobilesafe.util.Logger;
import com.bingoogol.mobilesafe.util.Md5Util;
import com.bingoogol.mobilesafe.util.ToastUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-1-24.
 */
public class HomeActivity extends FinalActivity {
    private static final String TAG = "HomeActivity";
    private static final String names[] = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "系统优化", "高级工具", "设置中心"};
    private static final int icons[] = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
            R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};

    private static ImageView iv_gv_home_icon;
    private static TextView tv_gv_home_name;

    private SharedPreferences sp;

    @ViewInject(id = R.id.gv_home)
    private GridView gv_home;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //检查用户是否设置过密码
                        if(isSetupPwd()) {
                            showEnterPwdDialog();
                        } else {
                            showSetupPwDialog();
                        }
                        break;
                    case 7:
                        // 高级工具
                        Intent atoosIntent = new Intent(HomeActivity.this,AtoolsActivity.class);
                        startActivity(atoosIntent);
                        break;
                    case 8:
                        // 设置中心
                        Intent settingIntent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(settingIntent);
                }
            }
        });
    }

    /**
     * 显示输入密码对话框
     */
    private void showEnterPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog_enter_pwd,null);
        final AlertDialog dialog = builder.create();
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        //去掉上下左右的间隙
        dialog.setView(view,0,0,0,0);
        dialog.show();
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString();
                if(TextUtils.isEmpty(pwd)) {
                    ToastUtil.makeText(getApplicationContext(),"密码不能为空");
                } else {
                    String savePwd = sp.getString("password","");
                    if(Md5Util.encode(pwd).equals(savePwd)) {
                        Logger.i(TAG,"进入手机防盗界面");
                        dialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtil.makeText(getApplicationContext(),"密码输入错误");
                    }
                }
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private boolean isSetupPwd() {
        String password = sp.getString("password","");
        if(TextUtils.isEmpty(password)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 显示设置密码对话框
     */
    private void showSetupPwDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog_setup_pwd,null);
        final AlertDialog dialog = builder.create();
        //去掉上下左右的间隙
        dialog.setView(view,0,0,0,0);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        final EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        dialog.show();

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString().trim();
                String confirm_pwd = et_confirm_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirm_pwd)) {
                    ToastUtil.makeText(getApplicationContext(),"密码不能为空");
                } else {
                    if(pwd.equals(confirm_pwd)) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", Md5Util.encode(pwd));
                        editor.commit();
                        dialog.dismiss();
                    } else {
                        ToastUtil.makeText(getApplicationContext(),"两次密码不一致");
                    }
                }
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 会调用多次，使用静态的变量引用，减少内存中申请的引用的个数
            View view = View.inflate(getApplicationContext(),R.layout.item_gv_home, null);
            iv_gv_home_icon = (ImageView) view.findViewById(R.id.iv_gv_home_icon);
            tv_gv_home_name = (TextView) view.findViewById(R.id.tv_gv_home_name);
            iv_gv_home_icon.setImageResource(icons[position]);
            tv_gv_home_name.setText(names[position]);

            if(position==0){
                String newname = sp.getString("newname", "");
                if(!TextUtils.isEmpty(newname)){
                    tv_gv_home_name.setText(newname);
                }
            }
            return view;
        }
    }
}