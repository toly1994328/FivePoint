package top.toly.reslib.my_design.effect.swipe_delete;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/11/18.
 */
public class SwipeLayout extends FrameLayout {
    private static final String TAG = "SwipeLayout";
    /**
     * 左侧视图
     */
    private View mLeftView;
    /**
     * 右侧视图
     */
    private View mRightView;
    /**
     * 右侧视图高
     */
    private int mRightViewH;
    /**
     * 右侧视图宽
     */
    private int mRightViewW;
    /**
     * 左侧视图宽
     */
    private int mLeftViewW;
    /**
     * 左侧视图高
     */
    private int mLeftViewH;

    private ViewDragHelper mViewDragHelper;
    private SwipeLayoutManager mSwipeLayoutManager;

    /**
     * 状态枚举：开-关
     */
    enum SwipeState {
        Open, Close;
    }

    private SwipeState currentState = SwipeState.Close;

    public SwipeLayout(Context context) {
        this(context, null, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 所有子控件均被映射成xml后触发可在此获取子view
     */
    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        //左侧View
        mLeftView = getChildAt(0);
        //右侧View
        mRightView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取测量宽高后子空间的宽高
     *
     * @param w    测量后的宽：px
     * @param h    测量后的高：px
     * @param oldw 测量前的宽：px
     * @param oldh 测量前的高：px
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRightViewH = mRightView.getMeasuredHeight();
        mRightViewW = mRightView.getMeasuredWidth();

        mLeftViewH = mLeftView.getMeasuredHeight();
        mLeftViewW = mLeftView.getMeasuredWidth();
    }

    /**
     * 摆放子控件:相对与父容器左上角
     *
     * @param changed 是否改变
     * @param left    左
     * @param top     上
     * @param right   右
     * @param bottom  下
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        //摆放右侧，全显示
        mLeftView.layout(0, 0, mLeftViewW, mLeftViewH);
        //摆放左侧，全显示
        mRightView.layout(mLeftView.getRight(), 0, mLeftView.getRight() + mRightViewW, mRightViewH);
    }

    /**
     * @param ev 事件
     * @return 是否截断事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev);

        //*M4:如果当前有打开的，则需要直接拦截，交给onTouch处理
        if (!mSwipeLayoutManager.isShouldSwipe(this)) {
            //先关闭已经打开的layout
            mSwipeLayoutManager.closeCurrentLayout();
            result = true;
        }

        return result;
    }

    //按下第一点的X
    private float tempX0, downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //*M3:如果当前有打开的，则下面的逻辑不能执行
        if (!mSwipeLayoutManager.isShouldSwipe(this)) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        }

        switch (event.getAction()) {
            //*T1:偏向于水平方向，请求listview不要拦截SwipeLayout
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                tempX0 = downX;
                downY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(Color.WHITE);
                //1.获取x和y方向移动的距离
                float moveX = event.getX();
                float moveY = event.getY();
                float delatX = moveX - downX;//x方向移动的距离
                float delatY = moveY - downY;//y方向移动的距离
                if (delatX < 0 && Math.abs(delatX) > Math.abs(delatY) || currentState == SwipeState.Open) {
                    //表示移动是偏向于水平方向，那么应该SwipeLayout应该处理，请求listview不要拦截
                    requestDisallowInterceptTouchEvent(true);

                }
                //更新downX，downY
                downX = moveX;
                downY = moveY;

                break;//*T1:
            case MotionEvent.ACTION_UP:

                float abs = Math.abs(event.getX() - tempX0);
                if (currentState == SwipeState.Close && abs < 10 && mOnClickListener != null) {
                    mOnClickListener.onClick(this);
                }

                break;

        }
        mViewDragHelper.processTouchEvent(event);

        //return true表示处理此事件
        return true;
    }

    private void init() {
        mSwipeLayoutManager = SwipeLayoutManager.INSTANCE;

        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override//D1:获取拖动目标
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mLeftView || child == mRightView;
            }

            @Override//D7:水平触区
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }

            @Override//D3:水平移动
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mLeftView) {//滑动限制
                    if (left > 0) {
                        left = 0;
                    }
                    if (left < -mRightViewW) {
                        left = -mRightViewW;
                    }
                } else if (child == mRightView) {
                    if (left > mLeftViewW) {
                        left = mLeftViewW;
                    }
                    if (left < (mLeftViewW - mRightViewW)) {
                        left = mLeftViewW - mRightViewW;
                    }
                }
                return left;
            }

            @Override//D5:伴随移动
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                if (changedView == mLeftView) {
                    mRightView.layout(
                            mRightView.getLeft() + dx, mRightView.getTop() + dy,
                            mRightView.getRight() + dx, mRightView.getBottom() + dy);
                }
                if (changedView == mRightView) {
                    mLeftView.layout(
                            mLeftView.getLeft() + dx, mLeftView.getTop() + dy,
                            mLeftView.getRight() + dx, mLeftView.getBottom() + dy);
                }
                //判断开和关闭的逻辑
                if (mLeftView.getLeft() == 0 && currentState != SwipeState.Close) {
                    currentState = SwipeState.Close;//说明应该将state更改为关闭
                    if (listener != null) {//*b3-2:回调接口关闭的方法
                        listener.onClose();
                    }
                    //*M2:说明当前的SwipeLayout已经关闭，需要让Manager清空一下
                    mSwipeLayoutManager.clearCurrentLayout();

                } else if (mLeftView.getLeft() == -mRightViewW && currentState != SwipeState.Open) {
                    currentState = SwipeState.Open; //说明应该将state更改为开
                    //*b3-1:回调接口打开的方法
                    if (listener != null) {
                        listener.onOpen();
                    }

                    //*M1:当前的Swipelayout已经打开，需要让Manager记录一下
                    mSwipeLayoutManager.setSwipeLayout(SwipeLayout.this);
                }

            }

            @Override//D6:释放
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                super.onViewReleased(releasedChild, xvel, yvel);
                if (mLeftView.getLeft() < -mRightViewW / 2) {
                    open();
                } else {
                    close();
                }

                //处理用户的稍微滑动
                if (xvel < -500 && currentState != SwipeState.Open) {
                    open();
                } else if (xvel > 500 && currentState != SwipeState.Close) {
                    close();
                }

            }
        });

    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开方法
     */
    public void open() {
        mViewDragHelper.smoothSlideViewTo(mLeftView, -mRightViewW, mLeftView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 关闭
     */
    public void close() {
        mViewDragHelper.smoothSlideViewTo(mLeftView, 0, mLeftView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * b2:回调监听方法
     */
    private OnSwipeStateChangeListener listener;

    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * *b1:回调接口
     */
    public interface OnSwipeStateChangeListener {
        void onOpen();

        void onClose();
    }

    ///////////////点击监听------总算解决了--toly-2018年9月7日14:49:21----------------
    public interface OnClickListener {
        void onClick(View v);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
