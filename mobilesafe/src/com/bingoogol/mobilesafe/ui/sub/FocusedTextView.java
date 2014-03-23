package com.bingoogol.mobilesafe.ui.sub;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author bingoogol@sina.com 14-2-11.
 */
public class FocusedTextView extends TextView {

    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        // 欺骗系统已经获取到了焦点
        return true;
    }
}
