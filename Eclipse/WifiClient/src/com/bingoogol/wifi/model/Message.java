package com.bingoogol.wifi.model;

public class Message {
	
	public static final int INTVALUE = 12;
	public static final int STRINGVALUE = 13;
	
	private int intValue;
	private String stringValue;
	private int messageType;
	
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
