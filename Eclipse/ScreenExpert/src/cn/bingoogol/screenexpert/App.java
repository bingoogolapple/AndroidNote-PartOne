package cn.bingoogol.screenexpert;

import cn.bingoogol.screenexpert.service.ScreenService;
import cn.bingoogol.screenexpert.util.SpUtil;
import android.app.Application;
import android.content.Intent;

public class App extends Application {
	private static App mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SpUtil.init(this);
		startService(new Intent(this, ScreenService.class));
	}
	
	public static App getInstance() {
		return mInstance;
	}
}
