package top.toly.reslib.my_design.useful.slideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 为了优化主页面：在关闭时使其无法滑动，并点击打开
 * Created by Administrator on 2017/11/17.
 */
public class MyRelativeLayout extends RelativeLayout{

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SlideView mSlideView;//定义QQSlideView对象

    public void setSlideView(SlideView slideView) {//获得QQSlideView引用
        mSlideView = slideView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mSlideView!=null&&mSlideView.getCurrentState()== SlideView.DragState.Open){
            return true;//当打开状态，拦截并消费掉
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlideView!=null&&mSlideView.getCurrentState()== SlideView.DragState.Open){
            if (event.getAction()==MotionEvent.ACTION_UP){
                mSlideView.close();
            }
            return true;//当打开状态，拦截并消费掉
        }
        return super.onTouchEvent(event);
    }
}
