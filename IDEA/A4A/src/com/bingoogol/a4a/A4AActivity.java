package com.bingoogol.a4a;

import android.app.Activity;
import android.os.Bundle;

public abstract class A4AActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        A4A.loadFieldsFromBundle(savedInstanceState, this, A4AActivity.class);

        int layoutId = A4A.getLayoutResourceByAnnotation(this, this, A4AActivity.class);
        if (layoutId == 0) {
            return;
        }
        setContentView(layoutId);
        afterAutowire(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        A4A.autowire(this, A4AActivity.class);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        A4A.saveFieldsToBundle(outState, this, A4AActivity.class);
    }

    protected abstract void afterAutowire(Bundle savedInstanceState);
}