package com.bingoogol.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class GPSService extends Service {
    private LocationManager lm;
    private MyLocationListener listener;
    private SharedPreferences sp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        // 查询条件
        Criteria criteria = new Criteria();
        // 精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 关心海拔
        criteria.setAltitudeRequired(true);
        // 运行产生开销
        criteria.setCostAllowed(true);
        // 允许最大电量请求
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = lm.getBestProvider(criteria, true);
        System.out.println("最好的提供者:" + provider);
        listener = new MyLocationListener();
        lm.requestLocationUpdates(provider, 0, 0, listener);
        super.onCreate();
    }

    private class MyLocationListener implements LocationListener {

        // 当位置变化时调用的方法
        @Override
        public void onLocationChanged(Location location) {
            String longitude = "jingdu:" + location.getLongitude();
            String latitude = "weidu:" + location.getLatitude();
            String accuacy = "jingquedu:" + location.getAccuracy();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last_location", latitude + longitude + accuacy);
            editor.commit();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        lm.removeUpdates(listener);
        listener = null;
        super.onDestroy();
    }
}
