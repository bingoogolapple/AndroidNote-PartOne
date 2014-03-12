package com.bingoogol.fragment.demo1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bingoogol.fragment.R;

/**
 * @author bingoogol@sina.com 14-2-25.
 */
public class RightFragment extends Fragment {
    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo1_right, null);
        tv = (TextView) view.findViewById(R.id.test_tv);
        return view;
    }

    public void setText(String text) {
        tv.setText(text);
    }
}
