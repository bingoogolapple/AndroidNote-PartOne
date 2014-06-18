package com.bingoogol.a4a;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bingoogol@sina.com on 14-3-14.
 */
public abstract class A4AFragment extends Fragment {
    protected View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Load any annotated fields from the bundle
        A4A.loadFieldsFromBundle(savedInstanceState, this, A4AFragment.class);

        //Load the content view using the AndroidLayout annotation
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView == null) {
            int layoutResource = A4A.getLayoutResourceByAnnotation(this, getActivity(), A4AFragment.class);
            if(layoutResource == 0){
                return null;
            }
            contentView = inflater.inflate(layoutResource, container, false);
        }
        //If we have the content view, autowire the Fragment's views
        autowireViews(contentView);
        //Callback for when autowiring is complete
        afterAutowire(savedInstanceState);
        return contentView;
    }

    protected void autowireViews(View contentView){
        A4A.autowireFragment(this, A4AFragment.class, contentView, getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        A4A.saveFieldsToBundle(outState, this, A4AFragment.class);
    }

    protected abstract void afterAutowire(Bundle savedInstanceState);
}
