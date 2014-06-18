package com.bingoogol.socketclient.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.bingoogol.socketclient.R;

import java.util.Date;

public class PTRListView extends ListView implements OnScrollListener {

	private int firstVisibleIndex;// listView在页面中第一个看见item的位置
	private ImageView arrow;
	private View headView;
	private ProgressBar progressBar;
	private TextView title;
	private TextView last_update;
	private int headcontentHeight;

	private static final int PULL_TO_REFRESH = 0;
	private static final int RELEASE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;
	private int state;// 当前下拉刷新控件的状态
	private OnRefreshListener refreshListener;// 刷新监听器
	private boolean isRefreshable;// 记录是否能够刷新
	private boolean isRecored;// 记录startY 起始位置 在整个滑动过程中，只记录一次
	private float startY;
	private boolean isBack;
	private Animation animation;
	private Animation receverAnimation;

	private static final int RATIO = 3;// 实际距离于 界面上距离之间的比例值

	public PTRListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PTRListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// 要在adapter中使用 ，在当前使用会报错 空指针
		// View headView = View.inflate(context, R.layout.header, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		headView = inflater.inflate(R.layout.header, null);
		arrow = (ImageView) headView.findViewById(R.id.arrow);
		progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);
		title = (TextView) headView.findViewById(R.id.title);
		last_update = (TextView) headView.findViewById(R.id.last_update);

		arrow.setMinimumWidth(70);
		arrow.setMinimumHeight(50);

		measureView(headView);

		headcontentHeight = headView.getMeasuredHeight();

		// 设置headView 与界面上边距的距离
		headView.setPadding(0, -1 * headcontentHeight, 0, 0);

		headView.invalidate();// headView 重绘

		addHeaderView(headView);

		setOnScrollListener(this);

		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(250);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());// 设置动画速度
		/*
		 * Interpolator 定义了动画的变化速度，可以实现匀速、正加速、负加速、无规则变加速等；
		 * 
		 * AccelerateDecelerateInterpolator，延迟减速，在动作执行到中间的时候才执行该特效。 AccelerateInterpolator, 会使慢慢以(float)的参数降低速度。 LinearInterpolator，平稳不变的 DecelerateInterpolator，在中间加速,两头慢 CycleInterpolator，曲线运动特效，要传递float型的参数。
		 */

		receverAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		receverAnimation.setDuration(200);
		receverAnimation.setFillAfter(true);
		receverAnimation.setInterpolator(new LinearInterpolator());// 设置动画速度 匀速

		state = DONE;
		isRefreshable = false;
	}

	// 测量Headview 宽高
	private void measureView(View child) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int childMeasureHeight;
		if (lp.height > 0) {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);// 适合、匹配
		} else {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);// 未指定
		}

		child.measure(childMeasureWidth, childMeasureHeight);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstVisibleIndex = firstVisibleItem;
	}

	public interface OnRefreshListener {
		abstract void onRefresh();
	}

	// 提供一个对外访问的刷新方法
	public void setOnRefreshListener(OnRefreshListener listener) {
		refreshListener = listener;
		isRefreshable = true;
	}

	// 数据刷新完成后执行方法
	// 下拉刷新 模式的改变 时间的更新
	public void onRefreshComplete() {
		// TODO Auto-generated method stub
		state = DONE;
		changeHeadViewOfState();

		last_update.setText("最近更新： " + new Date().toLocaleString());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstVisibleIndex == 0 && !isRecored) {
					startY = event.getY();
					isRecored = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float tempY = event.getY();
				if (firstVisibleIndex == 0 && !isRecored) {
					startY = tempY;
					isRecored = true;
				}

				if (state != REFRESHING) {
					if (state == PULL_TO_REFRESH) {
						if ((tempY - startY) / RATIO > headcontentHeight && tempY - startY > 0) {
							// 下啦刷新 --》 松开刷新
							state = RELEASE_TO_REFRESH;
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// 下啦刷新 --》 回到 刷新完成
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == RELEASE_TO_REFRESH) {
						if ((tempY - startY) / RATIO < headcontentHeight && tempY - startY > 0) {
							// 松开刷新 --》回到下拉刷新
							state = PULL_TO_REFRESH;
							isBack = true;// 从松开刷新 回到的下拉刷新
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// 松开刷新 --》 回到 刷新完成
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							// 刷新完成 --》 进入 下拉刷新
							state = PULL_TO_REFRESH;
							changeHeadViewOfState();
						}
					}

					if (state == PULL_TO_REFRESH || state == RELEASE_TO_REFRESH) {
						headView.setPadding(0, (int) ((tempY - startY) / RATIO - headcontentHeight), 0, 0);
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					if (state == DONE) {
						// 不需要处理
					}
					if (state == PULL_TO_REFRESH) {
						state = DONE;
						changeHeadViewOfState();
					}

					if (state == RELEASE_TO_REFRESH) {
						state = REFRESHING;
						changeHeadViewOfState();
						onRefresh();// 刷新 得到服务器数据
					}
				}
				break;
			}

		}
		return super.onTouchEvent(event);
	}

	// headview状态改变
	private void changeHeadViewOfState() {
		switch (state) {
		case PULL_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);

			title.setText("下拉刷新");
			arrow.clearAnimation();

			if (isBack) {// 从松开刷新 回到 下拉刷新
				arrow.startAnimation(animation);
				isBack = false;
			}
			break;
		case RELEASE_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);
			title.setText("松开刷新");
			arrow.clearAnimation();
			arrow.startAnimation(receverAnimation);

			break;
		case REFRESHING:
			arrow.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);

			title.setText("正在刷新中...");
			arrow.clearAnimation();

			headView.setPadding(0, 0, 0, 0);
			break;
		case DONE:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);

			title.setText("下拉刷新");
			arrow.clearAnimation();

			headView.setPadding(0, -1 * headcontentHeight, 0, 0);
			break;

		}
	}

	// 刷新 得到服务器的数据
	private void onRefresh() {
		// TODO Auto-generated method stub
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		last_update.setText("最近更新： " + new Date().toLocaleString());
		super.setAdapter(adapter);
	}
}