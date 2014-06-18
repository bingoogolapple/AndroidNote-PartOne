package cn.bingoogol.ftabhost.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bingoogol.ftabhost.R;

/**
 * Created by bingoogol@sina.com on 14-3-16.
 */
public class FragmentFourOne extends Fragment {
    private static final String TAG = "FragmentFourOne";

    private int flag = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, "onAttach:" + activity.getClass().getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            flag = savedInstanceState.getInt("flag");
            Log.w(TAG, "one恢复状态：" + flag);
        } else {
            Log.w(TAG, "one没有恢复状态");
        }
        Log.w(TAG, "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.w(TAG, "保存状态" + ++flag);
        outState.putInt("one 保存状态:", flag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_four_one, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_four_one = (TextView) getView().findViewById(R.id.tv_four_one);
        tv_four_one.setText("four one " + flag);
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
