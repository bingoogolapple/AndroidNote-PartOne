package com.bingoogol.mobilesafe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.util.ToastUtil;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-13.
 */
public class Setup3Activity extends BaseSetupActivity {

    @ViewInject(id = R.id.et_setup3_phone)
    private EditText et_setup3_phone;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_setup3);
        et_setup3_phone.setText(sp.getString("safenumber", ""));
    }

    @Override
    public void showNext() {
        String safenumber = et_setup3_phone.getText().toString().trim();
        if(TextUtils.isEmpty(safenumber)){
            ToastUtil.makeText(this,"安全号码不能为空");
            return;
        }

        String newSafenumber = safenumber.replace("-", "");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenumber", newSafenumber);
        editor.commit();
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
    }

    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String phone = data.getStringExtra("phone");
            et_setup3_phone.setText(phone);
        }
    }
}