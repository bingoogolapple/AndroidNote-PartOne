package cn.bingoogol.painter.model;

import java.io.Serializable;

import android.R.menu;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int COLOR = 0;
	public static final int XFERMODE = 1;
	public static final int START = 2;
	public static final int MOVE = 3;
	public static final int UP = 4;
	/**
	 * 该条命令的类型，整数或字符串
	 */
	public int type;
	public int color;
	public int xfermode;
	public float x;
	public float y;

	public Message() {
	}

	public static final Message getStartMsg(float x, float y) {
		Message msg = new Message();
		msg.type = START;
		msg.x = x;
		msg.y = y;
		return msg;
	}

	public static final Message getMoveMsg(float x, float y) {
		Message msg = new Message();
		msg.type = MOVE;
		msg.x = x;
		msg.y = y;
		return msg;
	}

	public static final Message getUpMsg() {
		Message msg = new Message();
		msg.type = UP;
		return msg;
	}

	public static final Message getXfermodeMsg(int xfermode) {
		Message msg = new Message();
		msg.type = XFERMODE;
		msg.xfermode = xfermode;
		return msg;
	}

	public static final Message getColorMsg(int color) {
		Message msg = new Message();
		msg.type = COLOR;
		msg.color = color;
		return msg;
	}
	
	@Override
	public String toString() {
		return "Message [type=" + type + ", color=" + color + ", xfermode=" + xfermode + ", x=" + x + ", y=" + y + "]";
	}
}