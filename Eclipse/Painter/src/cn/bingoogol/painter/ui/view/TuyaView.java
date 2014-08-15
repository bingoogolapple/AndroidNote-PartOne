package cn.bingoogol.painter.ui.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import cn.bingoogol.painter.model.Path;

public class TuyaView extends View {
	private static final String TAG = TuyaView.class.getSimpleName();
	private static final MaskFilter EMBOSSMASKFILTER = new EmbossMaskFilter(new float[] { 1, 1, 3 }, 0.4f, 8, 3f);
	private static final MaskFilter BLURMASKFILTER = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
	private static final Xfermode CLEAR_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private static final Xfermode SRC_ATOP_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
	// 保存一次一次绘制出来的图形。通过mTempCanvas和mTempPaint将画笔路径保存到mTempBitmap中。在onDraw方法中先将mTempBitmap画到真实的画布中，然后再画mCurrentPath到真实画布中（达到实时显示的效果）
	private Bitmap mTempBitmap;
	private Canvas mTempCanvas;
	private Paint mPaint;
	private Path mCurrentPath;
	// 保存Path路径栈，用于后退步骤
	private static Stack<Path> mSavePath;
	// 保存Path路栈,用于前进步骤
	private static Stack<Path> mCanclePath;
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
		mSavePath = new Stack<Path>();
		mCanclePath = new Stack<Path>();
		mTempCanvas = new Canvas();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
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
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
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
		mCurrentPath = new Path(mColor, mStrokeWidth);
		mCurrentPath.moveTo(x, y);
		mCurrentX = x;
		mCurrentY = y;
		if (isSaveDrawPath()) {
			mCurrentPath.draw(mTempCanvas, mPaint);
		}
		invalidate();
	}

	private void onTouchMove(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(mCurrentY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mCurrentPath.moveTo(x, y);
			mCurrentX = x;
			mCurrentY = y;
			if (isSaveDrawPath()) {
				mCurrentPath.draw(mTempCanvas, mPaint);
			}
			invalidate();
		}
	}

	private void onTouchUp() {
		if (isSaveDrawPath()) {
			// 将一条完整的路径保存下来
			mSavePath.push(mCurrentPath);
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
			mCurrentPath.drawPath(canvas, mPaint);
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

	private void rePaint() {
		initPaint();
		resetTempCanvas();
		Iterator<Path> iter = mSavePath.iterator();
		while (iter.hasNext()) {
			iter.next().drawPath(mTempCanvas, mPaint);
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
			mSavePath = (Stack<Path>) ois.readObject();
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
				mSavePath = (Stack<Path>) ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rePaint();
	}
}
