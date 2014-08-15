package cn.bingoogol.painter.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SPath implements Serializable {
	private static final long serialVersionUID = 1L;

	public int color;
	public float width;
	public ArrayList<Point> points;

	public SPath() {
	}

	public SPath(int color, float width) {
		this.color = color;
		this.width = width;
		points = new ArrayList<Point>();
	}

	public void drawPath(Canvas canvas, Paint paint) {
		paint.setColor(color);
		paint.setStrokeWidth(width);
		int size = points.size();
		if (size > 0) {
			canvas.drawLine(points.get(0).x, points.get(0).y, points.get(0).x, points.get(0).y, paint);
			for (int i = 1; i < size; i++) {
				canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, paint);
			}
		}
	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		paint.setStrokeWidth(width);
		int size = points.size();
		if (size == 1) {
			canvas.drawLine(points.get(0).x, points.get(0).y, points.get(0).x, points.get(0).y, paint);
		} else if (size > 1) {
			canvas.drawLine(points.get(size - 2).x, points.get(size - 2).y, points.get(size - 1).x, points.get(size - 1).y, paint);
		}
	}

	public void moveTo(float x, float y) {
		points.add(new Point(x, y));
	}
}