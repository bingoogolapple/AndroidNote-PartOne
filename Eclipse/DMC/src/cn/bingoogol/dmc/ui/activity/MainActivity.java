package cn.bingoogol.dmc.ui.activity;

import java.util.List;

import org.cybergarage.upnp.Device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.bingoogol.dmc.App;
import cn.bingoogol.dmc.R;
import cn.bingoogol.dmc.model.MediaRenderDevices;
import cn.bingoogol.dmc.model.MediaRenderDevices.DeviceChangeListener;
import cn.bingoogol.dmc.service.DMCService;
import cn.bingoogol.dmc.service.DMSService;
import cn.bingoogol.util.Logger;

public class MainActivity extends BaseActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private List<Device> mDevices = MediaRenderDevices.getInstance().getDevices();
	private ListView mDeviceLv;
	private DeviceAdapter mDeviceAdapter;

	@Override
	protected void initView(Bundle savedInstanceState) {
		startService(new Intent(this, DMSService.class));
		startService(new Intent(this, DMCService.class));
		setContentView(R.layout.activity_main);
		mDeviceLv = (ListView) findViewById(R.id.lv_main_device);
		mDeviceAdapter = new DeviceAdapter();
		mDeviceLv.setAdapter(mDeviceAdapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, DMSService.class));
		stopService(new Intent(this, DMCService.class));
	}

	@Override
	protected void setListener() {
		mDeviceLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Logger.i(TAG, "click device -- " + mDevices.get(position).getFriendlyName());
				MediaRenderDevices.getInstance().setSelectedDevice(mDevices.get(position));
				startActivity(new Intent(mApp, FileListActivity.class));

			}
		});
		MediaRenderDevices.getInstance().setDeviceChangeListener(new DeviceChangeListener() {

			@Override
			public void onDeviceChange() {
				refreshDeviceList();
			}

		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
	}

	private void refreshDeviceList() {
		runOnUiThread(new Runnable() {
			public void run() {
				mDeviceAdapter.notifyDataSetChanged();
			}
		});
	}

	private class DeviceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDevices.size();
		}

		@Override
		public Object getItem(int position) {
			return mDevices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(App.getInstance(), R.layout.item_main_device, null);
				viewHolder.deviceNameTv = (TextView) convertView.findViewById(R.id.tv_item_main_device_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Device device = mDevices.get(position);
			// 可以根据location中的ip匹配大屏
			Logger.i(TAG, "friendlyName:" + device.getFriendlyName() + "   location:" + device.getLocation());

			viewHolder.deviceNameTv.setText(device.getFriendlyName());
			return convertView;
		}
	}

	private static class ViewHolder {
		public TextView deviceNameTv;
	}

}