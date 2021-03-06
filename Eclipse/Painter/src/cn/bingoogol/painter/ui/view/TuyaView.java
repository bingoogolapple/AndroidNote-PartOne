package cn.bingoogol.painter.ui.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Stack;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import cn.bingoogol.painter.model.Point;
import cn.bingoogol.painter.model.SPath;

public class TuyaView extends View {
	private static final String TAG = TuyaView.class.getSimpleName();
	private static final MaskFilter EMBOSSMASKFILTER = new EmbossMaskFilter(new float[] { 1, 1, 3 }, 0.4f, 8, 3f);
	private static final MaskFilter BLURMASKFILTER = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
	private static final Xfermode CLEAR_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private static final Xfermode SRC_ATOP_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
	// 保存一次一次绘制出来的图形。通过mCacheCanvas和mCachePaint将画笔路径保存到mCacheBitmap中。在onDraw方法中先将mCacheBitmap画到真实的画布中，然后再画mCurrentPath到真实画布中（达到实时显示的效果）
	private Bitmap mCacheBitmap;
	private Canvas mCacheCanvas;
	private Paint mCachePaint;
	private Path mCurrentPath;
	private SPath mCurrentSPath;
	// 保存Path路径栈，用于后退步骤
	private Stack<SPath> mSavePath;
	// 保存Path路栈,用于前进步骤
	private Stack<SPath> mCanclePath;
	private float mCurrentX;
	private float mCurrentY;
	private static final float TOUCH_TOLERANCE = 4;
	private float mStrokeWidth = 15;
	private int mColor = Color.RED;
	private MaskFilter mMaskFilter;
	private Xfermode mXfermode;
	private Paint mBitmapPaint;

	public TuyaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TuyaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mSavePath = new Stack<SPath>();
		mCanclePath = new Stack<SPath>();
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

	private void initPaint() {
		mCachePaint = new Paint();
		mCachePaint.setAntiAlias(true);
		mCachePaint.setDither(true);
		mCachePaint.setStyle(Paint.Style.STROKE);
		mCachePaint.setStrokeJoin(Paint.Join.ROUND);
		mCachePaint.setStrokeCap(Paint.Cap.ROUND);
		mCachePaint.setStrokeWidth(mStrokeWidth);
		mCachePaint.setColor(mColor);
		mCachePaint.setMaskFilter(mMaskFilter);
		mCachePaint.setXfermode(mXfermode);
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
		mCurrentSPath = new SPath(mColor, mStrokeWidth);
		mCurrentSPath.moveTo(x, y);

		mCurrentPath = new Path();
		mCurrentPath.moveTo(x, y);
		mCurrentX = x;
		mCurrentY = y;
		if (isSaveDrawPath()) {
			// mCurrentSPath.draw(mCacheCanvas, mCachePaint);
			mCacheCanvas.drawPath(mCurrentPath, mCachePaint);
		}
		invalidate();
	}

	private void onTouchMove(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(mCurrentY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mCurrentPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX) / 2, (y + mCurrentY) / 2);

			mCurrentSPath.moveTo(x, y);
			mCurrentX = x;
			mCurrentY = y;
			if (isSaveDrawPath()) {
				// mCurrentSPath.draw(mCacheCanvas, mCachePaint);
				mCacheCanvas.drawPath(mCurrentPath, mCachePaint);
			}
			invalidate();
		}
	}

	private void onTouchUp() {
		if (isSaveDrawPath()) {
			// 将一条完整的路径保存下来
			mSavePath.push(mCurrentSPath);
		}
		mCurrentPath = null;
		mCurrentSPath = null;
		invalidate();
	}

	private boolean isSaveDrawPath() {
		return mXfermode != SRC_ATOP_XFERMODE;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 将前面已经画过得显示出来
		canvas.drawBitmap(mCacheBitmap, 0, 0, mBitmapPaint);
		// if (mCurrentSPath != null && mXfermode == SRC_ATOP_XFERMODE) {
		// mCurrentSPath.drawPath(canvas, mCachePaint);
		// }
		if (mCurrentPath != null && mXfermode == SRC_ATOP_XFERMODE) {
			canvas.drawPath(mCurrentPath, mCachePaint);
		}
	}

	public boolean undo() {
		if (!mSavePath.empty()) {
			mCanclePath.add(mSavePath.pop());
			rePaint();
			return true;
		}
		return false;
	}

	public boolean redo() {
		if (!mCanclePath.empty()) {
			mSavePath.add(mCanclePath.pop());
			rePaint();
			return true;
		}
		return false;
	}

	// private void rePaint() {
	// initPaint();
	// resetTempCanvas();
	// Iterator<SPath> iter = mSavePath.iterator();
	// while (iter.hasNext()) {
	// iter.next().drawPath(mCacheCanvas, mCachePaint);
	// }
	// mCurrentSPath = null;
	// invalidate();
	// }

	private void rePaint() {
		resetTempCanvas();
		Iterator<SPath> iter = mSavePath.iterator();
		while (iter.hasNext()) {
			SPath spath = iter.next();
			mColor = spath.color;
			mStrokeWidth = spath.width;
			initPaint();
			Path path = new Path();
			int size = spath.points.size();
			if (size > 0) {
				path.moveTo(spath.points.get(0).x, spath.points.get(0).y);
				for (int i = 1; i < size; i++) {
					path.quadTo(spath.points.get(i - 1).x, spath.points.get(i - 1).y, (spath.points.get(i).x + spath.points.get(i - 1).x) / 2, (spath.points.get(i).y + spath.points.get(i - 1).y) / 2);
				}
				mCacheCanvas.drawPath(path, mCachePaint);
			}
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

	public void save2SDCard() {
		File sdFile = new File(Environment.getExternalStorageDirectory(), "painter.out");
		try {
			FileOutputStream fos = new FileOutputStream(sdFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mSavePath);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(getContext(), "成功保存到sd卡", Toast.LENGTH_SHORT).show();
	}

	public void recoverFromSDCard() {
		File sdFile = new File(Environment.getExternalStorageDirectory(), "painter.out");
		try {
			FileInputStream fis = new FileInputStream(sdFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mSavePath = (Stack<SPath>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		rePaint();
	}

	public void save2Sp() {
		try {
			// 保存对象
			SharedPreferences.Editor sharedata = getContext().getSharedPreferences("Painter", Context.MODE_PRIVATE).edit();
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			// 将对象序列化写入byte缓存
			os.writeObject(mSavePath);
			// 将序列化的数据转为16进制保存
			String bytesToHexString = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
			// 保存该16进制数组
			sharedata.putString("paint", bytesToHexString);
			sharedata.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toast.makeText(getContext(), "成功保存到sp", Toast.LENGTH_SHORT).show();
	}

	public void recoverFromSp() {
		try {
			SharedPreferences sp = getContext().getSharedPreferences("Painter", Context.MODE_PRIVATE);
			if (sp.contains("paint")) {
				String string = sp.getString("paint", "");
				// 将16进制的数据转为数组，准备反序列化
				byte[] stringToBytes = Base64.decode(string, Base64.DEFAULT);
				ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
				ObjectInputStream ois = new ObjectInputStream(bis);
				// 返回反序列化得到的对象
				mSavePath = (Stack<SPath>) ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rePaint();
	}
}
