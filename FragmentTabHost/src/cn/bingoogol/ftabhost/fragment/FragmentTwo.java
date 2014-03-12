package cn.bingoogol.ftabhost.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.ftabhost.R;

public class FragmentTwo extends Fragment {
	private static final String TAG = "FragmentTwo";
    private PTRListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_two, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}

}
