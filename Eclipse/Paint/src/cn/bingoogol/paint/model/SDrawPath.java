package cn.bingoogol.paint.model;

import java.io.Serializable;

public class SDrawPath implements Serializable {
	private static final long serialVersionUID = 1L;
	public SPath path;
	public SPaint paint;

	public SDrawPath() {
	}

	public SDrawPath(SPath path, SPaint paint) {
		this.path = path;
		this.paint = paint;
	}
}