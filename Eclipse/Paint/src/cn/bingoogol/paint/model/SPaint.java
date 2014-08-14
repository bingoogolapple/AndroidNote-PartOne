package cn.bingoogol.paint.model;

import java.io.Serializable;

import android.graphics.Paint;

public class SPaint extends Paint implements Serializable {
	private static final long serialVersionUID = 1L;
	public SPaint() {
	}

	public SPaint(int ditherFlag) {
		super(ditherFlag);
	}

}