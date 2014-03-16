package cn.bingoogol.ftabhost.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.ftabhost.R;

public class FragmentOne extends Fragment {
    private static final String TAG = "FragmentOne";
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FragmentOneOne mFragmentOneOne;
    private FragmentOneTwo mFragmentOneTwo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();

        if(savedInstanceState == null) {
            mFragmentOneOne = new FragmentOneOne();
            mFragmentOneTwo = new FragmentOneTwo();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.add(R.id.fl_one_content,mFragmentOneOne);
            mFragmentTransaction.commit();
        }
        mFragmentManager.beginTransaction();
        Log.w(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        view.findViewById(R.id.btn_one_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fl_one_content,mFragmentOneOne);
                mFragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.btn_one_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fl_one_content,mFragmentOneTwo);
                mFragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(TAG, "onActivityCreated");

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
}