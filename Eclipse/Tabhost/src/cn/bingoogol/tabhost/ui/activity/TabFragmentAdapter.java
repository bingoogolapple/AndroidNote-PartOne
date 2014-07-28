package cn.bingoogol.tabhost.ui.activity;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.bingoogol.tabhost.R;

public class TabFragmentAdapter implements OnCheckedChangeListener {
	private OnTabChangerListener mOnTabChangerListener;
	private FragmentManager mFragmentManager;
	private List<Fragment> mFragments;
	private RadioGroup mTabRg;
	private FragmentActivity mActivity;
	private int mContentId;
	private int mCurrentCheckedId;
	private Fragment mCurrentFragment;

	public TabFragmentAdapter(FragmentActivity activity, int contentId, List<Fragment> fragments, RadioGroup tabRg) {
		mActivity = activity;
		mFragmentManager = mActivity.getSupportFragmentManager();
		mContentId = contentId;
		mFragments = fragments;
		mTabRg = tabRg;

		// 默认第一个被选中
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		RadioButton radioButton = (RadioButton) mTabRg.getChildAt(0);
		radioButton.setChecked(true);
		mCurrentCheckedId = radioButton.getId();
		mCurrentFragment = mFragments.get(0);
		ft.add(R.id.realtabcontent, mCurrentFragment);
		ft.commit();

		mTabRg.setOnCheckedChangeListener(this);
	}

	public void setOnTabChangeListener(OnTabChangerListener onTabChangerListener) {
		mOnTabChangerListener = onTabChangerListener;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (mOnTabChangerListener != null && mOnTabChangerListener.onBeforeChange(mCurrentCheckedId, checkedId)) {
			((RadioButton) mTabRg.findViewById(mCurrentCheckedId)).setChecked(true);
			return;
		} else {
			for (int tabId = 0; tabId < mTabRg.getChildCount(); tabId++) {
				if (mTabRg.getChildAt(tabId).getId() == checkedId) {
					// 获取新的Fragment
					Fragment newFragment = mFragments.get(tabId);
					FragmentTransaction ft = mFragmentManager.beginTransaction();

					// 如果没有添加，则先添加，接下来再show出来
					if (!newFragment.isAdded()) {
						ft.add(mContentId, newFragment);
					}

					if (!mCurrentFragment.equals(newFragment)) {
						ft.hide(mCurrentFragment);
						mCurrentFragment.onPause();

						mCurrentFragment = newFragment;
						ft.show(newFragment);
						mCurrentFragment.onResume();

						mCurrentCheckedId = checkedId;
					}
					ft.commit();
					return;
				}
			}
		}
	}

	public interface OnTabChangerListener {
		/**
		 * 在切换tab之前检查是否切换tab
		 * 
		 * @param oldCheckedId
		 * @param newCheckedId
		 * @return 如果阻止此次切换则返回true，否则返回false
		 */
		public boolean onBeforeChange(int oldCheckedId, int newCheckedId);
	}
}