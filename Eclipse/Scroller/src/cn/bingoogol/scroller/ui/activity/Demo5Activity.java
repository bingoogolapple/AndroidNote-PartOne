package cn.bingoogol.scroller.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.bingoogol.scroller.R;
import cn.bingoogol.scroller.ui.view.ScrollLauncherViewGroup;

public class Demo5Activity extends Activity {

	private Context mContext;
	private int[] imagesArray;
	private ScrollLauncherViewGroup mScrollLauncherViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		mContext = this;
		imagesArray = new int[] { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d };
		mScrollLauncherViewGroup = new ScrollLauncherViewGroup(mContext);
		ImageView imageView = null;
		LayoutParams layoutParams = null;
		for (int i = 0; i < imagesArray.length; i++) {
			imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(imagesArray[i]);
			layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			imageView.setLayoutParams(layoutParams);
			mScrollLauncherViewGroup.addView(imageView);
		}
		setContentView(mScrollLauncherViewGroup);
	}
}
