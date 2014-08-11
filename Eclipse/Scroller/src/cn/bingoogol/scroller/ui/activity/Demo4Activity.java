package cn.bingoogol.scroller.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import cn.bingoogol.scroller.R;

/** 
 * Demo描述: 
 * 实现可以拉动后回弹的布局. 
 * 类似于下拉刷新的. 
 * 
 * 参考资料: 
 * 1 http://gundumw100.iteye.com/blog/1884373 
 * 2 http://blog.csdn.net/gemmem/article/details/7321910 
 * 3 http://ipjmc.iteye.com/blog/1615828 
 * 4 http://blog.csdn.net/c_weibin/article/details/7438323 
 * 5 http://www.cnblogs.com/wanqieddy/archive/2012/05/05/2484534.html 
 * 6 http://blog.csdn.net/hudashi/article/details/7353075 
 *   Thank you very much 
 */ 
public class Demo4Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo4);
	}
}
