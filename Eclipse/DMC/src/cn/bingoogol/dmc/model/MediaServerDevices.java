package cn.bingoogol.dmc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.cybergarage.upnp.Device;

import cn.bingoogol.util.DlnaUtils;
import cn.bingoogol.util.Logger;

public class MediaServerDevices {
	private static final String TAG = MediaServerDevices.class.getSimpleName();
	private static final MediaServerDevices mInstance = new MediaServerDevices();
	public List<Device> mDevices = new ArrayList<Device>();
	private Device mSelectedDevice;

	private MediaServerDevices() {
	}

	public static MediaServerDevices getInstance() {
		return mInstance;
	}

	public Device[] getDevices() {
		if (mDevices.size() == 0) {
			return new Device[0];
		} else {
			return mDevices.toArray(new Device[] {});
		}
	}

	public void setDevices(Device[] devices) {
		mDevices = Arrays.asList(devices);
	}

	public void setDevices(Iterator<Device> iterator) {
		ArrayList<Device> devices = new ArrayList<Device>();
		while (iterator.hasNext()) {
			Device device = iterator.next();
			devices.add(device);
		}
		mDevices = devices;
	}

	public Device getSelectedDevice() {
		return mSelectedDevice;
	}

	public void setSelectedDevice(Device selectedDevice) {
		mSelectedDevice = selectedDevice;
	}

	public CharSequence[] getDeviceNames() {
		CharSequence[] deviceNames = new CharSequence[mDevices.size()];
		for (int i = 0; i < mDevices.size(); i++) {
			CharSequence name = mDevices.get(i).getFriendlyName();
			deviceNames[i] = name;
		}
		return deviceNames;
	}

	public void addDevice(Device device) {
		if (device.getFriendlyName().equals(DlnaUtils.creat12BitUUID())) {
			mSelectedDevice = device;
			mDevices.add(device);
			Logger.i(TAG, "         addServer--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
		} else {
			Logger.i(TAG, "      addNotServer--" + "deviceType:" + device.getDeviceType() + "   UDN:" + device.getUDN() + "  modelName:" + device.getModelName() + "   friendlyName:" + device.getFriendlyName());
		}
	}

}
