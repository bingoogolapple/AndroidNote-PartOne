package cn.bingoogol.actionbar.ui.view;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import cn.bingoogol.actionbar.R;

// 继承自v4包中的ActionProvider
public class PlusActionProvider extends ActionProvider {
	private static final String TAG = PlusActionProvider.class.getSimpleName();
	private Context mContext;

	public PlusActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add(mContext.getString(R.string.actionbar_camera)).setIcon(R.drawable.actionbar_camera).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Log.i(TAG, mContext.getString(R.string.actionbar_camera));
				return true;
			}
		});
		subMenu.add(mContext.getString(R.string.actionbar_photo)).setIcon(R.drawable.actionbar_photo).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Log.i(TAG, mContext.getString(R.string.actionbar_camera));
				return false;
			}
		});
	}

	@Override
	public boolean hasSubMenu() {
		// 为了表示这个Action Provider是有子菜单的，需要重写hasSubMenu()方法并返回true
		return true;
	}

}
