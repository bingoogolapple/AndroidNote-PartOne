package cn.bingoogol.paint.ui.activity.ui.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import cn.bingoogol.paint.model.SDrawPath;
import cn.bingoogol.paint.model.SPaint;
import cn.bingoogol.paint.model.SPath;

public class TuyaView extends View {
	private static final String TAG = TuyaView.class.getSimpleName();
	private static final MaskFilter EMBOSSMASKFILTER = new EmbossMaskFilter(new float[] { 1, 1, 3 }, 0.4f, 8, 3f);
	private static final MaskFilter BLURMASKFILTER = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
	private static final Xfermode CLEAR_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private static final Xfermode SRC_ATOP_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
	// 保存一次一次绘制出来的图形。通过mTempCanvas和mTempPaint将画笔路径保存到mTempBitmap中。在onDraw方法中先将mTempBitmap画到真实的画布中，然后再画mCurrentPath到真实画布中（达到实时显示的效果）
	private Bitmap mTempBitmap;
	private Canvas mTempCanvas;
	private SPaint mPaint;
	private SPath mCurrentPath;
	private SDrawPath mCurrentDrawPath;
	// 保存Path路径栈，用于后退步骤
	private static Stack<SDrawPath> mSaveDrawPath;
	// 保存Path路栈,用于前进步骤
	private static Stack<SDrawPath> mCancleDrawPath;
	private float mCurrentX;
	private float mCurrentY;
	private static final float TOUCH_TOLERANCE = 4;
	private float mStrokeWidth = 15;
	private int mColor = Color.RED;
	private MaskFilter mMaskFilter;
	private Xfermode mXfermode;
	private SPaint mBitmapPaint;
	private Point point;

	public TuyaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TuyaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mSaveDrawPath = new Stack<SDrawPath>();
		mCancleDrawPath = new Stack<SDrawPath>();
		mTempCanvas = new Canvas();
		mBitmapPaint = new SPaint(SPaint.DITHER_FLAG);
	}

	private void resetTempCanvas() {
		Log.i(TAG, "resetTempCanvas->getMeasuredWidth = " + getMeasuredWidth() + "   getMeasureHeight = " + getMeasuredHeight() + "  width = " + getWidth() + "   height = " + getHeight());
		mTempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		mTempCanvas.setBitmap(mTempBitmap);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		resetTempCanvas();
	}

	private void initPaint() {
		mPaint = new SPaint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(SPaint.Style.STROKE);
		mPaint.setStrokeJoin(SPaint.Join.ROUND);
		mPaint.setStrokeCap(SPaint.Cap.ROUND);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setColor(mColor);
		mPaint.setMaskFilter(mMaskFilter);
		mPaint.setXfermode(mXfermode);
	}

	public void setColor(int color) {
		mColor = color;
	}

	public void setMaskFilter(MaskFilterType maskFilterType) {
		switch (maskFilterType) {
		case EMBOSSMASK:
			mMaskFilter = EMBOSSMASKFILTER;
			break;
		case BLURMASK:
			mMaskFilter = BLURMASKFILTER;
			break;
		case NONEMASK:
			mMaskFilter = null;
			break;
		}
	}

	public void setXfermodeType(XfermodeType xfermodeType) {
		switch (xfermodeType) {
		case CLEARXFERMODE:
			mXfermode = CLEAR_XFERMODE;
			break;
		case SRCATOPXFERMODE:
			mXfermode = SRC_ATOP_XFERMODE;
			break;
		case NONEXFERMODE:
			mXfermode = null;
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
		initPaint();
		// 每一次记录的路径对象是不一样的
		mCurrentPath = new SPath();
		mCurrentPath.moveTo(x, y);
		mCurrentX = x;
		mCurrentY = y;
		if (isSaveDrawPath()) {
			mCurrentDrawPath = new SDrawPath(mCurrentPath, mPaint);
			mTempCanvas.drawPath(mCurrentPath, mPaint);
		}
		invalidate();
	}

	private void onTouchMove(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(mCurrentY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
			mCurrentPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX) / 2, (y + mCurrentY) / 2);
			mCurrentX = x;
			mCurrentY = y;
			if (isSaveDrawPath()) {
				mTempCanvas.drawPath(mCurrentPath, mPaint);
			}
			invalidate();
		}
	}

	private void onTouchUp() {
		if (isSaveDrawPath()) {
			mCurrentPath.lineTo(mCurrentX, mCurrentY);
			mTempCanvas.drawPath(mCurrentPath, mPaint);
			// 将一条完整的路径保存下来
			mSaveDrawPath.push(mCurrentDrawPath);
		}
		mCurrentPath = null;
		invalidate();
	}

	private boolean isSaveDrawPath() {
		return mXfermode != SRC_ATOP_XFERMODE;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 将前面已经画过得显示出来
		canvas.drawBitmap(mTempBitmap, 0, 0, mBitmapPaint);
		if (mCurrentPath != null && mXfermode == SRC_ATOP_XFERMODE) {
			canvas.drawPath(mCurrentPath, mPaint);
		}
	}

	public boolean undo() {
		if (!mSaveDrawPath.empty()) {
			mCancleDrawPath.add(mSaveDrawPath.pop());
			rePaint();
			return true;
		}
		return false;
	}

	public boolean redo() {
		if (!mCancleDrawPath.empty()) {
			mSaveDrawPath.add(mCancleDrawPath.pop());
			rePaint();
			return true;
		}
		return false;
	}

	private void rePaint() {
		resetTempCanvas();
		Iterator<SDrawPath> iter = mSaveDrawPath.iterator();
		while (iter.hasNext()) {
			SDrawPath drawPath = iter.next();
			mTempCanvas.drawPath(drawPath.path, drawPath.paint);
		}
		mCurrentPath = null;
		invalidate();
	}

	public enum MaskFilterType {
		EMBOSSMASK, BLURMASK, NONEMASK
	}

	public enum XfermodeType {
		CLEARXFERMODE, SRCATOPXFERMODE, NONEXFERMODE
	}

	public void save() {
		File sdFile = new File(Environment.getExternalStorageDirectory(), "oauth_1.out");
		try {
			FileOutputStream fos = new FileOutputStream(sdFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mSaveDrawPath);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toast.makeText(getContext(), "成功保存到sd卡", Toast.LENGTH_SHORT).show();
	}

	public void recover() {
		File sdFile = new File(Environment.getExternalStorageDirectory(), "oauth_1.out");
		try {
			FileInputStream fis = new FileInputStream(sdFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mSaveDrawPath = (Stack<SDrawPath>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		rePaint();
	}
}
