package com.bingoogol.imghandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.ImageView;

public class SYActivity extends Activity {
	private ImageView testIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sy);
		// 初始化组件
		testIv = (ImageView) this.findViewById(R.id.iv_test);
		handle();
	}

	private void handle() {
		//第一张位图
		Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
		//第二张位图
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
		//空白位图
		Bitmap alterBitmap = Bitmap.createBitmap(bitmap1.getWidth() + 100, bitmap1.getHeight() + 100, bitmap1.getConfig());

		// 用空白位图新建一张画布
		Canvas canvas = new Canvas(alterBitmap);
		
		// 新建一个画笔
		Paint paint = new Paint();
		//核心
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
		
		//画第一张图
		canvas.drawBitmap(bitmap1, new Matrix(), paint);
		
		Matrix matrix = new Matrix();
		
		//设置第二张图的原点坐标距离右侧300，距离下方200
		matrix.preTranslate(canvas.getWidth()-300, canvas.getHeight() - 200);
		//画第二张图
		canvas.drawBitmap(bitmap2, matrix, paint);
		
		// 设置ImageView的位图图像
		testIv.setImageBitmap(alterBitmap);
	}
}
