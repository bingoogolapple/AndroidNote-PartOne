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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.bingoogol.ftabhost.R;

public class FragmentFour extends Fragment implements View.OnClickListener {
    private static final String TAG = "FragmentFour";
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FragmentFourOne mFragmentFourOne;
    private FragmentFourTwo mFragmentFourTwo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();

        if (savedInstanceState == null) {
            mFragmentFourOne = new FragmentFourOne();
            mFragmentFourTwo = new FragmentFourTwo();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.add(android.R.id.content, mFragmentFourOne, "one");
            mFragmentTransaction.addToBackStack("one");
            mFragmentTransaction.commit();
        }
        mFragmentManager.beginTransaction();
        Log.w(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        view.findViewById(R.id.btn_four_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentManager.findFragmentByTag("two") != null) {
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.hide(mFragmentFourTwo).show(mFragmentFourOne);
                    mFragmentTransaction.addToBackStack("one");
                    mFragmentTransaction.commit();
                }
            }
        });
        view.findViewById(R.id.btn_four_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                if (mFragmentManager.findFragmentByTag("two") == null) {
                    mFragmentTransaction.hide(mFragmentFourOne).add(android.R.id.content, mFragmentFourTwo, "two");
                    mFragmentTransaction.addToBackStack("two");
                } else {
                    mFragmentTransaction.hide(mFragmentFourOne).show(mFragmentFourTwo);
                    mFragmentTransaction.addToBackStack("two");
                }
                mFragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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

    }

}
