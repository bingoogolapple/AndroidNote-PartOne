package com.bingoogol.fragment.demo1;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bingoogol.fragment.R;

/**
 * @author bingoogol@sina.com 14-2-25.
 */
public class LeftFragment extends Fragment {

    /**
     * 当fragment被创建的时候调用的方法，返回当前fragment显示的内容
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("bingo", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_demo1_left, null);
        Button button = (Button) view.findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                RightFragment rightFragment = (RightFragment) getActivity().getFragmentManager().findFragmentById(R.id.right_fragment);
                RightFragment rightFragment = (RightFragment) getFragmentManager().findFragmentById(R.id.right_fragment);
                //在activity中通过tag的方式添加，此处才能通过tag查找
                //getFragmentManager().findFragmentByTag("");
                rightFragment.setText("内容变化了");
//				TextView testTv = (TextView) rightFragment.getView().findViewById(R.id.test_tv);
//				testTv.setText("内容变化了");
            }
        });
        return view;
    }
}
