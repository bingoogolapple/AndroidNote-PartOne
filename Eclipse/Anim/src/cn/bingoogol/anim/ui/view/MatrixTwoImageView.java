package cn.bingoogol.anim.ui.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MatrixTwoImageView extends ImageView {
	private static final int SCALE_REDUCE = 1;
	private static final int SCALE_ADD = 2;
	private static final int ROLATE_REDUCE = 3;
	private static final int ROLATE_ADD = 4;
	private boolean mOnAnimation = true;
	private int mRotateDegree = 3;
	private float mMinScale = 0.95f;
	private int mWidth;
	private int mHeight;
	private boolean mIsFinish = true;
	private boolean mIsActionMove = false;
	private boolean mIsScale = false;
	private Camera mCamera;
	boolean mXbigY = false;
	float mRolateX = 0;
	float mRolateY = 0;
	private OnViewClickListener mOnViewClickListener = null;

	public MatrixTwoImageView(Context context) {
		super(context);
		initConstructor();
	}

	public MatrixTwoImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initConstructor();
	}

	public MatrixTwoImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initConstructor();
	}

	private void initConstructor() {
		setAdjustViewBounds(true);
		setScaleType(ScaleType.MATRIX);
		mCamera = new Camera();
	}

	public void setOnClickIntent(OnViewClickListener onclick) {
		this.mOnViewClickListener = onclick;
	}

	/**
	 * 必要的初始化
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
			mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
			Drawable drawable = getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bd.setAntiAlias(true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (!mOnAnimation)
			return true;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			float X = event.getX();
			float Y = event.getY();
			mRolateX = mWidth / 2 - X;
			mRolateY = mHeight / 2 - Y;
			mXbigY = Math.abs(mRolateX) > Math.abs(mRolateY) ? true : false;
			mIsScale = X > mWidth / 3 && X < mWidth * 2 / 3 && Y > mHeight / 3 && Y < mHeight * 2 / 3;
			mIsActionMove = false;
			if (mIsScale) {
				handler.sendEmptyMessage(SCALE_REDUCE);
			} else {
				rolateHandler.sendEmptyMessage(1);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			if (x > mWidth || y > mHeight || x < 0 || y < 0) {
				mIsActionMove = true;
			} else {
				mIsActionMove = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsScale) {
				handler.sendEmptyMessage(6);
			} else {
				rolateHandler.sendEmptyMessage(6);
			}
			break;
		}
		return true;
	}

	public interface OnViewClickListener {
		public void onViewClick(MatrixTwoImageView view);
	}

	private Handler rolateHandler = new Handler() {
		private Matrix matrix = new Matrix();
		private float count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case 1:
				count = 0;
				beginRolate(matrix, (mXbigY ? count : 0), (mXbigY ? 0 : count));
				rolateHandler.sendEmptyMessage(2);
				break;
			case 2:
				beginRolate(matrix, (mXbigY ? count : 0), (mXbigY ? 0 : count));
				if (count < mRotateDegree) {
					rolateHandler.sendEmptyMessage(2);
				} else {
					mIsFinish = true;
				}
				count++;
				count++;
				break;
			case 3:
				beginRolate(matrix, (mXbigY ? count : 0), (mXbigY ? 0 : count));
				if (count > 0) {
					rolateHandler.sendEmptyMessage(3);
				} else {
					mIsFinish = true;
					if (!mIsActionMove && mOnViewClickListener != null) {
						mOnViewClickListener.onViewClick(MatrixTwoImageView.this);
					}
				}
				count--;
				count--;
				break;
			case 6:
				count = mRotateDegree;
				beginRolate(matrix, (mXbigY ? count : 0), (mXbigY ? 0 : count));
				rolateHandler.sendEmptyMessage(3);
				break;
			}
		}
	};

	private synchronized void beginRolate(Matrix matrix, float rolateX, float rolateY) {
		// Bitmap bm = getImageBitmap();
		int scaleX = (int) (mWidth * 0.5f);
		int scaleY = (int) (mHeight * 0.5f);
		mCamera.save();
		mCamera.rotateX(mRolateY > 0 ? rolateY : -rolateY);
		mCamera.rotateY(mRolateX < 0 ? rolateX : -rolateX);
		mCamera.getMatrix(matrix);
		mCamera.restore();
		// 控制中心点
		if (mRolateX > 0 && rolateX != 0) {
			matrix.preTranslate(-mWidth, -scaleY);
			matrix.postTranslate(mWidth, scaleY);
		} else if (mRolateY > 0 && rolateY != 0) {
			matrix.preTranslate(-scaleX, -mHeight);
			matrix.postTranslate(scaleX, mHeight);
		} else if (mRolateX < 0 && rolateX != 0) {
			matrix.preTranslate(-0, -scaleY);
			matrix.postTranslate(0, scaleY);
		} else if (mRolateY < 0 && rolateY != 0) {
			matrix.preTranslate(-scaleX, -0);
			matrix.postTranslate(scaleX, 0);
		}
		setImageMatrix(matrix);
	}

	private Handler handler = new Handler() {
		private Matrix matrix = new Matrix();
		private float s;
		int count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case SCALE_REDUCE:
				if (!mIsFinish) {
					return;
				} else {
					mIsFinish = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(mMinScale));
					BeginScale(matrix, s);
					handler.sendEmptyMessage(2);
				}
				break;
			case 2:
				BeginScale(matrix, s);
				if (count < 4) {
					handler.sendEmptyMessage(2);
				} else {
					mIsFinish = true;
					if (!mIsActionMove && mOnViewClickListener != null) {
						mOnViewClickListener.onViewClick(MatrixTwoImageView.this);
					}
				}
				count++;
				break;
			case 6:
				if (!mIsFinish) {
					handler.sendEmptyMessage(6);
				} else {
					mIsFinish = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(1.0f / mMinScale));
					BeginScale(matrix, s);
					handler.sendEmptyMessage(2);
				}
				break;
			}
		}
	};

	private synchronized void BeginScale(Matrix matrix, float scale) {
		int scaleX = (int) (mWidth * 0.5f);
		int scaleY = (int) (mHeight * 0.5f);
		matrix.postScale(scale, scale, scaleX, scaleY);
		setImageMatrix(matrix);
	}

}