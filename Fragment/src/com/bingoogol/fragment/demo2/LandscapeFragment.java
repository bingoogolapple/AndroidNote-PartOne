package com.bingoogol.fragment.demo2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bingoogol.fragment.R;

/**
 * @author bingoogol@sina.com 14-2-25.
 */
public class LandscapeFragment extends Fragment {
    //     onAttach
    // onCreate
    //     onCreateView
    //     onActivityCreated
    // onStart
    // onResume
    // onPause
    // onStop
    //     onDestoryView
    // onDestory
    //     onDetch

    @Override
    public void onAttach(Activity activity) {
        Log.i("bingo", "onAttach");
        super.onAttach(activity);
    }

    /**
     * 初始化Fragment，实例化在Fragment中的成员变量
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("bingo", "onCreate");
        super.onCreate(savedInstanceState);
    }

    /**
     * 当fragment被创建的时候调用的方法，返回当前fragment显示的内容
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo2_landscape, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("bingo", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i("bingo", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("bingo", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("bingo", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("bingo", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i("bingo", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i("bingo", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i("bingo", "onDetach");
        super.onDetach();
    }
}
