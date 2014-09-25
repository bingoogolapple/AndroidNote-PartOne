/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package cn.bingoogol.volley;

import android.app.Application;
import cn.bingoogol.volley.data.RequestManager;

public class VolleyApp extends Application {
	@Override
    public void onCreate() {
        super.onCreate();
        init();
    }


    private void init() {
        RequestManager.init(this);
    }
}
