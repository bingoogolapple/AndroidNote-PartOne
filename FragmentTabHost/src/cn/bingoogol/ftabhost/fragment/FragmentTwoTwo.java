package cn.bingoogol.ftabhost.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import cn.bingoogol.ftabhost.R;

public class FragmentTwoTwo extends ListFragment {
    private static final String TAG = "FragmentTwoTwo";
    private String[] mDatas;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        Log.w(TAG, "onCreate");
    }

    private void initDatas() {
        mDatas = new String[40];
        for (int i = 0; i < 40; i++) {
            mDatas[i] = "one " + i;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_two_two, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas));
        Log.w(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
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
}
