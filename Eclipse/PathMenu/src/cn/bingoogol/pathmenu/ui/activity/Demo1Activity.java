package cn.bingoogol.pathmenu.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bingoogol.pathmenu.R;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class Demo1Activity extends Activity implements OnClickListener {
	private static final String TAG = Demo1Activity.class.getSimpleName();
	private Button mMenuButton;
	private Button mItemButton1;
	private Button mItemButton2;
	private Button mItemButton3;
	private Button mItemButton4;
	private Button mItemButton5;

	private boolean mIsMenuOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo1);
		mMenuButton = (Button) findViewById(R.id.menu);
		mMenuButton.setOnClickListener(this);

		mItemButton1 = (Button) findViewById(R.id.item1);
		mItemButton1.setOnClickListener(this);

		mItemButton2 = (Button) findViewById(R.id.item2);
		mItemButton2.setOnClickListener(this);

		mItemButton3 = (Button) findViewById(R.id.item3);
		mItemButton3.setOnClickListener(this);

		mItemButton4 = (Button) findViewById(R.id.item4);
		mItemButton4.setOnClickListener(this);

		mItemButton5 = (Button) findViewById(R.id.item5);
		mItemButton5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!mIsMenuOpen) {
			mIsMenuOpen = true;
			ObjectAnimator.ofFloat(mMenuButton, "rotation", 0, 360).setDuration(500).start();
//			ObjectAnimator.ofFloat(mMenuButton, "rotationY", 0, 360).setDuration(500).start();
			doAnimateOpen(mItemButton1, 0, 5, 500);
			doAnimateOpen(mItemButton2, 1, 5, 500);
			doAnimateOpen(mItemButton3, 2, 5, 500);
			doAnimateOpen(mItemButton4, 3, 5, 500);
			doAnimateOpen(mItemButton5, 4, 5, 500);
		} else {
			mIsMenuOpen = false;
			ObjectAnimator.ofFloat(mMenuButton, "rotationX", 360, 0).setDuration(500).start();
			ObjectAnimator.ofFloat(mMenuButton, "rotationY", 360, 0).setDuration(500).start();
			doAnimateClose(mItemButton1, 0, 5, 500);
			doAnimateClose(mItemButton2, 1, 5, 500);
			doAnimateClose(mItemButton3, 2, 5, 500);
			doAnimateClose(mItemButton4, 3, 5, 500);
			doAnimateClose(mItemButton5, 4, 5, 500);
		}
	}

	/**
	 * 打开菜单的动画
	 * 
	 * @param view
	 *            执行动画的view
	 * @param index
	 *            view在动画序列中的顺序
	 * @param total
	 *            动画序列的个数
	 * @param radius
	 *            动画半径
	 */
	private void doAnimateOpen(View view, int index, int total, int radius) {
		if (view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
		double degree = Math.PI * index / ((total - 1) * 2);
		int translationX = (int) (radius * Math.cos(degree));
		int translationY = (int) (radius * Math.sin(degree));
		Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d", degree, translationX, translationY));
		AnimatorSet set = new AnimatorSet();
		// 包含平移、缩放和透明度动画
		set.playTogether(ObjectAnimator.ofFloat(view, "translationX", 0, translationX), ObjectAnimator.ofFloat(view, "translationY", 0, translationY), ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f), ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f), ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
		set.setStartDelay(index * 100 / (total - 1));
		// 动画周期为500ms
		set.setDuration(1 * 500).start();
	}

	/**
	 * 关闭菜单的动画
	 * 
	 * @param view
	 *            执行动画的view
	 * @param index
	 *            view在动画序列中的顺序
	 * @param total
	 *            动画序列的个数
	 * @param radius
	 *            动画半径
	 */
	private void doAnimateClose(final View view, int index, int total, int radius) {
		if (view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
		double degree = Math.PI * index / ((total - 1) * 2);
		int translationX = (int) (radius * Math.cos(degree));
		int translationY = (int) (radius * Math.sin(degree));
		Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d", degree, translationX, translationY));
		AnimatorSet set = new AnimatorSet();

		// 包含平移、缩放和透明度动画
		set.playTogether(ObjectAnimator.ofFloat(view, "translationX", translationX, 0), ObjectAnimator.ofFloat(view, "translationY", translationY, 0), ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f), ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f), ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));
		// 为动画加上事件监听，当动画结束的时候，我们把当前view隐藏
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				view.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}
		});
		set.setStartDelay((total - index) * 100 / (total - 1));
		set.setDuration(1 * 500).start();
	}
}