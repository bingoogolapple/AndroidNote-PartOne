package cn.bingoogol.circleprogressbar.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import cn.bingoogol.circleprogressbar.R;

public class CircleProgressBar extends View {
	// 画实心圆的画笔
	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingPaint;
	// 画字体的画笔
	private Paint mTextPaint;
	// 圆形颜色
	private int mCircleColor = Color.BLUE;;
	// 圆环颜色
	private int mRingColor = Color.GREEN;
	// 圆环宽度
	private int mStrokeWidth = 15;
	private int mTextColor = Color.GREEN;
	private int mMax = 100;
	private int mProgress = 0;
	private int mTextSize = 15;
	// 字的长度
	private float mTxtWidth;
	// 字的高度
	private float mTxtHeight;
	private int mCentre;
	private int mCircleRadius;
	private int mRingRadius;
	private RectF mOval;

	public CircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
		final int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CircleProgressBar_textColor:
				mTextColor = typedArray.getColor(attr, mTextColor);
				break;
			case R.styleable.CircleProgressBar_circleColor:
				mCircleColor = typedArray.getColor(attr, mCircleColor);
				break;
			case R.styleable.CircleProgressBar_ringColor:
				mRingColor = typedArray.getColor(attr, mRingColor);
				break;
			case R.styleable.CircleProgressBar_strokeWidth:
				mStrokeWidth = typedArray.getDimensionPixelSize(attr, mStrokeWidth);
				break;
			case R.styleable.CircleProgressBar_textSize:
				mTextSize = typedArray.getDimensionPixelSize(attr, mTextSize);
				break;
			case R.styleable.CircleProgressBar_max:
				mMax = typedArray.getInteger(attr, mMax);
				break;
			case R.styleable.CircleProgressBar_progress:
				mProgress = typedArray.getInteger(attr, mProgress);
				break;
			}
		}
		typedArray.recycle();
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.FILL);

		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setTextSize(mTextSize);

		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCentre = getWidth() / 2;
		mCircleRadius = mCentre - mStrokeWidth;
		mRingRadius = mCircleRadius + mStrokeWidth / 2;
		mOval = new RectF(mCentre - mRingRadius, mCentre - mRingRadius, mCentre + mRingRadius, mCentre + mRingRadius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(mCentre, mCentre, mCircleRadius, mCirclePaint);
		if (mProgress >= 0 && mProgress <= mMax) {
			canvas.drawArc(mOval, 0, ((float) mProgress / mMax) * 360, false, mRingPaint);
			String txt = (mProgress * 100 / mMax) + "%";
			mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
			canvas.drawText(txt, mCentre - mTxtWidth / 2, mCentre + mTxtHeight / 4, mTextPaint);
		}
	}

	public void setMax(int max) {
		if (max >= 0) {
			mMax = max;
		}
	}

	public void setProgress(int progress) {
		if (progress < 0) {
			mProgress = 0;
		} else if (progress > mMax) {
			mProgress = mMax;
		} else {
			mProgress = progress;
		}
		postInvalidate();
	}

	public int getProgress() {
		return mProgress;
	}
}
