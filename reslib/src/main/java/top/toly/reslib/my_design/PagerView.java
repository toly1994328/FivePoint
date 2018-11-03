package top.toly.reslib.my_design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import top.toly.zutils.core.domain.Point;
import top.toly.zutils.core.shortUtils.Change;
import top.toly.zutils.core.ui.anima.AniHelper;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/8:15:38
 * 邮箱：1981462002@qq.com
 * 说明：
 */
public class PagerView extends ViewGroup {

    private AniHelper mAniHelper;
    private Context mContext;

    public PagerView(Context context) {
        this(context, null, 0);

    }

    public PagerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mAniHelper = new AniHelper();

    }

    @Override
    /**
     * 计算控件尺寸==ViewGroup还有带孩子
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);//一次获取子view
            childView.measure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    /**
     * 对子view进行布局排列，确定子view位置
     * changed  若为true ，说明布局发生了变化
     * l\t\r\b\  是指当前viewgroup 在其父view中的位置
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();//获取子view个数
        int mWidth = getWidth();//MyPager的宽度
        int mHeight = getHeight();//MyPager的高度
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);//一次获取子view

            //指定子view的位置  ,  左，上，右，下，是指在viewGround坐标系中的位置
            childView.layout(0 + mWidth * i, 0, mWidth * (i + 1), mHeight);
        }
    }

    boolean isSonMoving = false;

    @Override
    /**
     * 是否中断事件的传递 默认false
     * ev是子控件上的ev
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        parseEvent(ev);
        boolean result = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                tempP1 = new Point((double) ev.getX(), (double) ev.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                double disx = Math.abs(ev.getX() - tempP0.x);
                double disy = Math.abs(ev.getY() - tempP0.y);
                if (disx > disy && disx > Change.dp2px(mContext, 8)) {
                    result = true;
                    isSonMoving = false;
                } else {
                    result = false;//不拦截
                    isSonMoving = true;
                }

                break;
        }
        return result;
    }

    /**
     * 当前的ID值
     * 显示在屏幕上的子View的下标
     */
    private int currId = 0;

    Point p0 = new Point(0, 0);//最后一次坐标点
    Point p = new Point(0, 0);//最后一次坐标点
    float dy = 0;//下移总量
    float dx = 0;//右移总量

    double vMAX;//最大速度
    //是否移动
    boolean isMove = false;
    //是否按下
    boolean isDown = false;

    //是否向左滑
    boolean isToLeft = false;
    boolean isToLR = false;
    //是否向下滑
    boolean isToDown = false;
    boolean isToUD = false;
    //滑动速度等级
    int speedLever = 0;


    long lastTimestamp = 0L;//最后一次的时间戳
    Point tempP0;//临时点--记录按下时点
    double tempV = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        parseEvent(event);
        invalidate();
        return true;
    }

    /**
     * 添加自己的事件解析
     *
     * @param event
     */
    private void parseEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x0 = event.getX();
                float y0 = event.getY();
                p0.x = x0;
                p0.y = y0;
                tempP0 = p0;
                lastTimestamp = new Date().getTime();
                isDown = true;
//
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                p = new Point(x, y);//最后一次坐标点

                long curTimestamp = new Date().getTime();
                long t = curTimestamp - lastTimestamp;
                double s = disPos2d(p0, p);
                double v = s / t * 1000;
                //慢速 LEVER 1:   v < 500
                //稍快 LEVER 2:   v > 500 && v < 1000
                //快速 LEVER 3:   v > 1000 && v < 4000
                //极速 LEVER 4:   v > 4000
                vMAX = tempV > v ? tempV : v;

                if (vMAX > 500) {
                    speedLever = 1;
                }
                if (vMAX > 500 && vMAX < 1500) {
                    speedLever = 2;
                }
                if (vMAX > 1500 && vMAX < 4000) {
                    speedLever = 3;
                }
                if (vMAX > 4000) {
                    speedLever = 4;
                }
                float dataX = (float) (p.x - p0.x);
                float dataY = (float) (p.y - p0.y);
                dx = (float) (p.x - tempP0.x);
                dy = (float) (p.y - tempP0.y);


                //x的移动量大于y,并且x有一定的移动量，此时向左或向右
                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Change.dp2px(mContext, 8)) {
                    isToLeft = dx < 0;
                    isToLR = true;
                    isToUD = false;
                    isToUD = false;
                } else {
                    isToDown = dy > 0;
                    isToUD = true;
                    isToLR = false;
                    isToLeft = false;
                }
                if (!isSonMoving) {
                    scrollBy(-(int) dataX, 0);
                }

                if (Math.abs(dy) > 50) {
                    isMove = true;
                }
                p0 = p;//更新位置
                lastTimestamp = curTimestamp;//更新时间
                tempV = vMAX;
                break;
            case MotionEvent.ACTION_UP:
//
                if (speedLever < 3) {//  在没有发生快速滑动的时候，才执行按位置判断currid
                    if (event.getX() - tempP0.x > getWidth() / 2) { // 手指向右滑动，超过屏幕的1/2  当前的currid - 1
                        currId = currId == 0 ? 0 : currId - 1;
                    } else if (-(event.getX() - tempP0.x) > getWidth() / 2) { // 手指向左滑动，超过屏幕的1/2  当前的currid + 1
                        currId = currId >= getChildCount() - 1 ? getChildCount() - 1 : currId + 1;
                    }
                }
                //检测快速移动
                if (speedLever >= 3 && isToLeft) {
                    currId = currId >= getChildCount() - 1 ? getChildCount() - 1 : currId + 1;
                }

                if (speedLever >= 3 && !isToLeft) {
                    currId = currId == 0 ? 0 : currId - 1;
                }
                moveToDest(currId);

                isDown = false;
                //重置：tempP0
                tempP0.x = 0.f;
                tempP0.y = 0.f;
                tempV = 0;
                speedLever = 0;
                break;
        }
    }


    /**
     * 移动到指定的屏幕上
     *
     * @param nextId 屏幕 的下标
     */
    private void moveToDest(int nextId) {

        //触发listener事件
        if (mOnPagerChange != null) {
            mOnPagerChange.pagerChange(currId);
        }

        int distance = currId * getWidth() - getScrollX(); // 最终的位置 - 现在的位置  = 要移动的距离

        mAniHelper.startAni(new Point(getScrollX(), 0), new Point(distance, 0));

        /*
         * 刷新当前view   onDraw()方法 的执行
         */
        invalidate();
    }

    @Override
    /**
     * invalidate();  会导致  computeScroll（）这个方法的执行
     */
    public void computeScroll() {
        if (mAniHelper.computeScrollOffset()) {
            int newX = mAniHelper.getP().x.intValue();
            System.out.println("newX::" + newX);
            scrollTo(newX, 0);
            invalidate();
        }
    }


    /**
     * 两点间距离函数
     *
     * @param p0 第一点
     * @param p1 第二点
     * @return 两点间距离
     */
    public float disPos2d(Point p0, Point p1) {
        return (float) Math.sqrt((p0.x - p1.x) * (p0.x - p1.x) + (p0.y - p1.y) * (p0.y - p1.y));
    }

    /////////////////////////////////////////
    public interface OnPagerChange {
        void pagerChange(int currId);
    }

    private OnPagerChange mOnPagerChange;

    public void setOnPagerChange(OnPagerChange onPagerChange) {
        mOnPagerChange = onPagerChange;
    }
}
