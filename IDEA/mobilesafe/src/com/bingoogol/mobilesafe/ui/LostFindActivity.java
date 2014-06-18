package com.bingoogol.mobilesafe.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.util.ToastUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-12.
 */
public class LostFindActivity extends FinalActivity {
    private static final String TAG = "LostFindActivity";
    private SharedPreferences sp;

    @ViewInject(id = R.id.tv_lostfind_number)
    private TextView tv_lostfind_number;
    @ViewInject(id = R.id.iv_lostfind_status)
    private ImageView iv_lostfind_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        if(isConfiged()) {
            setContentView(R.layout.activity_lost_find);
            tv_lostfind_number.setText(sp.getString("safenumber",""));
            if(sp.getBoolean("protecting",false)) {
                iv_lostfind_status.setImageResource(R.drawable.lock);
            } else {
                iv_lostfind_status.setImageResource(R.drawable.unlock);
            }
        } else {
            loadSetup1();
        }
    }

    private void loadSetup1() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }

    private boolean isConfiged() {
        return sp.getBoolean("isconfig",false);
    }

    public void reEntrySetup(View view) {
        loadSetup1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.lost_find,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_change_name) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置新的名称");
            final EditText et = new EditText(this);
            et.setHint("请输入新的名称");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newname = et.getText().toString().trim();
                    if (TextUtils.isEmpty(newname)) {
                        ToastUtil.makeText(getApplicationContext(), "名称不能为空");
                    } else {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("newname", newname);
                        editor.commit();
                    }
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
