package com.zq.dianzheng.model;

public class Memo {
	private int id;
	private String content;
	private long time = 0;

	public Memo() {
	}

	public Memo(String content) {
		this.content = content;
	}

	public Memo(String content, long time) {
		this.content = content;
		this.time = time;
	}

	public Memo(int id, String content, long time) {
		this.id = id;
		this.content = content;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Memo [id=" + id + ", content=" + content + ", time=" + time + "]";
	}
}
