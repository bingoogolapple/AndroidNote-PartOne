package cn.bingoogol.anim.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import cn.bingoogol.anim.R;
import cn.bingoogol.anim.ui.view.MatrixOneImageView;
import cn.bingoogol.anim.ui.view.MatrixOneImageView.OnViewClickListener;
import cn.bingoogol.anim.util.ToastUtil;

public class MatrixFragment extends BaseFragment {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_matrix);
	}

	@Override
	protected void setListener() {
		MatrixOneImageView matrixOneImageView = (MatrixOneImageView) mRootView.findViewById(R.id.moiv_matrix);
		matrixOneImageView.setOnViewClickListener(new OnViewClickListener() {
			
			@Override
			public void onViewClick(MatrixOneImageView view) {
				ToastUtil.makeText("点击了MatrixOneImageView");
			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
