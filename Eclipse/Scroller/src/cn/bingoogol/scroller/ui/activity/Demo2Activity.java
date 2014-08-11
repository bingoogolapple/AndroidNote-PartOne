package cn.bingoogol.scroller.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.bingoogol.scroller.R;

/** 
 * Demo描述: 
 * 对ViewGroup调用scrollTo()和scrollBy()方法. 
 *  
 * 验证理论: 
 * 假如一个ViewGroup(比如此处的XXXLayout)调用了scrollTo(By)() 
 * 它的Content(即它所有的子View)都会移动. 
 *  
 * 参考资料: 
 * 1 http://blog.csdn.net/wangjinyu501/article/details/32339379 
 * 2 http://blog.csdn.net/qinjuning/article/details/7247126 
 *   Thank you very much 
 *    
 * 备注说明: 
 * 使用scrollTo(By)()方法移动过程较快而且比较生硬. 
 * 为了优化scrollTo(By)()的滑动过程可采用Scroller类. 
 * 该类源码第一句This class encapsulates scrolling. 
 * 就指明了该类的目的:封装了滑动过程. 
 * 在后面的示例中,将学习到Scroller的使用. 
 */  
public class Demo2Activity extends Activity {
	private LinearLayout mLinearLayout;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo2);
		init();
	}

	private void init() {
		mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mLinearLayout.scrollBy(-50, 0);
			}
		});
	}
}
