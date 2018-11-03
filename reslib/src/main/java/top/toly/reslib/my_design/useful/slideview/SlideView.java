package top.toly.reslib.my_design.useful.slideview;


import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/11/16.
 */
public class SlideView extends FrameLayout {

    private View mMenuView;
    private View mMainView;
    private ViewDragHelper mViewDragHelper;
    private int mWidth;//控件宽
    private float mDragRange;//拖拽范围
    private IntEvaluator intEvaluator;//int的计算器
    private FloatEvaluator floatEvaluator;//float的计算器

    private int mCurrentAnima = ANIMA_DEFAULT;

    public static final int ANIMA_DEFAULT = 0;
    public static final int ANIMA_ROTATE = 1;
    public static final int ANIMA_FADE = 2;

    enum DragState {//*b1-1:定义状态常量
        Open, Close;
    }

    private DragState currentState = DragState.Close;//*b1-2：SlideMenu默认关闭

    /**
     * *b1-3获取当前的状态
     *
     * @return
     */
    public DragState getCurrentState() {
        return currentState;
    }

    public SlideView(Context context) {//*a0:构造函数
        super(context);
        init();
    }


    public SlideView(Context context, AttributeSet attrs) {//*a0:
        super(context, attrs);
        init();
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {//*a0：
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * a1:onFinishInflate获取子控件
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //简单的异常处理：该控件只能有两个子控件
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("QQSlideView only have 2 children!");
        }
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    /**
     * a4:该方法在onMeasure执行完之后执行，那么可以在该方法中初始化自己和子View的宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mDragRange = mWidth * 0.8f;//打开时剩余0.2屏幕宽
    }

    //a3:获取触摸权
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * a2:初始化方法
     */
    private void init() {

        floatEvaluator = new FloatEvaluator();
        intEvaluator = new IntEvaluator();
        //D1:
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override//D1:
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mMainView || child == mMenuView;
            }

            @Override//D3:
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mMainView) {
                    if (left < 0) left = 0;//限制mainView的左边
                    if (left > mDragRange) left = (int) mDragRange;//限制mainView的右边
                }
                return left;
            }

            @Override//D7:可触区域，若不指定
            public int getViewHorizontalDragRange(View child) {
                return (int) mDragRange;
            }

            @Override//D5:
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == mMenuView) {
                    //手动固定住MenuView
                    mMenuView.layout(0, 0, mMenuView.getMeasuredWidth(), mMenuView.getMeasuredHeight());
                    //让mMainView移动起来
                    int newLeft = mMainView.getLeft() + dx;
                    if (newLeft < 0) newLeft = 0;//限制mMainView的左边
                    if (newLeft > mDragRange) newLeft = (int) mDragRange;//限制mMainView的右边
                    mMainView.layout(newLeft, mMainView.getTop() + dy, newLeft + mMainView.getMeasuredWidth(), mMainView.getBottom() + dy);
                }

                //1.*a2-3计算滑动的百分比
                float fraction = mMainView.getLeft() / mDragRange;
                //2.*a2-4执行伴随动画
                setAnim(fraction);

                //3.b4:恰当时机，调用接口对象方法：更改状态，回调listener的方法
                if (fraction == 0 && currentState != DragState.Close) {
                    currentState = DragState.Close;//更改状态为关闭，并回调关闭的方法
                    if (listener != null) listener.onClose();
                } else if (fraction > 0.99f && currentState != DragState.Open) {//此处log显示fraction无法达到1
                    currentState = DragState.Open;//更改状态为打开，并回调打开的方法
                    if (listener != null) listener.onOpen();
                }
                //将drag的fraction暴漏给外界
                if (listener != null) {
                    listener.onDraging(fraction);
                }
            }

            @Override//D6:
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (mMainView.getLeft() < mDragRange / 2) {
                    close(); //*a2-1在左半边
                } else {
                    open(); //*a2-2在右半边
                }
                //处理用户的稍微滑动
                if (xvel > 500 && currentState != DragState.Open) {
                    open();
                } else if (xvel < -500 && currentState != DragState.Close) {
                    close();
                }
            }
        });
    }

    /**
     * *a2-1打开菜单
     */
    public void close() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, mMainView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * *a2-3打开菜单
     */
    public void open() {
        mViewDragHelper.smoothSlideViewTo(mMainView, (int) mDragRange, mMainView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {//复写此方法，D6:才有效果
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setAnimaType(int currentAnima) {
        mCurrentAnima = currentAnima;
    }

    /**
     * *a2-4执行伴随动画
     *
     * @param fraction
     */
    private void setAnim(float fraction) {
        // startFloat + fraction * (endValue.floatValue() - startFloat)

        switch (mCurrentAnima) {
            case ANIMA_DEFAULT://移动menuView
                mMenuView.setTranslationX(intEvaluator.
                        evaluate(fraction, -mMenuView.getMeasuredWidth() / 2, 0));
                break;
            case ANIMA_ROTATE:
                mMainView.setRotation(floatEvaluator.evaluate(fraction, 0, 250));
                mMainView.setScaleX(floatEvaluator.evaluate(fraction, 1f, 0.55f));
                mMainView.setScaleY(floatEvaluator.evaluate(fraction, 1f, 0.55f));
                mMenuView.setTranslationX(intEvaluator.
                        evaluate(fraction, -mMenuView.getMeasuredWidth() / 2, 0));
                break;
            case ANIMA_FADE://移动menuView
                mMenuView.setTranslationX(intEvaluator.
                        evaluate(fraction, -mMenuView.getMeasuredWidth() / 2, 0));
                mMenuView.setAlpha(floatEvaluator.evaluate(fraction, 0.5f, 1f));
                break;
        }

        //给SlideMenu的背景添加黑色的遮罩效果
//        if (this.getBackground() != null) {
//            getBackground().setColorFilter(
//                    (Integer) DwUtils.evaluateColor(fraction,  Color.TRANSPARENT, Color.BLACK),
//                    PorterDuff.Mode.SRC_OVER);
//        }

    }

    /**
     * b3:回调监听方法
     */
    private OnDragStateChangeListener listener;//*b3-1:定义接口对象

    public void setOnDragStateChangeListener(OnDragStateChangeListener listener) {
        this.listener = listener;//*b3-2：获得传入接口对象引用
    }

    /**
     * b2:回调接口
     */
    public interface OnDragStateChangeListener {

        void onOpen();//打开的回调

        void onClose();//关闭的回调

        void onDraging(float fraction);//正在拖拽中的回调
    }


}
