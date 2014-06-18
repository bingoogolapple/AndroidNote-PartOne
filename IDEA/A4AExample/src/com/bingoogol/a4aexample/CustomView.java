package com.bingoogol.a4aexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bingoogol.a4a.A4A;
import com.bingoogol.a4a.A4AView;

import java.util.jar.Attributes;


public class CustomView extends RelativeLayout {
    @A4AView(R.id.title)
    private TextView title;

    @A4AView(R.id.icon)
    private ImageView icon;
    public CustomView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custome_view, this);
        A4A.autowireView(this, CustomView.class, context);
        title.setText("标题");
        icon.setBackgroundResource(R.drawable.ic_launcher);
    }

    public CustomView(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custome_view, this);
        A4A.autowireView(this, CustomView.class, context);
        title.setText("标题");
        icon.setBackgroundResource(R.drawable.ic_launcher);
    }
}
