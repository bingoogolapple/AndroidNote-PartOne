package cn.bingoogol.tabhost.ui.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.listener.TabListener;
import cn.bingoogol.tabhost.ui.fragment.AlbumFragment;
import cn.bingoogol.tabhost.ui.fragment.ArtistFragment;
import cn.bingoogol.tabhost.util.Logger;

public class ABFourActivity extends BaseActivity {
	private static final String TAG = ABFourActivity.class.getSimpleName();

	@Override
	protected void initView(Bundle savedInstanceState) {
		setTitle("标题4");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = actionBar.newTab().setText("artist").setTabListener(new TabListener<ArtistFragment>(this, "artist", ArtistFragment.class));
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("album").setTabListener(new TabListener<AlbumFragment>(this, "album", AlbumFragment.class));
		actionBar.addTab(tab);
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_abfour, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy");
	}
}
