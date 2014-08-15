package cn.bingoogol.painter.ui.view;

import java.util.Iterator;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import cn.bingoogol.painter.model.SPath;

public class SynView extends View {
	private static final String TAG = SynView.class.getSimpleName();
	private static final int LOCAL = 1;
	private static final int REMOTE = 2;
	private static final float TOUCH_TOLERANCE = 2;
	private static final float STROKE_WIDTH = 15;
	public static final int XFERMODE_CLEAR = 1;
	public static final int XFERMODE_SRC_TOP = 2;
	public static final int XFERMODE_NORMAL = 3;
	private static final Xfermode CLEAR_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private static final Xfermode SRC_ATOP_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
	private Bitmap mCacheBitmap;
	private Canvas mCacheCanvas;
	private Paint mBitmapPaint;
	// 本地
	private float mCurrentX;
	private float mCurrentY;
	private int mColor = Color.RED;
	private Xfermode mXfermode;
	private Paint mCachePaint;
	private Path mPath;
	private SPath mSPath;
	private Stack<SPath> mPathStack;
	// 远程
	private float mCurrentRemoteX;
	private float mCurrentRemoteY;
	private int mRemoteColor = Color.RED;
	private Xfermode mRemoteXfermode;
	private Paint mRemoteCachePaint;
	private Path mRemotePath;
	private SPath mRemoteSPath;
	private Stack<SPath> mRemotePathStack;
	private OnRemoteTouchListener mRemoteTouchListener;

	public SynView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SynView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPathStack = new Stack<SPath>();
		mRemotePathStack = new Stack<SPath>();
		mCacheCanvas = new Canvas();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
	}

	private void resetTempCanvas() {
		Log.i(TAG, "resetTempCanvas->getMeasuredWidth = " + getMeasuredWidth() + "   getMeasureHeight = " + getMeasuredHeight() + "  width = " + getWidth() + "   height = " + getHeight());
		mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		mCacheCanvas.setBitmap(mCacheBitmap);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		resetTempCanvas();
	}

	private Paint initPaint(int flag) {
		Paint paint = new Paint(Paint.DITHER_FLAG);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(STROKE_WIDTH);
		if (flag == LOCAL) {
			paint.setColor(mColor);
			paint.setXfermode(mXfermode);
		} else if (flag == REMOTE) {
			paint.setColor(mRemoteColor);
			paint.setXfermode(mRemoteXfermode);
		}
		return paint;
	}

	public void setOnRemoteTouchListener(OnRemoteTouchListener remoteTouchListener) {
		mRemoteTouchListener = remoteTouchListener;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public void setRemoteColor(int color) {
		mRemoteColor = color;
	}

	public void setXfermode(int xfermode) {
		switch (xfermode) {
		case XFERMODE_CLEAR:
			mXfermode = CLEAR_XFERMODE;
			break;
		case XFERMODE_SRC_TOP:
			mXfermode = SRC_ATOP_XFERMODE;
			break;
		case XFERMODE_NORMAL:
			mXfermode = null;
			break;
		}
	}

	public void setRemoteXfermode(int xfermode) {
		switch (xfermode) {
		case XFERMODE_CLEAR:
			mRemoteXfermode = CLEAR_XFERMODE;
			break;
		case XFERMODE_SRC_TOP:
			mRemoteXfermode = SRC_ATOP_XFERMODE;
			break;
		case XFERMODE_NORMAL:
			mRemoteXfermode = null;
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onTouchStart(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			onTouchMove(x, y);
			break;
		case MotionEvent.ACTION_UP:
			onTouchUp();
			break;
		}
		return true;
	}

	private void onTouchStart(float x, float y) {
		if (mRemoteTouchListener != null) {
			mRemoteTouchListener.onTouchStart(x, y);
		}
		mCachePaint = initPaint(LOCAL);
		mSPath = new SPath(mColor, STROKE_WIDTH);
		mSPath.moveTo(x, y);

		mPath = new Path();
		mPath.moveTo(x, y);
		mCurrentX = x;
		mCurrentY = y;
		if (isSavePath()) {
			mCacheCanvas.drawPath(mPath, mCachePaint);
			// mSPath.draw(mCacheCanvas, mCachePaint);
		}
		invalidate();
	}

	public void onRemoteTouchStart(float x, float y) {
		mRemoteCachePaint = initPaint(REMOTE);
		// 每一次记录的路径对象是不一样的
		mRemoteSPath = new SPath(mRemoteColor, STROKE_WIDTH);
		mRemoteSPath.moveTo(x, y);

		mRemotePath = new Path();
		mRemotePath.moveTo(x, y);

		mCurrentRemoteX = x;
		mCurrentRemoteY = y;
		if (isSaveRemotePath()) {
			// mRemoteSPath.draw(mCacheCanvas, mRemoteCachePaint);
			mCacheCanvas.drawPath(mRemotePath, mRemoteCachePaint);
		}
		invalidate();
	}

	private void onTouchMove(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(mCurrentY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			if (mRemoteTouchListener != null) {
				mRemoteTouchListener.onTouchMove(x, y);
			}
			mSPath.moveTo(x, y);

			mPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX) / 2, (y + mCurrentY) / 2);
			mCurrentX = x;
			mCurrentY = y;
			if (isSavePath()) {
				// mSPath.draw(mCacheCanvas, mCachePaint);
				mCacheCanvas.drawPath(mPath, mCachePaint);
			}
			invalidate();
		}
	}

	public void onRemoteTouchMove(float x, float y) {
		mRemoteSPath.moveTo(x, y);

		mRemotePath.quadTo(mCurrentRemoteX, mCurrentRemoteY, (x + mCurrentRemoteX) / 2, (y + mCurrentRemoteY) / 2);
		mCurrentRemoteX = x;
		mCurrentRemoteY = y;
		if (isSaveRemotePath()) {
			// mRemoteSPath.draw(mCacheCanvas, mRemoteCachePaint);
			mCacheCanvas.drawPath(mRemotePath, mRemoteCachePaint);
		}
		invalidate();
	}

	private void onTouchUp() {
		if (mRemoteTouchListener != null) {
			mRemoteTouchListener.onTouchUp();
		}
		if (isSavePath()) {
			mPathStack.push(mSPath);
		}
		mSPath = null;
		mPath = null;
		invalidate();
	}

	public void onRemoteTouchUp() {
		if (isSaveRemotePath()) {
			mRemotePathStack.push(mRemoteSPath);
		}
		mRemoteSPath = null;
		mRemotePath = null;
		invalidate();
	}

	private boolean isSavePath() {
		return mXfermode != SRC_ATOP_XFERMODE;
	}

	private boolean isSaveRemotePath() {
		return mRemoteXfermode != SRC_ATOP_XFERMODE;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 将前面已经画过得显示出来
		canvas.drawBitmap(mCacheBitmap, 0, 0, mBitmapPaint);
		// if (mSPath != null && mXfermode == SRC_ATOP_XFERMODE) {
		// mSPath.drawPath(canvas, mCachePaint);
		// }
		// if (mRemoteSPath != null && mRemoteXfermode == SRC_ATOP_XFERMODE) {
		// mRemoteSPath.drawPath(canvas, mRemoteCachePaint);
		// }

		if (mPath != null && mXfermode == SRC_ATOP_XFERMODE) {
			canvas.drawPath(mPath, mCachePaint);
		}
		if (mRemotePath != null && mRemoteXfermode == SRC_ATOP_XFERMODE) {
			canvas.drawPath(mRemotePath, mRemoteCachePaint);
		}
	}

	private void rePaint() {
		resetTempCanvas();
		mCachePaint = initPaint(LOCAL);
		Iterator<SPath> iter = mPathStack.iterator();
		while (iter.hasNext()) {
			iter.next().drawPath(mCacheCanvas, mCachePaint);
		}
		mSPath = null;
		mRemoteCachePaint = initPaint(REMOTE);
		Iterator<SPath> remoteIter = mRemotePathStack.iterator();
		while (remoteIter.hasNext()) {
			remoteIter.next().drawPath(mCacheCanvas, mRemoteCachePaint);
		}
		mRemoteSPath = null;
		invalidate();
	}

	public interface OnRemoteTouchListener {
		public void onTouchStart(float x, float y);

		public void onTouchMove(float x, float y);

		public void onTouchUp();
	}
}