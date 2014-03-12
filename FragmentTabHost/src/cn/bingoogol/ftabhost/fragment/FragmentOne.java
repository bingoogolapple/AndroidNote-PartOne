package cn.bingoogol.ftabhost.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import cn.bingoogol.ftabhost.R;

public class FragmentOne extends ListFragment {
	private static final String TAG = "FragmentOne";
	private String[] arr = new String[50];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_one, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated");
		
		for (int i = 0; i < 50; i++) {
			arr[i] = "条目" + i;
		}

		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr));
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), arr[position], Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}

}
