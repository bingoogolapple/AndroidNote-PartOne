package cn.bingoogol.dmc.engine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.UPnPStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.bingoogol.dmc.model.DlnaItem;
import cn.bingoogol.dmc.model.DlnaItemChild;
import cn.bingoogol.dmc.model.MediaServerDevices;
import cn.bingoogol.util.Logger;

public class ItemHelper {
	private static final String TAG = ItemHelper.class.getSimpleName();
	public static final String NEW_DEVICES_FOUND = "dlna.player.NEW_DEVICES_FOUND";

	public static List<DlnaItem> getItems(int id) {
		Device device = MediaServerDevices.getInstance().getSelectedDevice();
		if (device == null) {
			return null;
		}
		Logger.i(TAG, "device=" + device + ",device.getLocation()=" + device.getLocation() + ",device.getDeviceType()=" + device.getDeviceType());
		org.cybergarage.upnp.Service service = device.getService("urn:schemas-upnp-org:service:ContentDirectory:1");
		if (service == null) {
			Logger.i(TAG, "no service for ContentDirectory!!!");
			return null;
		}
		Action action = service.getAction("Browse");
		if (action == null) {
			Logger.i(TAG, "action for Browse is null!!!");
			return null;
		}
		ArgumentList argumentList = action.getArgumentList();
		argumentList.getArgument("ObjectID").setValue(id);
		argumentList.getArgument("BrowseFlag").setValue("BrowseDirectChildren");
		argumentList.getArgument("StartingIndex").setValue("0");
		argumentList.getArgument("RequestedCount").setValue("0");
		argumentList.getArgument("Filter").setValue("*");
		argumentList.getArgument("SortCriteria").setValue("");
		if (action.postControlAction()) {
			ArgumentList outArgList = action.getOutputArgumentList();
			Argument result = outArgList.getArgument("Result");
			Logger.i(TAG, "Result:" + result.getValue());
			List<DlnaItem> items = parseItem(result);
			return items;
		} else {
			UPnPStatus err = action.getControlStatus();
			Logger.e(TAG, "Error Code = " + err.getCode());
			Logger.e(TAG, "Error Desc = " + err.getDescription());
		}
		return null;
	}

	public static List<DlnaItemChild> getItemChild(String id) {
		if (MediaServerDevices.getInstance().getSelectedDevice() == null) {
			return null;
		}
		org.cybergarage.upnp.Service service = MediaServerDevices.getInstance().getSelectedDevice().getService("urn:schemas-upnp-org:service:ContentDirectory:1");
		Action action = service.getAction("Browse");
		ArgumentList argumentList = action.getArgumentList();
		argumentList.getArgument("ObjectID").setValue(id);
		argumentList.getArgument("BrowseFlag").setValue("BrowseDirectChildren");
		argumentList.getArgument("StartingIndex").setValue("0");
		argumentList.getArgument("RequestedCount").setValue("0");
		argumentList.getArgument("Filter").setValue("*");
		argumentList.getArgument("SortCriteria").setValue("");
		if (action.postControlAction()) {
			ArgumentList outArgList = action.getOutputArgumentList();
			Argument result = outArgList.getArgument("Result");
			Logger.i(TAG, "getItemChild:result.getValue()=" + result.getValue());
			List<DlnaItemChild> items = parseItemChild(result);
			return items;
		} else {
			UPnPStatus err = action.getControlStatus();
			Logger.e(TAG, "Error Code = " + err.getCode());
			Logger.e(TAG, "Error Desc = " + err.getDescription());
		}
		return null;
	}

	public static List<DlnaItem> parseItem(Argument result) {
		List<DlnaItem> list = new ArrayList<DlnaItem>();
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = dfactory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(result.getValue().getBytes("UTF-8"));
			Document doc = documentBuilder.parse(is);
			NodeList containers = doc.getElementsByTagName("container");
			for (int j = 0; j < containers.getLength(); ++j) {
				DlnaItem item = new DlnaItem();
				Node container = containers.item(j);
				NodeList childNodes = container.getChildNodes();
				for (int l = 0; l < childNodes.getLength(); ++l) {
					Node childNode = childNodes.item(l);
					if (childNode.getNodeName().equals("dc:title")) {
						item.title = childNode.getFirstChild().getNodeValue();
						item.id = container.getAttributes().getNamedItem("id").getNodeValue();
					}
				}

				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<DlnaItemChild> parseItemChild(Argument result) {
		List<DlnaItemChild> list = new ArrayList<DlnaItemChild>();
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = dfactory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(result.getValue().getBytes("UTF-8"));
			Document doc = documentBuilder.parse(is);
			NodeList items = doc.getElementsByTagName("item");
			for (int j = 0; j < items.getLength(); ++j) {
				DlnaItemChild i = new DlnaItemChild();
				Node item = items.item(j);
				i.id = item.getAttributes().getNamedItem("id").getNodeValue();
				i.parentID = item.getAttributes().getNamedItem("parentID").getNodeValue();
				NodeList childNodes = item.getChildNodes();
				for (int l = 0; l < childNodes.getLength(); ++l) {
					Node childNode = childNodes.item(l);
					if (childNode.getNodeName().equals("dc:title")) {
						i.title = childNode.getFirstChild().getNodeValue();
					} else if (childNode.getNodeName().equals("upnp:class")) {
						i.objectClass = childNode.getFirstChild().getNodeValue();
					} else if (childNode.getNodeName().equals("res")) {
						i.size = childNode.getAttributes().getNamedItem("size").getNodeValue();
						i.protocolInfo = childNode.getAttributes().getNamedItem("protocolInfo").getNodeValue();
						i.res = childNode.getFirstChild().getNodeValue();
					}
				}
				list.add(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}