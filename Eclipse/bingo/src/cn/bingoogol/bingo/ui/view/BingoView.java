package cn.bingoogol.bingo.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import cn.bingoogol.bingo.R;

public class BingoView extends View {

	public BingoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BingoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		initAttrs(context, attrs);
	}

	private void initView(Context context) {
		//View view = View.inflate(context, R.layout., this);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BingoView);
		final int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.BingoView_test:
				@SuppressWarnings("unused")
				String test = typedArray.getText(attr).toString();
				break;
			}
		}
		typedArray.recycle();
	}
}
