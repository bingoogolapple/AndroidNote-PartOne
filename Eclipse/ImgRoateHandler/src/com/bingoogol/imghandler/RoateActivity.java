package com.bingoogol.imghandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class RoateActivity extends Activity implements OnClickListener {
	private ImageView testIv;
	private Button roateBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roate);
		//初始化组件
		testIv = (ImageView) this.findViewById(R.id.iv_test);
		roateBtn = (Button) this.findViewById(R.id.btn_rotate);
		//给旋转按钮添加点击事件
		roateBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		//设置可以获取ImageView组件的位图图像
		testIv.setDrawingCacheEnabled(true);
		Bitmap bitmap = testIv.getDrawingCache();
		//新建位图图像,该位图图像的所有参数和bitmap的参数是一样的
		Bitmap alterBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		//新建画布
		Canvas canvas = new Canvas(alterBitmap);
		//新建图像矩阵
		Matrix matrix = new Matrix();
		
		//以图片的中心为旋转中心，旋转90度
		matrix.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		
		//新建一个画笔
		Paint paint = new Paint();
		//消除锯齿
		paint.setAntiAlias(true);
		//把旋转后的图片画到画布上
		canvas.drawBitmap(bitmap, matrix, paint);
		//设置ImageView的位图图像
		testIv.setImageBitmap(alterBitmap);
	}
}
