package com.bingoogol.mobilesafe.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.domain.UpgradeInfo;
import com.bingoogol.mobilesafe.engine.UpgradeInfoPaser;
import com.bingoogol.mobilesafe.service.PhoneStatusService;
import com.bingoogol.mobilesafe.util.*;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends FinalActivity {

    private static final String TAG = "SplashActivity";
    private static final int LOAD_MAIN_UI = 1;
    private static final int SHOW_UPGRADE_INFO = 2;
    private static final int XML_PARSE_ERROR = 3;
    private static final int SERVER_ERROR = 4;
    private static final int URL_ERROR = 5;
    private static final int NETWORK_ERROR = 6;
    private static final int NET_NOT_CONNECTED = 7;
    private static final int SDCARD_NOT_WRITABLE = 8;

    @ViewInject(id = R.id.tv_splash_version)
    private TextView tv_splash_version;
    private UpgradeInfo upgradeInfo;
    private String version;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean isLoadMainUI = true;
            switch (msg.what) {
                case LOAD_MAIN_UI:
                    break;
                case SHOW_UPGRADE_INFO:
                    showUpgradeDialog();
                    isLoadMainUI = false;
                    break;
                case XML_PARSE_ERROR:
                    ToastUtil.makeText(getApplicationContext(), "升级信息解析错误");
                    break;
                case SERVER_ERROR:
                    ToastUtil.makeText(getApplicationContext(), "服务器内部错误");
                    break;
                case URL_ERROR:
                    ToastUtil.makeText(getApplicationContext(), "url错误");
                    break;
                case NETWORK_ERROR:
                    ToastUtil.makeText(getApplicationContext(), "网络链接错误，请检查网络");
                    break;
                case NET_NOT_CONNECTED:
                    ToastUtil.makeText(getApplicationContext(), "未链接到网络");
                    break;
                case SDCARD_NOT_WRITABLE:
                    ToastUtil.makeText(getApplicationContext(), "SD卡不可写");
                    break;
            }
            if(isLoadMainUI) {
                loadMainUI();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        version = getCurrentVersion();
        tv_splash_version.setText(getResources().getText(R.string.version_number) + ":" + version);

        new Thread(new CheckVersionTask()).start();

        Animation animation = new AlphaAnimation(0.4f, 1.0f);
        animation.setDuration(1000);
        //避免创建不必要的对象
        this.findViewById(R.id.ll_splash_main).startAnimation(animation);
        copyDB("address.db");
        copyDB("commonnum.db");

        installShortCut();
    }

    private void installShortCut() {
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "safe快捷方式");
        // 桌面应用通过action和category来判断某快捷方式是否已存在
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.application_icon));
        Intent i = new Intent();
        // 如果不是入口activity，则必须用隐式意图
        i.setAction("com.bingoogol.mobilesafe.home");
        i.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        sendBroadcast(intent);
    }

    private void copyDB(final String dbname) {
        new Thread() {
            public void run() {
                try {

                    File file = new File(getFilesDir(), dbname);
                    if (file.exists() && file.length() > 0) {
                        Logger.i(TAG, "数据库已经存在无需拷贝");
                    } else {
                        InputStream is = getAssets().open(dbname);
                        // data/data/包名/files/address.db
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();
                        showToastInMainThread("拷贝数据库成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastInMainThread("拷贝数据库失败");
                }

            };
        }.start();
    }

    /**
     * 在主线程显示土司
     *
     * @param text
     */
    public void showToastInMainThread(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.makeText(getApplicationContext(), text);
            }
        });
    }

    /**
     * 检查更新子线程
     */
    private class CheckVersionTask implements Runnable {

        @Override
        public void run() {
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            boolean autoupdate = sp.getBoolean("autoupdate", false);
            if(!autoupdate){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadMainUI();
                return;
            }


            //默认加载主界面
            Message msg = handler.obtainMessage(LOAD_MAIN_UI);
            long startTime = System.currentTimeMillis();
            try {
                if (!ConnectivityUtil.isConnected(getApplicationContext())) {
                    msg.what = NET_NOT_CONNECTED;
                } else if(!StorageUtil.isExternalStorageWritable()) {
                    msg.what = SDCARD_NOT_WRITABLE;
                } else {
                    URL url = new URL(Constants.config.UPGRADE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        upgradeInfo = UpgradeInfoPaser.getUpgradeInfo(is);
                        if (upgradeInfo != null) {
                            if (!version.equals(upgradeInfo.version)) {
                                //版本号不同，提示用户更新
                                msg.what = SHOW_UPGRADE_INFO;
                            }
                        } else {
                            //解析xml失败
                            msg.what = XML_PARSE_ERROR;
                        }
                    } else {
                        // 服务器错误或者资源没找到
                        msg.what = SERVER_ERROR;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg.what = URL_ERROR;
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = NETWORK_ERROR;
            } finally {
                long endTime = System.currentTimeMillis();
                long dTime = endTime - startTime;
                if (dTime < 1000) {
                    try {
                        Thread.sleep(1000 - dTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 显示升级对话框
     */
    private void showUpgradeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //让用户不能取消对话框
        builder.setCancelable(false);
        builder.setIcon(R.drawable.application_icon);
        builder.setTitle("升级提醒");
        builder.setMessage(upgradeInfo.description);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.i(TAG, "下载apk文件");
                if(!ConnectivityUtil.isWifiConnected(getApplicationContext())) {
                    //wifi不可用，询问用户是否继续下载
                } else {
                    downloadApk();
                }
            }
        });
        builder.setNegativeButton("下次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "用户取消，进入程序主界面");
                loadMainUI();
            }
        });
        builder.create().show();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在下载.....");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        FinalHttp fh = new FinalHttp();
        fh.download(upgradeInfo.apkurl, new File(StorageUtil.getAppDir(), "mobilesafe.apk").getAbsolutePath(),
                new AjaxCallBack<File>() {
                    @Override
                    public void onStart() {
                        pd.show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        pd.dismiss();
                        install(file);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        pd.setMax((int) count);
                        pd.setProgress((int) current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        ToastUtil.makeText(getApplicationContext(), strMsg);
                        loadMainUI();
                    }
                });
    }

    /**
     * 加载主界面
     */
    private void loadMainUI() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取应用程序的版本号
     *
     * @return 应用程序版本号
     */
    private String getCurrentVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            //第二个参数，附加可选标识，一般用不到写0
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            //利用api getPackageName()得到的包名，这个异常根本不可能发生
            return "";
        }
    }

    /**
     * 安装应用
     *
     * @param file apk文件
     */
    private void install(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

}
