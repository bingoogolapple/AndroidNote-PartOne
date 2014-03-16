package cn.bingoogol.ftabhost.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.bingoogol.ftabhost.R;

public class FragmentThree extends Fragment implements View.OnClickListener {
    private static final String TAG = "FragmentThree";
    private View ll_three_one;
    private View ll_three_two;
    private ListView lv_three_one;
    private ListView lv_three_two;
    private String[] mDatasOne;
    private String[] mDatasTwo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll_three_one = getView().findViewById(R.id.ll_three_one);
        ll_three_two = getView().findViewById(R.id.ll_three_two);
        lv_three_one = (ListView) getView().findViewById(R.id.lv_three_one);
        lv_three_two = (ListView) getView().findViewById(R.id.lv_three_two);
        getView().findViewById(R.id.btn_three_one).setOnClickListener(this);
        getView().findViewById(R.id.btn_three_two).setOnClickListener(this);
        Log.w(TAG, "onActivityCreated");
        initDatas();
        lv_three_one.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatasOne));
        lv_three_two.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatasTwo));

    }

    private void initDatas() {
        mDatasOne = new String[40];
        for (int i = 0; i < 40; i++) {
            mDatasOne[i] = "one " + i;
        }

        mDatasTwo = new String[35];
        for (int i = 0; i < 35; i++) {
            mDatasTwo[i] = "two " + i;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w(TAG, "onDetach");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_three_one:
                ll_three_two.setVisibility(View.VISIBLE);
                ll_three_one.setVisibility(View.GONE);
                break;
            case R.id.btn_three_two:
                ll_three_one.setVisibility(View.VISIBLE);
                ll_three_two.setVisibility(View.GONE);
                break;
        }

    }
}
