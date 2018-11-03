package top.toly.zutils.core.base;


import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.toly.zutils.core.test.Logic;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/10:9:52
 * 邮箱：1981462002@qq.com
 * 说明：View封装父类
 */
public abstract class BaseView extends View {

    private float mDensity = getContext().getResources().getDisplayMetrics().density;

    public enum MoveSpeed {
        SlOW, NORMAL, FAST, ROCKET
    }

    /**
     * 移动方向
     */
    public enum Orientation {
        NO,
        TOP, BOTTOM, LEFT, RIGHT,
        TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT
    }


    private MoveSpeed mMoveSpeed = MoveSpeed.NORMAL;
    private Orientation mOrientation = Orientation.NO;


    private boolean isOnTouchEvent = true;


    public BaseView(Context context) {
        this(context, null, 0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isOnTouchEvent = init(attrs);
    }


    /**
     * 按下坐标点
     */
    private PointF mPos;

    /**
     * 移动的最后一次坐标点
     */
    private PointF p;


    private float dy = 0;//下移总量
    private float dx = 0;//右移总量
    private double allS;//位移
    private double vMax;//最大速度

    protected boolean isDown = false;//是否按下
    protected boolean isMove = false;//是否移动


    protected PointF tempP0;//临时点--记录按下时点
    private long lastTimestamp = 0L;//最后一次的时间戳
    private double tempV = 0;

    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        parseEvent(event);

        return isOnTouchEvent;
    }


    /**
     * 添加自己的事件解析
     *
     * @param event 事件
     */
    private void parseEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //按下---为p0赋值
                mPos = new PointF(event.getX(), event.getY());
                tempP0 = mPos;
                lastTimestamp = System.currentTimeMillis();
                isDown = true;

                if (mOnEventListener != null) {
                    mOnEventListener.down(mPos);
                }
                break;

            case MotionEvent.ACTION_MOVE:


                //最后一次坐标点
                p = new PointF(event.getX(), event.getY());
                //处理速度
                long curTimestamp = speedHandler();

                dx = p.x - tempP0.x;
                dy = p.y - tempP0.y;

                double dir = Logic.deg((float) Math.acos(dx / allS));
                orientationHandler();//处理方向

                if (mOnEventListener != null) {
                    mOnEventListener.move(mPos, allS, dy, dx, dy < 0 ? dir : -dir, mOrientation);
                }
                if (Math.abs(dy) > 50 / 3.0 * mDensity) {
                    isMove = true;
                }

                mPos = p;//更新位置
                lastTimestamp = curTimestamp;//更新时间
                tempV = vMax;
                break;
            case MotionEvent.ACTION_UP:
                orientationHandler();//处理方向

                if (mOnEventListener != null) {
                    mOnEventListener.up(mPos, mMoveSpeed, mOrientation);
                }

                reset();//重置工作
                break;
        }
    }

    /**
     * 重置工作
     */
    private void reset() {
        isDown = false;//重置按下状态
        tempP0.x = 0;//重置：tempP0
        tempP0.y = 0;//重置：tempP0
        tempV = 0;//重置速度
        allS = 0;//位移量置零
        vMax = 0;//最大速度置零
        mOrientation = Orientation.NO;//重置方向
        mMoveSpeed = MoveSpeed.NORMAL;//重置速度
    }

    /**
     * 处理方向
     */
    private void orientationHandler() {
        if (allS != 0) {
            double dir = Logic.deg((float) Math.acos(dx / allS));

            if (dy < 0 && dir > 70 && dir < 110) {
                mOrientation = Orientation.TOP;
            }

            if (dy > 0 && dir > 70 && dir < 110) {
                mOrientation = Orientation.BOTTOM;
            }

            if (dx > 0 && dir < 20) {
                mOrientation = Orientation.RIGHT;
            }

            if (dx < 0 && dir > 160) {
                mOrientation = Orientation.LEFT;
            }

            if (dy < 0 && dir <= 70 && dir >= 20) {
                mOrientation = Orientation.TOP_RIGHT;
            }

            if (dy < 0 && dir >= 110 && dir <= 160) {
                mOrientation = Orientation.TOP_LEFT;
            }

            if (dx > 0 && dy > 0 && dir >= 20 && dir <= 70) {
                mOrientation = Orientation.BOTTOM_RIGHT;
            }

            if (dx < 0 && dy > 0 && dir >= 110 && dir <= 160) {
                mOrientation = Orientation.BOTTOM_LEFT;
            }
        }
    }

    /**
     * 处理速度
     *
     * @return 当前时间
     */
    private long speedHandler() {
        long curTimestamp = System.currentTimeMillis();
        long t = curTimestamp - lastTimestamp;
        float s = Logic.disPos2d(mPos, p);
        allS += s;
        //由于速度是px/ms-->dp/ms
        double v = s / t * 1000 / 5;
        vMax = tempV > v ? tempV : v;

        if (vMax < 500 / 3.0 * mDensity) {
            mMoveSpeed = MoveSpeed.SlOW;
        }
        if (vMax > 500 / 3.0 * mDensity && vMax <= 1000 / 3.0 * mDensity) {
            mMoveSpeed = MoveSpeed.NORMAL;
        }
        if (vMax > 1000 / 3.0 * mDensity && vMax < 4000 / 3.0 * mDensity) {
            mMoveSpeed = MoveSpeed.FAST;
        }
        if (vMax > 4000 / 3.0 * mDensity) {
            mMoveSpeed = MoveSpeed.ROCKET;
        }
        return curTimestamp;
    }


    /**
     * 适配dp
     *
     * @param dp dp数字
     * @return px 数字
     */
    public float dp2px(float dp) {
        final Float scale = getContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * 适配dp
     *
     * @param id dimen的值
     * @return px 数字
     */
    public int dimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    /**
     * 适配dp
     *
     * @param id dimen的值
     * @return px 数字
     */
    public int color(int id) {
        return ContextCompat.getColor(getContext(), id);
    }


    /**
     * 初始化工作
     *
     * @param attrs 自定义属性
     * @return 是否相应事件
     */
    public abstract boolean init(AttributeSet attrs);

    ////////////////////////////////////-----------事件监听回调
    public interface OnEventListener {
        /**
         * 点击
         *
         * @param pointF 点前点
         */
        void down(PointF pointF);

        /**
         * 抬起
         *
         * @param pointF      点前点
         * @param speed       速度
         * @param orientation 方向
         */
        void up(PointF pointF, MoveSpeed speed, Orientation orientation);

        /**
         * 移动
         *
         * @param pointF      点前点
         * @param s           位移
         * @param dy          y 位移
         * @param dx          x位移
         * @param dir         角度
         * @param orientation
         */
        void move(PointF pointF, double s, float dy, float dx, double dir, Orientation orientation);
    }

    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener onEventListener) {
        mOnEventListener = onEventListener;
    }

}
