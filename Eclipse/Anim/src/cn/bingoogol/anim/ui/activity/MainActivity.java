package cn.bingoogol.anim.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import cn.bingoogol.anim.R;
import cn.bingoogol.anim.ui.fragment.AlphaFragment;
import cn.bingoogol.anim.ui.fragment.ComplexFragment;
import cn.bingoogol.anim.ui.fragment.FlipFragment;
import cn.bingoogol.anim.ui.fragment.FrameFragment;
import cn.bingoogol.anim.ui.fragment.RoateFragment;
import cn.bingoogol.anim.ui.fragment.ScaleFragment;
import cn.bingoogol.anim.ui.fragment.ShakeFragment;
import cn.bingoogol.anim.ui.fragment.TranslateFragment;

public class MainActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.btn_main_shake).setOnClickListener(this);
		findViewById(R.id.btn_main_flip).setOnClickListener(this);
		findViewById(R.id.btn_main_alpha).setOnClickListener(this);
		findViewById(R.id.btn_main_scale).setOnClickListener(this);
		findViewById(R.id.btn_main_translate).setOnClickListener(this);
		findViewById(R.id.btn_main_rotate).setOnClickListener(this);
		findViewById(R.id.btn_main_frame).setOnClickListener(this);
		findViewById(R.id.btn_main_complex).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_shake:
			changeContent(new ShakeFragment());
			break;
		case R.id.btn_main_flip:
			changeContent(new FlipFragment());
			break;
		case R.id.btn_main_alpha:
			changeContent(new AlphaFragment());
			break;
		case R.id.btn_main_scale:
			changeContent(new ScaleFragment());
			break;
		case R.id.btn_main_translate:
			changeContent(new TranslateFragment());
			break;
		case R.id.btn_main_rotate:
			changeContent(new RoateFragment());
			break;
		case R.id.btn_main_frame:
			changeContent(new FrameFragment());
			break;
		case R.id.btn_main_complex:
			changeContent(new ComplexFragment());
			break;

		default:
			break;
		}
	}

	private void changeContent(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_container, fragment).commit();
	}

}
