package cn.bingoogol.viewpager.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.util.DensityUtil;

public class BingoViewPager extends RelativeLayout {
	private ViewPager mViewPager;
	private LinearLayout mPointContainer;
	private ImageView[] mPoints;
	private float mPointSpacing;
	private Drawable mPointFocusedDrawable;
	private Drawable mPointUnfocusedDrawable;
	private int mPageCount = 3;

	public BingoViewPager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BingoViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		initAttrs(context, attrs);
	}

	private void initView(Context context) {
		// View view = View.inflate(context, R.layout., this);
		mViewPager = new ViewPager(context);
		mPointContainer = new LinearLayout(context);
		addView(mViewPager, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		RelativeLayout.LayoutParams pointContainerLp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		pointContainerLp.setMargins(0, 0, 0, 20);
		addView(mPointContainer, pointContainerLp);

		initPoints(context);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BingoViewPager);
		final int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.BingoViewPager_pointFocusedImg:
				mPointFocusedDrawable = typedArray.getDrawable(attr);
				break;
			case R.styleable.BingoViewPager_pointUnfocusedImg:
				mPointUnfocusedDrawable = typedArray.getDrawable(attr);
			case R.styleable.BingoViewPager_pointSpacing:
				final float scale = context.getResources().getDisplayMetrics().density;
				mPointSpacing = (typedArray.getDimension(attr, 2.0f) * scale + 0.5f);
				break;
			}
		}
		typedArray.recycle();
	}

	private void initPoints(Context context) {
		mPoints = new ImageView[mPageCount];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		int margin = DensityUtil.dip2px(mPointSpacing);
		lp.setMargins(margin, 0, margin, 0);
		ImageView imageView;
		for (int i = 0; i < mPageCount; i++) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(lp);
			mPoints[i] = imageView;
			mPointContainer.addView(imageView);
		}
		setCurrentPoint(0);
	}

	private void setCurrentPoint(int position) {
		mPoints[position].setImageDrawable(mPointFocusedDrawable);
		for (int i = 0; i < mPoints.length; i++) {
			if (position != i) {
				mPoints[i].setImageDrawable(mPointUnfocusedDrawable);
			}
		}
	}

}