package com.bingoogol.socketclient.ui;

import android.app.Activity;
import android.os.Bundle;
import com.bingoogol.socketclient.R;

public class DownloadActivity extends Activity {
	private static final String TAG = "DownloadActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
	}
}