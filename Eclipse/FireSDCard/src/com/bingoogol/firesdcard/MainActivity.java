package com.bingoogol.firesdcard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		sendBroadcast(intent);
		finish();
	}


}
