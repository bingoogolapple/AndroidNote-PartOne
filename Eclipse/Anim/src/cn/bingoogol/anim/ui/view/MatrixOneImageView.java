package cn.bingoogol.anim.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MatrixOneImageView extends ImageView {
	private static final int SCALE_REDUCE = 1;
	private static final int SCALE_ADD = 2;
	private static final float mMinScale = 0.95f;
	private Point mCenter;
	private boolean mIsScaling = false;
	private OnViewClickListener mOnViewClickListener = null;
	private int mReduceCount = 0;
	private int mAddCount = 0;
	private Matrix mMatrix = new Matrix();
	private float mScale;

	public MatrixOneImageView(Context context) {
		super(context);
		initConstructor();
	}

	public MatrixOneImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initConstructor();
	}

	public MatrixOneImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initConstructor();
	}

	private void initConstructor() {
		setAdjustViewBounds(true);
		setScaleType(ScaleType.MATRIX);
	}

	public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
		mOnViewClickListener = onViewClickListener;
	}

	/**
	 * 必要的初始化
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			int mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
			int mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
			mCenter = new Point(mWidth / 2, mHeight / 2);
			Drawable drawable = getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bd.setAntiAlias(true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsScaling) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsScaling = true;
			mReduceCount = 0;
			mScale = (float) Math.sqrt(Math.sqrt(mMinScale));
			mMatrix.set(getImageMatrix());
			beginScale(mMatrix, mScale);
			mScaleHandler.sendEmptyMessage(SCALE_REDUCE);
			break;
		}
		return true;
	}

	/**
	 * 控制缩放的Handler
	 */
	private Handler mScaleHandler = new Handler() {

		public void handleMessage(Message msg) {
			mMatrix.set(getImageMatrix());
			switch (msg.what) {
			case SCALE_REDUCE:
				beginScale(mMatrix, mScale);
				if (mReduceCount < 4) {
					mScaleHandler.sendEmptyMessage(SCALE_REDUCE);
				} else {
					mAddCount = 0;
					mScale = (float) Math.sqrt(Math.sqrt(1.0f / mMinScale));
					mMatrix.set(getImageMatrix());
					beginScale(mMatrix, mScale);
					mScaleHandler.sendEmptyMessage(SCALE_ADD);
				}
				mReduceCount++;
				break;
			case SCALE_ADD:
				beginScale(mMatrix, mScale);
				if (mAddCount < 4) {
					mScaleHandler.sendEmptyMessage(SCALE_ADD);
				} else {
					mIsScaling = false;
					if (mOnViewClickListener != null) {
						mOnViewClickListener.onViewClick(MatrixOneImageView.this);
					}
				}
				mAddCount++;
				break;
			}
		}
	};

	/**
	 * 缩放
	 * 
	 * @param matrix
	 * @param scale
	 */
	private synchronized void beginScale(Matrix matrix, float scale) {
		matrix.postScale(scale, scale, mCenter.x, mCenter.y);
		setImageMatrix(matrix);
	}

	public interface OnViewClickListener {
		public void onViewClick(MatrixOneImageView view);
	}

}