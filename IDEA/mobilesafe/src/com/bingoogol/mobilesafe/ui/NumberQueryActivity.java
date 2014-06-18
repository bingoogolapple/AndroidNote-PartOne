package com.bingoogol.mobilesafe.ui;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.db.dao.AddressDao;
import com.bingoogol.mobilesafe.util.ToastUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class NumberQueryActivity extends FinalActivity {
    @ViewInject(id = R.id.et_number)
    private EditText et_number;
    @ViewInject(id = R.id.tv_number_address)
    private TextView tv_number_address;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_query);

        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        et_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(s.toString());
                tv_number_address.setText("归属地:"+address);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void query(View view){
        String number = et_number.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            ToastUtil.makeText(this, "电话号码不能为空");
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_number.startAnimation(shake);
            //view.startAnimation(shake);
            vibrator.vibrate(300);
            return;
        }else{
            String address = AddressDao.getAddress(number);
            tv_number_address.setText("归属地:"+address);
        }

    }

}
