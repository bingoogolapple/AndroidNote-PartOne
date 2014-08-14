package cn.bingoogol.painter.model;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	public float x;
	public float y;

	public Point() {
	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}