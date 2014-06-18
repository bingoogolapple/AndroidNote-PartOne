package com.bingo.bean;

import java.io.Serializable;

public class Mp3Info implements Serializable {
	private static final long serialVersionUID = 83580349431981729L;
	private String id;
	private String mp3Name;
	private String mp3Size;
	public Mp3Info() {
		super();
	}
	public Mp3Info(String id, String mp3Name, String mp3Size) {
		this.id = id;
		this.mp3Name = mp3Name;
		this.mp3Size = mp3Size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMp3Name() {
		return mp3Name;
	}
	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}
	public String getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
}
