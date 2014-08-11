package cn.bingoogol.scroller.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bingoogol.scroller.R;
import cn.bingoogol.scroller.ui.view.LinearLayoutSubClass;

/** 
 * Demo描述: 
 * Scroller使用示例——让控件平移划过屏幕 
 *  
 * 参考资料: 
 * http://blog.csdn.net/c_weibin/article/details/7438323 
 * Thank you very much 
 *  
 * 注意事项: 
 * 1 在布局中将cc.cn.LinearLayoutSubClass的控件的宽度设置为"fill_parent" 
 *   便于观察滑动的效果 
 */  
public class Demo3Activity extends Activity {
	private Button mButton;
	private LinearLayoutSubClass mLinearLayoutSubClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo3);
		init();
	}

	private void init() {
		mLinearLayoutSubClass = (LinearLayoutSubClass) findViewById(R.id.linearLayoutSubClass);
		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mLinearLayoutSubClass.beginScroll();
			}
		});
	}
}
