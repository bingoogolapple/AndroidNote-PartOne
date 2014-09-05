package cn.bingoogol.dmc.model;

import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Device;

import cn.bingoogol.util.Logger;

public class MediaRenderDevices {
	private static final String TAG = MediaRenderDevices.class.getSimpleName();
	private static final MediaRenderDevices mDLNAContainer = new MediaRenderDevices();
	private static final String MEDIARENDER = "urn:schemas-upnp-org:device:MediaRenderer:1";
	private List<Device> mDevices;
	private Device mSelectedDevice;
	private DeviceChangeListener mDeviceChangeListener;

	private MediaRenderDevices() {
		mDevices = new ArrayList<Device>();
	}

	public static MediaRenderDevices getInstance() {
		return mDLNAContainer;
	}

	public static boolean isMediaRenderDevice(Device device) {
		return device != null && MEDIARENDER.equalsIgnoreCase(device.getDeviceType()) ? true : false;
	}

	public synchronized void addDevice(Device device) {
		if (isMediaRenderDevice(device)) {
			int size = mDevices.size();
			for (int i = 0; i < size; i++) {
				String udnString = mDevices.get(i).getUDN();
				if (device.getUDN().equalsIgnoreCase(udnString)) {
					// 如果已经存在，则直接返回
					return;
				}
			}
			mDevices.add(device);
			Logger.i(TAG, "         addRender--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
			if (mDeviceChangeListener != null) {
				mDeviceChangeListener.onDeviceChange();
			}
		} else {
			Logger.i(TAG, "      addNotRender--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
		}
	}

	public synchronized void removeDevice(Device device) {
		if (isMediaRenderDevice(device)) {
			int size = mDevices.size();
			for (int i = 0; i < size; i++) {
				String udnString = mDevices.get(i).getUDN();
				if (device.getUDN().equalsIgnoreCase(udnString)) {
					mDevices.remove(i);
					Logger.i(TAG, "      removeRender--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
					if (mSelectedDevice != null && mSelectedDevice.getUDN().equalsIgnoreCase(device.getUDN())) {
						mSelectedDevice = null;
					}
					if (mDeviceChangeListener != null) {
						mDeviceChangeListener.onDeviceChange();
					}
					// 已经删除，直接返回
					return;
				}
			}
		} else {
			Logger.i(TAG, "   removeNotRender--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
		}
	}

	public synchronized void clear() {
		if (mDevices != null) {
			mDevices.clear();
			mSelectedDevice = null;
		}
	}

	public Device getSelectedDevice() {
		return mSelectedDevice;
	}

	public void setSelectedDevice(Device mSelectedDevice) {
		this.mSelectedDevice = mSelectedDevice;
	}

	public void setDeviceChangeListener(DeviceChangeListener deviceChangeListener) {
		mDeviceChangeListener = deviceChangeListener;
	}

	public List<Device> getDevices() {
		return mDevices;
	}

	public interface DeviceChangeListener {
		public void onDeviceChange();
	}

}