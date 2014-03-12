package com.bingoogol.resource.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.bingoogol.resource.R;

public class HomeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    private ImageView iv_animation;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_animation = (ImageView) this.findViewById(R.id.iv_animation);
        iv_animation.setOnClickListener(this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        Log.i(TAG, item.getTitle().toString());
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_animation:
                Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
                iv_animation.startAnimation(hyperspaceJump);

                break;
        }
    }
}
