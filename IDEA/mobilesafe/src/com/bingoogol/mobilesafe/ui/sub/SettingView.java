package com.bingoogol.mobilesafe.ui.sub;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bingoogol.mobilesafe.R;

/**
 * @author bingoogol@sina.com 14-2-16.
 */
public class SettingView  extends RelativeLayout {
    private String content_on;
    private String content_off;

    private TextView tv_title;
    private TextView tv_content;
    private CheckBox cb;

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SettingView);
        String title = a.getString(R.styleable.SettingView_title);
        content_on = a.getString(R.styleable.SettingView_content_on);
        content_off = a.getString(R.styleable.SettingView_content_off);
        tv_title.setText(title);
        if (isChecked()) {
            tv_content.setText(content_on);
        } else {
            tv_content.setText(content_off);
        }
        a.recycle();
    }

    public SettingView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.setting_view, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        cb = (CheckBox) view.findViewById(R.id.cb_status);
    }

    /**
     *
     */
    public boolean isChecked() {
        return cb.isChecked();
    }

    /**
     *
     */
    public void setChecked(boolean checked) {
        cb.setChecked(checked);
        if (checked) {
            tv_content.setTextColor(Color.BLACK);
            tv_content.setText(content_on);
        } else {
            tv_content.setText(content_off);
            tv_content.setTextColor(Color.RED);
        }
    }



    public void setOnclickedListener(CompoundButton.OnCheckedChangeListener listener){
        cb.setOnCheckedChangeListener(listener);
    }
   }