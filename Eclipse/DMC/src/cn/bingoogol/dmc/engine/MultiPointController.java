package cn.bingoogol.dmc.engine;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import android.text.TextUtils;

public class MultiPointController {
	private static final String AVTransport1 = "urn:schemas-upnp-org:service:AVTransport:1";
	private static final String SetAVTransportURI = "SetAVTransportURI";
	private static final String RenderingControl = "urn:schemas-upnp-org:service:RenderingControl:1";
	private static final String Play = "Play";

	public static String metaData = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" xmlns:sec=\"http://www.sec.co.kr/\"><item id=\"filePath\" parentID=\"0\" restricted=\"1\"><upnp:class>object.item.videoItem</upnp:class><dc:title>IMAG1466</dc:title><dc:creator>Unknown Artist</dc:creator><upnp:artist>Unknown Artist</upnp:artist><upnp:albumArtURI>http://IP:PORT/filePath</upnp:albumArtURI><upnp:album>Unknown Album</upnp:album><res protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\">http://IP:PORT/filePath</res></item></DIDL-Lite>";

	// public static String metaData="<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" xmlns:sec=\"http://www.sec.co.kr/\"><item id=\"filePath\" parentID=\"0\" restricted=\"1\"><upnp:class>object.item.imageItem.photo</upnp:class><dc:title>IMAG1466</dc:title><dc:creator>Unknown Artist</dc:creator><upnp:artist>Unknown Artist</upnp:artist><upnp:albumArtURI>http://IP:PORT/filePath</upnp:albumArtURI><upnp:album>Unknown Album</upnp:album><res protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\">http://IP:PORT/filePath</res></item></DIDL-Lite>";
	// public static String metaData = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" xmlns:sec=\"http://www.sec.co.kr/\"><item id=\"filePath\" parentID=\"0\" restricted=\"1\"><upnp:class>object.item.audioItem.musicTrack</upnp:class><dc:title>IMAG1466</dc:title><dc:creator>Unknown Artist</dc:creator><upnp:artist>Unknown Artist</upnp:artist><upnp:albumArtURI>http://IP:PORT/filePath</upnp:albumArtURI><upnp:album>Unknown Album</upnp:album><res protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\">http://IP:PORT/filePath</res></item></DIDL-Lite>";

	public boolean play(Device device, String path) {
		Service service = device.getService(AVTransport1);

		if (service == null) {
			return false;
		}

		final Action action = service.getAction(SetAVTransportURI);
		if (action == null) {
			return false;
		}

		final Action playAction = service.getAction(Play);
		if (playAction == null) {
			return false;
		}

		if (TextUtils.isEmpty(path)) {
			return false;
		}

		action.setArgumentValue("InstanceID", 0);
		action.setArgumentValue("CurrentURI", path);
		action.setArgumentValue("CurrentURIMetaData", metaData);
		if (!action.postControlAction()) {
			return false;
		}

		playAction.setArgumentValue("InstanceID", 0);
		playAction.setArgumentValue("Speed", "1");
		return playAction.postControlAction();
	}

	public boolean goon(Device device, String pausePosition) {

		Service localService = device.getService(AVTransport1);
		if (localService == null)
			return false;
		final Action localAction = localService.getAction("Seek");
		if (localAction == null)
			return false;
		localAction.setArgumentValue("InstanceID", "0");
		localAction.setArgumentValue("Unit", "ABS_TIME");
		localAction.setArgumentValue("Target", pausePosition);
		boolean postControlAction = localAction.postControlAction();
		if (!postControlAction) {
			localAction.setArgumentValue("Unit", "REL_TIME");
			localAction.setArgumentValue("Target", pausePosition);
			localAction.postControlAction();
		}

		Action playAction = localService.getAction("Play");
		if (playAction == null) {
			return false;
		}
		playAction.setArgumentValue("InstanceID", 0);
		playAction.setArgumentValue("Speed", "1");
		return playAction.postControlAction();
	}

	public String getTransportState(Device device) {
		Service localService = device.getService(AVTransport1);
		if (localService == null) {
			return null;
		}
		final Action localAction = localService.getAction("GetTransportInfo");
		if (localAction == null) {
			return null;
		}
		localAction.setArgumentValue("InstanceID", "0");
		if (localAction.postControlAction()) {
			return localAction.getArgumentValue("CurrentTransportState");
		} else {
			return null;
		}
	}

	public String getVolumeDbRange(Device device, String argument) {
		Service localService = device.getService(RenderingControl);
		if (localService == null) {
			return null;
		}
		Action localAction = localService.getAction("GetVolumeDBRange");
		if (localAction == null) {
			return null;
		}
		localAction.setArgumentValue("InstanceID", "0");
		localAction.setArgumentValue("Channel", "Master");
		if (!localAction.postControlAction()) {
			return null;
		} else {
			return localAction.getArgumentValue(argument);
		}
	}

	public int getMinVolumeValue(Device device) {
		String minValue = getVolumeDbRange(device, "MinValue");
		if (TextUtils.isEmpty(minValue)) {
			return 0;
		}
		return Integer.parseInt(minValue);
	}

	public int getMaxVolumeValue(Device device) {
		String maxValue = getVolumeDbRange(device, "MaxValue");
		if (TextUtils.isEmpty(maxValue)) {
			return 100;
		}
		return Integer.parseInt(maxValue);
	}

	public boolean seek(Device device, String targetPosition) {
		Service localService = device.getService(AVTransport1);
		if (localService == null)
			return false;
		Action localAction = localService.getAction("Seek");
		if (localAction == null) {
			return false;
		}
		localAction.setArgumentValue("InstanceID", "0");
		localAction.setArgumentValue("Unit", "ABS_TIME");
		localAction.setArgumentValue("Target", targetPosition);
		boolean postControlAction = localAction.postControlAction();
		if (!postControlAction) {
			localAction.setArgumentValue("Unit", "REL_TIME");
			localAction.setArgumentValue("Target", targetPosition);
			return localAction.postControlAction();
		} else {
			return postControlAction;
		}
	}

	public String getPositionInfo(Device device) {
		Service localService = device.getService(AVTransport1);
		if (localService == null)
			return null;
		final Action localAction = localService.getAction("GetPositionInfo");
		if (localAction == null) {
			return null;
		}
		localAction.setArgumentValue("InstanceID", "0");
		boolean isSuccess = localAction.postControlAction();
		if (isSuccess) {
			return localAction.getArgumentValue("RelTime");
		} else {
			return null;
		}
	}

	/**
	 * 获取媒体播放时长
	 * 
	 * @param device
	 * @return
	 */
	public String getMediaDuration(Device device) {
		Service localService = device.getService(AVTransport1);
		if (localService == null) {
			return null;
		}
		final Action localAction = localService.getAction("GetMediaInfo");
		if (localAction == null) {
			return null;
		}
		localAction.setArgumentValue("InstanceID", "0");
		if (localAction.postControlAction()) {
			return localAction.getArgumentValue("MediaDuration");
		} else {
			return null;
		}
	}

	public boolean setMute(Device mediaRenderDevice, String targetValue) {
		Service service = mediaRenderDevice.getService(RenderingControl);
		if (service == null) {
			return false;
		}
		final Action action = service.getAction("SetMute");
		if (action == null) {
			return false;
		}
		action.setArgumentValue("InstanceID", "0");
		action.setArgumentValue("Channel", "Master");
		action.setArgumentValue("DesiredMute", targetValue);
		return action.postControlAction();
	}

	public String getMute(Device device) {
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return null;
		}
		final Action getAction = service.getAction("GetMute");
		if (getAction == null) {
			return null;
		}
		getAction.setArgumentValue("InstanceID", "0");
		getAction.setArgumentValue("Channel", "Master");
		getAction.postControlAction();
		return getAction.getArgumentValue("CurrentMute");
	}

	public boolean setVoice(Device device, int value) {
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return false;
		}
		final Action action = service.getAction("SetVolume");
		if (action == null) {
			return false;
		}
		action.setArgumentValue("InstanceID", "0");
		action.setArgumentValue("Channel", "Master");
		action.setArgumentValue("DesiredVolume", value);
		return action.postControlAction();
	}

	public int getVoice(Device device) {
		Service service = device.getService(RenderingControl);
		if (service == null) {
			return -1;
		}
		final Action getAction = service.getAction("GetVolume");
		if (getAction == null) {
			return -1;
		}
		getAction.setArgumentValue("InstanceID", "0");
		getAction.setArgumentValue("Channel", "Master");
		if (getAction.postControlAction()) {
			return getAction.getArgumentIntegerValue("CurrentVolume");
		} else {
			return -1;
		}
	}

	public boolean stop(Device device) {
		Service service = device.getService(AVTransport1);
		if (service == null) {
			return false;
		}
		final Action stopAction = service.getAction("Stop");
		if (stopAction == null) {
			return false;
		}
		stopAction.setArgumentValue("InstanceID", 0);
		return stopAction.postControlAction();
	}

	public boolean pause(Device mediaRenderDevice) {
		Service service = mediaRenderDevice.getService(AVTransport1);
		if (service == null) {
			return false;
		}
		final Action pauseAction = service.getAction("Pause");
		if (pauseAction == null) {
			return false;
		}
		pauseAction.setArgumentValue("InstanceID", 0);
		return pauseAction.postControlAction();
	}

}