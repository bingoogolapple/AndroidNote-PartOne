package cn.bingoogol.viewpager.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.bingoogol.viewpager.R;

public class BingoViewPager extends RelativeLayout {
	private static final int RMP = RelativeLayout.LayoutParams.MATCH_PARENT;
	private static final int RWC = RelativeLayout.LayoutParams.WRAP_CONTENT;
	private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private ViewPager mViewPager = null;
	private List<View> mViews = null;
	private LinearLayout mPointContainer = null;
	private List<ImageView> mPoints = null;
	private boolean mPointVisibility = true;
	private boolean mAutoPlayAble = false;
	private boolean mIsAutoPlaying = true;
	private int mAutoPlayInterval = 2000;
	private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
	private int mPointSpacing = 15;
	private int mPointEdgeSpacing = 15;
	private int mPointContainerWidth = RMP;
	private int mPointContainerHeight = RWC;
	private Drawable mPointFocusedDrawable = null;
	private Drawable mPointUnfocusedDrawable = null;
	private Drawable mPointContainerBackgroundDrawable = null;
	private Handler mPagerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mViewPager.setCurrentItem(msg.what);
		};
	};
	private Timer mAutoPlayTimer = null;

	public BingoViewPager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BingoViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
		initView(context);
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
				break;
			case R.styleable.BingoViewPager_pointContainerBackground:
				mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);
				break;
			case R.styleable.BingoViewPager_pointSpacing:
				/**
				 * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int. getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
				 */
				mPointSpacing = typedArray.getDimensionPixelSize(attr, mPointSpacing);
				break;
			case R.styleable.BingoViewPager_pointEdgeSpacing:
				mPointEdgeSpacing = typedArray.getDimensionPixelSize(attr, mPointEdgeSpacing);
				break;
			case R.styleable.BingoViewPager_pointGravity:
				mPointGravity = typedArray.getInt(attr, mPointGravity);
				break;
			case R.styleable.BingoViewPager_pointContainerWidth:
				try {
					mPointContainerWidth = typedArray.getDimensionPixelSize(attr, mPointContainerWidth);
				} catch (UnsupportedOperationException e) {
					// 如果是指定的wrap_content或者match_parent会执行下面这一行
					mPointContainerWidth = typedArray.getInt(attr, mPointContainerWidth);
				}
				break;
			case R.styleable.BingoViewPager_pointContainerHeight:
				try {
					mPointContainerHeight = typedArray.getDimensionPixelSize(attr, mPointContainerHeight);
				} catch (UnsupportedOperationException e) {
					mPointContainerHeight = typedArray.getInt(attr, mPointContainerHeight);
				}
				break;
			case R.styleable.BingoViewPager_pointVisibility:
				mPointVisibility = typedArray.getBoolean(attr, mPointVisibility);
				break;
			case R.styleable.BingoViewPager_pointAutoPlayAble:
				mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);
				break;
			case R.styleable.BingoViewPager_pointAutoPlayInterval:
				mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
				break;
			}
		}
		typedArray.recycle();
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		mViewPager = new ViewPager(context);
		addView(mViewPager, new RelativeLayout.LayoutParams(RMP, RMP));

		if (mPointVisibility) {
			mPointContainer = new LinearLayout(context);
			mPointContainer.setOrientation(LinearLayout.HORIZONTAL);
			mPointContainer.setPadding(mPointEdgeSpacing, 0, mPointEdgeSpacing, 0);
			if (mPointContainerBackgroundDrawable != null) {
				mPointContainer.setBackgroundDrawable(mPointContainerBackgroundDrawable);
			}
			RelativeLayout.LayoutParams pointContainerLp = new RelativeLayout.LayoutParams(mPointContainerWidth, mPointContainerHeight);
			// 处理圆点在顶部还是底部
			if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
				pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			} else {
				pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			}
			int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
			// 处理圆点在左边、右边还是水平居中
			if (horizontalGravity == Gravity.LEFT) {
				mPointContainer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			} else if (horizontalGravity == Gravity.RIGHT) {
				mPointContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			} else {
				mPointContainer.setGravity(Gravity.CENTER);
			}
			addView(mPointContainer, pointContainerLp);
		}
	}

	public void setViewPagerViews(List<View> views) {
		mViews = views;
		mViewPager.setAdapter(new MyAdapter());
		mViewPager.setOnPageChangeListener(new MyListener());
		if (mPointVisibility) {
			setPoints();
			processAutoPlay();
		}
	}

	private void setPoints() {
		if (mPoints != null) {
			mPoints.clear();
			mViewPager.removeAllViews();
		} else {
			mPoints = new ArrayList<ImageView>();
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
		int margin = mPointSpacing / 2;
		lp.setMargins(margin, 0, margin, 0);
		ImageView imageView;
		for (int i = 0; i < mViews.size(); i++) {
			imageView = new ImageView(getContext());
			imageView.setLayoutParams(lp);
			mPoints.add(imageView);
			mPointContainer.addView(imageView);
		}
		setCurrentPoint(0);
	}

	private void processAutoPlay() {
		if (mAutoPlayAble) {
			mViewPager.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						if (mIsAutoPlaying) {
							stopAutoPlay();
						}
						break;
					case MotionEvent.ACTION_UP:
						if (mAutoPlayAble && !mIsAutoPlaying) {
							startAutoPlay();
						}
						break;
					}
					return false;
				}
			});
		}
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (mAutoPlayAble) {
			if (visibility == VISIBLE) {
				startAutoPlay();
			} else if (visibility == INVISIBLE) {
				stopAutoPlay();
			}
		}
	}

	private void startAutoPlay() {
		mIsAutoPlaying = true;
		mAutoPlayTimer = new Timer();
		mAutoPlayTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// mViewPager.getChildCount() 获取到的是当前被加载的子控件个数，并不等于mViews.size()
				mPagerHandler.sendEmptyMessage((mViewPager.getCurrentItem() + 1) % mViews.size());
			}
		}, mAutoPlayInterval, mAutoPlayInterval);
	}

	private void stopAutoPlay() {
		if (mAutoPlayTimer != null) {
			mIsAutoPlaying = false;
			mAutoPlayTimer.cancel();
			mAutoPlayTimer = null;
		}
	}

	private void setCurrentPoint(int position) {
		if (mPointFocusedDrawable == null) {
			throw new RuntimeException("pointFocusedImg is not allowed to be NULL");
		} else if (mPointUnfocusedDrawable == null) {
			throw new RuntimeException("pointUnfocusedImg is not allowed to be NULL");
		}
		mPoints.get(position).setImageDrawable(mPointFocusedDrawable);
		for (int i = 0; i < mViews.size(); i++) {
			if (position != i) {
				mPoints.get(i).setImageDrawable(mPointUnfocusedDrawable);
			}
		}
	}

	private final class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// 获取ViewPager的个数，这个方法是必须实现的
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// container容器就是ViewPager, position指的是ViewPager的索引
			// 从View集合中获取对应索引的元素, 并添加到ViewPager中
			((ViewPager) container).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// 从ViewPager中删除集合中对应索引的View对象
			((ViewPager) container).removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// view 要关联的页面, object instantiateItem()方法返回的对象
			// 是否要关联显示页面与 instantiateItem()返回值，这个方法是必须实现的
			return view == object;
		}
	}

	private final class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				// 开始滑动
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				// 当松开手时
				// 如果没有其他页显示出来：SCROLL_STATE_DRAGGING --> SCROLL_STATE_IDLE
				// 如果有其他页有显示出来（不管显示了多少），就会触发正在设置页码
				// 页码没有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> SCROLL_STATE_IDLE
				// 页码有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> onPageSelected --> SCROLL_STATE_IDLE
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				// 停止滑动
				break;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// Logger.i(TAG, "onPageScrolled:  position=" + position + "  positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			if (mPointVisibility) {
				setCurrentPoint(position);
			}
		}
	}

}