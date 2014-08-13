package cn.bingoogol.paintsender.ui.activity.ui.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class TuyaView extends View {
	private static final MaskFilter EMBOSSMASKFILTER = new EmbossMaskFilter(new float[] { 1, 1, 3 }, 0.4f, 8, 3f);
	private static final MaskFilter BLURMASKFILTER = new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL);
	private static final Xfermode CLEAR_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private static final Xfermode SRC_ATOP_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	// 画布的画笔
	private Paint mBitmapPaint;
	// 真实的画笔
	private Paint mPaint;
	// 临时点坐标
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	// 保存Path路径的集合,用List集合来模拟栈，用于后退步骤
	private static List<DrawPath> mSaveDrawPath;
	// 保存Path路径的集合,用List集合来模拟栈,用于前进步骤
	private static List<DrawPath> mCancleDrawPath;
	// 记录Path路径的对象
	private DrawPath mDrawPath;
	private int mScreenWidth, mScreenHeight;
	private float mStrokeWidth = 15;
	private int mColor = Color.RED;
	private MaskFilter mMaskFilter;
	private Xfermode mXfermode;

	public TuyaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TuyaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
		// 保存一次一次绘制出来的图形
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mSaveDrawPath = new ArrayList<DrawPath>();
		mCancleDrawPath = new ArrayList<DrawPath>();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
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
			// 每次down下去重新初始化画笔
			initPaint();
			// 重置下一步操作
			mCancleDrawPath = new ArrayList<DrawPath>();
			// 每次down下去重新new一个Path
			mPath = new Path();
			// 每一次记录的路径对象是不一样的
			mDrawPath = new DrawPath();
			mDrawPath.path = mPath;
			mDrawPath.paint = mPaint;
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	private void touch_start(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(mY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		// 将一条完整的路径保存下来(相当于入栈操作)
		mSaveDrawPath.add(mDrawPath);
		mPath = null;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 将前面已经画过得显示出来
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		if (mPath != null) {
			// 实时的显示
			canvas.drawPath(mPath, mPaint);
		}
	}

	/**
	 * 撤销的核心思想就是将画布清空， 将保存下来的Path路径最后一个移除掉， 重新将路径画在画布上面。
	 */
	public int undo() {
		mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
		// 重新设置画布，相当于清空画布
		mCanvas.setBitmap(mBitmap);
		// 清空画布，但是如果图片有背景的话，则使用上面的重新初始化的方法，用该方法会将背景清空掉…
		if (mSaveDrawPath.size() > 0) {
			DrawPath dPath = mSaveDrawPath.get(mSaveDrawPath.size() - 1);
			mCancleDrawPath.add(dPath);

			// 移除最后一个path,相当于出栈操作
			mSaveDrawPath.remove(mSaveDrawPath.size() - 1);

			Iterator<DrawPath> iter = mSaveDrawPath.iterator();
			while (iter.hasNext()) {
				DrawPath drawPath = iter.next();
				mCanvas.drawPath(drawPath.path, drawPath.paint);
			}
			invalidate();// 刷新
		} else {
			return -1;
		}
		return mSaveDrawPath.size();
	}

	/**
	 * 重做的核心思想就是将撤销的路径保存到另外一个集合里面(栈)， 然后从redo的集合里面取出最顶端对象， 画在画布上面即可。
	 */
	public int redo() {
		mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		// 清空画布，但是如果图片有背景的话，则使用上面的重新初始化的方法，用该方法会将背景清空掉…
		if (mCancleDrawPath.size() > 0) {
			// 移除最后一个path,相当于出栈操作
			DrawPath dPath = mCancleDrawPath.get(mCancleDrawPath.size() - 1);
			mSaveDrawPath.add(dPath);
			mCancleDrawPath.remove(mCancleDrawPath.size() - 1);

			Iterator<DrawPath> iter = mSaveDrawPath.iterator();
			while (iter.hasNext()) {
				DrawPath drawPath = iter.next();
				mCanvas.drawPath(drawPath.path, drawPath.paint);
			}
			invalidate();// 刷新
		}
		return mCancleDrawPath.size();
	}

	private class DrawPath {
		public Path path;
		public Paint paint;
	}

	public enum MaskFilterType {
		EMBOSSMASK, BLURMASK, NONEMASK
	}

	public enum XfermodeType {
		CLEARXFERMODE, SRCATOPXFERMODE, NONEXFERMODE
	}

}
