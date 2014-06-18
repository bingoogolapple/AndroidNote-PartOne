package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.engine.SmsUtils;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class AtoolsActivity extends Activity {
    private ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
    }


    public void addressQuery(View view){
        Intent intent = new Intent(this,NumberQueryActivity.class);
        startActivity(intent);
    }

    public void commonNumberQuery(View view){
        Intent intent = new Intent(this,CommonNumActivity.class);
        startActivity(intent);
    }

    /**
     * 备份短信
     */
    public void backupSms(View view){

//		final ProgressDialog pd = new ProgressDialog(this);
//		pd.setMessage("正在备份短信");
//		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		pd.show();
        new Thread(){
            public void run() {
                SmsUtils.backUpSms(AtoolsActivity.this, new SmsUtils.BackUpsmsListener() {
                    @Override
                    public void onSmsBackup(int process) {
                        //pd.setProgress(process);
                        progressBar1.setProgress(process);
                    }

                    @Override
                    public void beforeSmsBackup(int max) {
                        //pd.setMax(max);
                        progressBar1.setMax(max);
                    }
                });
                //	pd.dismiss();
            };
        }.start();

    }

    /**
     * 显示程序锁的ui
     * @param view
     */
    public void showAppLockUI(View view){
        Intent intent = new Intent(this,ApplockActivity.class);
        startActivity(intent);

    }
}
