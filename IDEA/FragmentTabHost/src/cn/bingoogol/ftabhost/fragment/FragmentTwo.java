package cn.bingoogol.ftabhost.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.ftabhost.R;

public class FragmentTwo extends Fragment {
    private static final String TAG = "FragmentTwo";
    private ViewPager vp_two_fragments;
    private Fragment[] mDatas = new Fragment[]{new FragmentTwoOne(),new FragmentTwoTwo()};
    private ViewPagerAdapter mViewPagerAdapter;

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
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(TAG, "onActivityCreated");
        vp_two_fragments = (ViewPager) getView().findViewById(R.id.vp_two_fragments);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        vp_two_fragments.setAdapter(mViewPagerAdapter);
        getView().findViewById(R.id.btn_two_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_two_fragments.setCurrentItem(0);
            }
        });
        getView().findViewById(R.id.btn_two_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_two_fragments.setCurrentItem(1);
            }
        });
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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mDatas[i];
        }

        @Override
        public int getCount() {
            return mDatas.length;
        }
    }
}
