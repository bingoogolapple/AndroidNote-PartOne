package com.bingoogol.mobilesafe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bingoogol.mobilesafe.R;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-13.
 */
public class Setup2Activity extends BaseSetupActivity {

    @ViewInject(id = R.id.iv_setup2_status)
    private ImageView iv_setup2_status;
    @Override
    public void initLayout() {
        setContentView(R.layout.activity_setup2);
        if(isBindSim()) {
            iv_setup2_status.setImageResource(R.drawable.lock);
        } else {
            iv_setup2_status.setImageResource(R.drawable.unlock);
        }
    }

    @Override
    public void showNext() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }

    public void bindSim(View view) {
        SharedPreferences.Editor editor = sp.edit();
        if(isBindSim()) {
            editor.putString("simSeriaNumber",null);
            iv_setup2_status.setImageResource(R.drawable.unlock);
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String simSeriaNumber = tm.getSimSerialNumber();
            editor.putString("simSeriaNumber", simSeriaNumber);
            iv_setup2_status.setImageResource(R.drawable.lock);
        }
        editor.commit();
    }

    private boolean isBindSim() {
        return !TextUtils.isEmpty(sp.getString("simSeriaNumber",null));
    }
}