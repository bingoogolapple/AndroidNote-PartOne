package com.bingoogol.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Toggle extends View implements OnTouchListener {
	//开关处于开启时的背景位图
	private Bitmap onBgBitmap;
	//开关处于关闭时的背景位图
	private Bitmap offBgBitmap;
	//滑块位图
	private Bitmap slipBitmap;
	//开关当前状态
	private boolean currentToggleState;
	//开关上一个状态
	private boolean previousToggleState;
	//开关是否可用
	private boolean isAvailable;
	//开关是否处于滑动状态
	private boolean isSlipping;
	//当前手指触碰的x坐标值
	private float currentX;
	//开关状态变化监听器
	private OnToggleStateChangeListener onToggleStateChangeListener;

	public void setCurrentToggleState(boolean currentToggleState) {
		this.currentToggleState = currentToggleState;
	}

	public Toggle(Context context) {
		super(context);
		setOnTouchListener(this);
	}

	public Toggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	public void setImages(int onBg, int offBg, int slip) {
		onBgBitmap = BitmapFactory.decodeResource(getResources(), onBg);
		offBgBitmap = BitmapFactory.decodeResource(getResources(), offBg);
		slipBitmap = BitmapFactory.decodeResource(getResources(), slip);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentX = event.getX();
			isSlipping = true;
			break;
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			isSlipping = false;
			if (currentX < onBgBitmap.getWidth() / 2) {
				currentToggleState = false;
			} else {
				currentToggleState = true;
			}
			if (isAvailable && currentToggleState != previousToggleState) {
				previousToggleState = currentToggleState;
				onToggleStateChangeListener.onToggleStateChange(currentToggleState);
			}
			break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(onBgBitmap.getWidth(), onBgBitmap.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//滑块左上角x坐标值
		float slipX = 0;
		Matrix matrix = new Matrix();
		Paint paint = new Paint();

		if (isSlipping) {
			if (currentX < onBgBitmap.getWidth() / 2) {
				canvas.drawBitmap(offBgBitmap, matrix, paint);
			} else {
				canvas.drawBitmap(onBgBitmap, matrix, paint);
			}
			if (currentX > onBgBitmap.getWidth()) {
				slipX = onBgBitmap.getWidth() - slipBitmap.getWidth();
			} else {
				slipX = currentX - slipBitmap.getWidth() / 2;
			}
			
			if (slipX < 0) {
				slipX = 0;
			}
			if (slipX > onBgBitmap.getWidth() - slipBitmap.getWidth()) {
				slipX = onBgBitmap.getWidth() - slipBitmap.getWidth();
			}
		} else {
			if (currentToggleState) {
				canvas.drawBitmap(onBgBitmap, matrix, paint);
				slipX = onBgBitmap.getWidth() - slipBitmap.getWidth();
			} else {
				canvas.drawBitmap(offBgBitmap, matrix, paint);
				slipX = 0;
			}
		}
		canvas.drawBitmap(slipBitmap, slipX, 0, paint);
	}

	public void setOnToggleStateChangeListener(OnToggleStateChangeListener onToggleStateChangeListener) {
		this.onToggleStateChangeListener = onToggleStateChangeListener;
		isAvailable = true;
	}

	public interface OnToggleStateChangeListener {
		public abstract void onToggleStateChange(boolean state);
	}
}
