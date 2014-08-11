package cn.bingoogol.scroller.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/** 
 * Scroller原理: 
 * 为了让View或者ViewGroup的内容发生移动,我们常用scrollTo()和scrollBy()方法. 
 * 但这两个方法执行的速度都很快,瞬间完成了移动感觉比较生硬. 
 * 为了使View或者ViewGroup的内容发生移动时比较平滑或者有其他的移动渐变效果 
 * 可采用Scroller来实现. 
 * 在具体实现时,我们继承并重写View或者ViewGroup时可生成一个Scroller由它来具体 
 * 掌控移动过程和结合插值器Interpolator调用scrollTo()和scrollBy()方法. 
 *  
 *  
 * Scroller的两个主要构造方法: 
 * 1 public Scroller(Context context) {} 
 * 2 public Scroller(Context context, Interpolator interpolator){} 
 * 采用第一个构造方法时,在移动中会采用一个默认的插值器Interpolator 
 * 也可采用第二个构造方法,为移动过程指定一个插值器Interpolator 
 *  
 *  
 * Scroller的调用过程以及View的重绘: 
 * 1 调用public void startScroll(int startX, int startY, int dx, int dy) 
 *   该方法为scroll做一些准备工作. 
 *   比如设置了移动的起始坐标,滑动的距离和方向以及持续时间等. 
 *   该方法并不是真正的滑动scroll的开始,感觉叫prepareScroll()更贴切些. 
 *    
 * 2 调用invalidate()或者postInvalidate()使View(ViewGroup)树重绘 
 *   重绘会调用View的draw()方法 
 *   draw()一共有六步:  
 *   Draw traversal performs several drawing steps which must be executed    
 *   in the appropriate order:    
 *   1. Draw the background    
 *   2. If necessary, save the canvas' layers to prepare for fading    
 *   3. Draw view's content    
 *   4. Draw children    
 *   5. If necessary, draw the fading edges and restore layers    
 *   6. Draw decorations (scrollbars for instance) 
 *   其中最重要的是第三步和第四步 
 *   第三步会去调用onDraw()绘制内容 
 *   第四步会去调用dispatchDraw()绘制子View 
 *   重绘分两种情况: 
 *   2.1 ViewGroup的重绘 
 *       在完成第三步onDraw()以后,进入第四步ViewGroup重写了 
 *       父类View的dispatchDraw()绘制子View,于是这样继续调用: 
 *       dispatchDraw()-->drawChild()-->child.computeScroll(); 
 *   2.2 View的重绘 
 *       我们注意到在2提到的"调用invalidate()".那么对于View它又是怎么 
 *       调用到了computeScroll()呢？View没有子View的.所以在View的源码里可以 
 *       看到dispatchDraw()是一个空方法.所以它的调用路径和ViewGroup是不一样的. 
 *       在此不禁要问:如果一个ButtonSubClass extends Button 当mButtonSubClass 
 *       执行mButtonSubClass.scrollTo()方法时怎么触发了ButtonSubClass类中重写 
 *       的computeScroll()方法??? 
 *       在这里我也比较疑惑,只有借助网上的资料和源码去从invalidate()看起. 
 *       总的来说是这样的:当View调用invalidate()方法时,会导致整个View树进行 
 *       从上至下的一次重绘.比如从最外层的Layout到里层的Layout,直到每个子View. 
 *       在重绘View树时ViewGroup和View时按理都会经过onMeasure()和onLayout()以及 
 *       onDraw()方法.当然系统会判断这三个方法是否都必须执行,如果没有必要就不会调用. 
 *       看到这里就明白了:当这个子View的父容器重绘时,也会调用上面提到的线路: 
 *       onDraw()-->dispatchDraw()-->drawChild()-->child.computeScroll(); 
 *       于是子View(比如此处举例的ButtonSubClass类)中重写的computeScroll()方法 
 *       就会被调用到. 
 *        
 * 3 View树的重绘会调用到View中的computeScroll()方法 
 *  
 * 4 在computeScroll()方法中 
 *   在View的源码中可以看到public void computeScroll(){}是一个空方法. 
 *   具体的实现需要自己来写.在该方法中我们可调用scrollTo()或scrollBy() 
 *   来实现移动.该方法才是实现移动的核心. 
 *   4.1 利用Scroller的mScroller.computeScrollOffset()判断移动过程是否完成 
 *       注意:该方法是Scroller中的方法而不是View中的!!!!!! 
 *       public boolean computeScrollOffset(){ } 
 *       Call this when you want to know the new location. 
 *       If it returns true,the animation is not yet finished.   
 *       loc will be altered to provide the new location. 
 *       返回true时表示还移动还没有完成. 
 *   4.2 若动画没有结束,则调用:scrollTo(By)(); 
 *       使其滑动scrolling 
 *        
 * 5 再次调用invalidate(). 
 *   调用invalidate()方法那么又会重绘View树. 
 *   从而跳转到第3步,如此循环,直到computeScrollOffset返回false 
 *        
 *    
 *    
 *   具体的滑动过程,请参见示图 
 *    
 *    
 *  
 *    
 *    
 * 通俗的理解: 
 * 从上可见Scroller执行流程里面的三个核心方法 
 * mScroller.startScroll() 
 * mScroller.computeScrollOffset() 
 * view.computeScroll() 
 * 1 在mScroller.startScroll()中为滑动做了一些初始化准备. 
 *   比如:起始坐标,滑动的距离和方向以及持续时间(有默认值)等. 
 *   其实除了这些,在该方法内还做了些其他事情: 
 *   比较重要的一点是设置了动画开始时间. 
 *  
 * 2 computeScrollOffset()方法主要是根据当前已经消逝的时间 
 *   来计算当前的坐标点并且保存在mCurrX和mCurrY值中. 
 *   因为在mScroller.startScroll()中设置了动画时间,那么 
 *   在computeScrollOffset()方法中依据已经消逝的时间就很容易 
 *   得到当前时刻应该所处的位置并将其保存在变量mCurrX和mCurrY中. 
 *   除此之外该方法还可判断动画是否已经结束. 
 *    
 *   所以在该示例中: 
 *   @Override 
 *   public void computeScroll() { 
 *      super.computeScroll(); 
 *      if (mScroller.computeScrollOffset()) { 
 *          scrollTo(mScroller.getCurrX(), 0); 
 *          invalidate(); 
 *      } 
 *   } 
 *   先执行mScroller.computeScrollOffset()判断了滑动是否结束 
 *   2.1 返回false,滑动已经结束. 
 *   2.2 返回true,滑动还没有结束. 
 *       并且在该方法内部也计算了最新的坐标值mCurrX和mCurrY. 
 *       就是说在当前时刻应该滑动到哪里了. 
 *       既然computeScrollOffset()如此贴心,盛情难却啊! 
 *       于是我们就覆写View的computeScroll()方法, 
 *       调用scrollTo(By)滑动到那里!满足它的一番苦心吧. 
 *    
 *  
 * 备注说明: 
 * 1 示例没有做边界判断和一些优化,在这方面有bug. 
 *   重点是学习Scroller的流程 
 * 2 不用纠结getCurrX()与getScrollX()有什么差别,二者得到的值一样. 
 *   但要注意它们是属于不同类里的. 
 *   getCurrX()-------> Scroller.getCurrX() 
 *   getScrollX()-----> View.getScrollX() 
 *  
 *  
 * 参考资料: 
 * 0 http://androidxref.com/2.3.6/xref 
 * 1 http://blog.csdn.net/wangjinyu501/article/details/32339379 
 * 2 http://blog.csdn.net/zjmdp/article/details/7713209 
 * 3 http://blog.csdn.net/xiaanming/article/details/17483273 
 *   Thank you very much 
 * 
 */ 
public class ScrollLauncherViewGroup extends ViewGroup {
	private int lastX;
	private int currentX;
	private int distanceX;
	private Context mContext;
	private Scroller mScroller;

	public ScrollLauncherViewGroup(Context context) {
		super(context);
		mContext = context;
		mScroller = new Scroller(context);
	}

	public ScrollLauncherViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollLauncherViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 注意: 1 getWidth()和getHeight()得到是屏幕的宽和高 因为在布局时指定了该控件的宽和高为fill_parent 2 view.getScrollX(Y)()得打mScrollX(Y) 3 调用scrollTo(x, y)后,x和y分别被赋值给mScrollX和mScrollY 请注意坐标方向.
	 */
	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			childView.layout(i * getWidth(), 0, getWidth() + i * getWidth(), getHeight());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			currentX = (int) event.getX();
			distanceX = currentX - lastX;
			mScroller.startScroll(getScrollX(), 0, -distanceX, 0);
			break;
		case MotionEvent.ACTION_UP:
			// 手指从屏幕右边往左滑动,手指抬起时滑动到下一屏
			if (distanceX < 0 && Math.abs(distanceX) > 50) {
				mScroller.startScroll(getScrollX(), 0, getWidth() - (getScrollX() % getWidth()), 0);
				// 手指从屏幕左边往右滑动,手指抬起时滑动到上一屏
			} else if (distanceX > 0 && Math.abs(distanceX) > 50) {
				mScroller.startScroll(getScrollX(), 0, -(getScrollX() % getWidth()), 0);
			}
			break;

		default:
			break;
		}
		// 重绘View树
		invalidate();
		return true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			invalidate();
		} else {
			if (mScroller.getCurrX() == getWidth() * (getChildCount() - 1)) {
				Toast.makeText(mContext, "已滑动到最后一屏", Toast.LENGTH_SHORT).show();
			}
		}
	}

}