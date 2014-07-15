package cn.bingoogol.tabhost.ui.view;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.ToastUtil;

public class MyActionProvider extends ActionProvider {

	public MyActionProvider(Context context) {
		super(context);
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add("子菜单1").setIcon(R.drawable.selector_tab_one).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				ToastUtil.makeText("点击子菜单1");
				return true;
			}
		});
		subMenu.add("子菜单2").setIcon(R.drawable.selector_tab_two).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				ToastUtil.makeText("点击子菜单2");
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
