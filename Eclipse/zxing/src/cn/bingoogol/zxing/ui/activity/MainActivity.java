package cn.bingoogol.zxing.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bingoogol.zxing.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class MainActivity extends BaseActivity {
	private TextView mResultTv;
	private ImageView mResultIv;
	private EditText mContentEt;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		mResultTv = (TextView) findViewById(R.id.tv_main_result);
		mResultIv = (ImageView) findViewById(R.id.iv_main_result);
		mContentEt = (EditText) findViewById(R.id.et_main_content);

	}

	@Override
	protected void setListener() {
		findViewById(R.id.btn_main_scan).setOnClickListener(this);
		findViewById(R.id.btn_main_generate1).setOnClickListener(this);
		findViewById(R.id.btn_main_generate2).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_scan:
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(mApp, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			break;
		case R.id.btn_main_generate1:
			generate1();
			break;
		case R.id.btn_main_generate2:
			//generate2();

			try {
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode("王浩", 350);
				mResultIv.setImageBitmap(qrCodeBitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private void generate2() {
		// 用户输入的需要生成二维码的url字符串
		String url = mContentEt.getText().toString().trim();
		// 声明生成的二维码图片
		Bitmap image = null;

		try {
			// 当用户输入的字符串url不为空时
			if (url != null && !"".equals(url)) {
				// 将用户输入的url作为参数，调用创建二维码的方法
				image = createTwoQRCode(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 若生成的二维码不为空，即生成二维码成功
		if (image != null) {
			// 将生成的二维码显示给用户
			mResultIv.setImageBitmap(image);
		}
	}

	private void generate1() {
		// 该变量用于保存用户输入的字符串
		String url = mContentEt.getText().toString().trim();
		// 判断用户输入的字符串是否包含中文
		for (int i = 0; i < url.length(); i++) {
			int c = url.charAt(i);
			// 若包含中文，提示用户条形码不能包含中文，同时结束该操作
			if (19968 <= c && c < 40623) {
				// 提示用户
				Toast.makeText(MainActivity.this, "不能包含中文", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		// 生成的条形码图片
		Bitmap image = null;
		try {
			// 当用户输入的url不为空时
			if (url != null && !"".equals(url)) {
				// 将用户输入的url作为参数，调用创建条形码的方法，生成条形码图片
				image = createOneQRCode(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (image != null) {
			// 将生成的条形码显示到界面的图片中
			mResultIv.setImageBitmap(image);
		}
	}

	/**
	 * 创建条形码的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public Bitmap createOneQRCode(String content) throws Exception {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200);
		// 矩阵的宽度
		int width = matrix.getWidth();
		// 矩阵的高度
		int height = matrix.getHeight();
		// 矩阵像素数组
		int[] pixels = new int[width * height];
		// 双重循环遍历每一个矩阵点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					// 设置矩阵像素点的值
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		// 根据颜色数组来创建位图
		/**
		 * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图， 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
		 * config是一个枚举，可以用它来指定位图“质量”。
		 */
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bm.setPixels(pixels, 0, width, 0, 0, width, height);
		// 将生成的条形码返回给调用者
		return bm;
	}

	/**
	 * 创建二维码的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public Bitmap createTwoQRCode(String content) throws Exception {
		// 生成二维码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
		// 矩阵的宽度
		int width = matrix.getWidth();
		// 矩阵的高度
		int height = matrix.getHeight();
		// 矩阵像素数组
		int[] pixels = new int[width * height];
		// 双重循环遍历每一个矩阵点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					// 设置矩阵像素点的值
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		// 根据颜色数组来创建位图
		/**
		 * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图， 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
		 * config是一个枚举，可以用它来指定位图“质量”。
		 */
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bm.setPixels(pixels, 0, width, 0, 0, width, height);
		// 将生成的条形码返回给调用者
		return bm;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			mResultTv.setText(scanResult);
		}
	}
}