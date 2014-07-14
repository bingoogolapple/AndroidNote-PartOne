package cn.bingoogol.tabhost.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class VerticalTabWidget extends TabWidget {

	public VerticalTabWidget(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
	}

	public VerticalTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
	}

	public VerticalTabWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	public void addView(View child) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		child.setLayoutParams(layoutParams);
		super.addView(child);
	}

}
