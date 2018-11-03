package top.toly.reslib.my_design.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.toly1994.logic_canvas.base.Pos;
import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;
import com.toly1994.logic_canvas.core.shape.Shape;
import com.toly1994.logic_canvas.core.shape.ShapeArc;
import com.toly1994.logic_canvas.core.shape.ShapeLine;
import com.toly1994.logic_canvas.go.NumGo;

import java.util.Date;

import top.toly.zutils.core.ui.common.ColUtils;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/7:9:04
 * 邮箱：1981462002@qq.com
 * 说明：
 */
public class SwitchRopView extends View {
    private static final String TAG = "SwitchRopView";

    //线的属性---------------------------------
    /**
     * 线宽
     */
    private float mRopWidth = dip2px(8f);
    /**
     * 线高
     */
    private float mRopHeight = dip2px(60f);
    /**
     * 线的颜色
     */
    private int mRopColor = 0xff76C5F5;
    //斜线的属性---------------------------------
    /**
     * 斜线宽
     */
    private float mPieceWidth = dip2px(8.5f);
    /**
     * 斜线高
     */
    private float mPieceHeight = dip2px(5f);
    /**
     * 斜线的个数
     */
    private float mPieceCount = 5;
    /**
     * 斜线的颜色
     */
    private int mPieceColor = 0xffFBF579;
    //圆圈的属性---------------------------------
    /**
     * 圆圈半径
     */
    private float mRingR = dip2px(10f);
    /**
     * 圆圈厚度
     */
    private float mRingB = dip2px(6f);
    /**
     * 圆圈颜色
     */
    private int mRingColor = 0xffC4C4BA;

    //小圈的属性---------------------------------
    /**
     * 小圆圈半径
     */
    private float mDorR = dip2px(7f);
    /**
     * 小圆圈厚度
     */
    private float mDotB = dip2px(2f);
    /**
     * 小圆圈颜色
     */
    private int mDotColor = 0xffffffff;


    private NumGo mRunNum;
    private float mRotateRate;//旋转的分度值
    Pos lastPos = new Pos(0, 0);//最后一次坐标点
    long lastTimestamp = 0L;//最后一次的时间戳
    float downHeight = 0;//下拉总量
    boolean isMove = false;//是否移动
    boolean isDown = false;//是否按下

    public SwitchRopView(Context context) {
        this(context, null, 0);
    }

    public SwitchRopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchRopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mRunNum = new NumGo(false, -1, 2000).setOnUpdate(new NumGo.OnUpdate() {
            @Override
            public void onUpdate(float rate) {
                mRotateRate = rate;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Painter painter = PainterEnum.INSTANCE.getInstance(canvas);
        //绘制线
        Shape rop = new ShapeLine().ps(
                new Pos(0, 0),
                new Pos(0, -mRopHeight),
                new Pos(0 + mRopWidth, -mRopHeight),
                new Pos(0 + mRopWidth, 0),
                new Pos(0, 0))
                .fs(mRopColor).p((2 * mRingR - mRopWidth) / 2, 0f);
        painter.draw(rop);
        //绘制斜线
        ShapeLine piece = new ShapeLine();
        piece.fs(mPieceColor);
        Pos p0 = new Pos(0, 0);
        Pos p1 = new Pos(0, -mPieceHeight);
        Pos p2 = new Pos(mPieceWidth, -mPieceWidth - mPieceHeight);
        Pos p3 = new Pos(mPieceWidth, -mPieceWidth);
        for (int i = 0; i < mPieceCount; i++) {
            piece.ps(p0, p1, p2, p3).p((2 * mRingR - mRopWidth) / 2, -mRopHeight / 5 * i);
            painter.draw(piece);
        }
        //绘制圆圈和点
        ShapeArc ring = new ShapeArc();
        ring.r(mRingR).ang(360f).b(mRingB).ss(mRingColor);
        ShapeArc rot = new ShapeArc();
        rot.r(mDorR).ang(45f).b(mDotB).rot(360 * mRotateRate).ss(mDotColor);
        painter.groupMove(mRingR, -mRopHeight - mRingR + 3, ring, rot);
        painter.draw(ring);
        painter.draw(rot);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
        setMeasuredDimension((int) (2 * mRingR), (int) dip2px(600f));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                mRunNum.go();//按下触发RunNum,使得小点旋转
                lastPos.x = event.getX();
                lastPos.y = event.getY();
                lastTimestamp = System.currentTimeMillis();
                mRingColor = ColUtils.randomRGB();//环设随机色
                isDown = true;
                break;
            case MotionEvent.ACTION_UP://抬起
                mRopHeight = dip2px(60f);//回复到原始高度
                mRunNum.end();//结束动画
                if (mOnRopUPListener != null && isMove) {//设置抬起监听
                    mOnRopUPListener.ropUp();//回调抬起函数
                    isMove = false;
                }
                downHeight = 0;
                isDown = false;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE://2

                float x = event.getX();
                float y = event.getY();
                Pos curPos = new Pos(x, y);//最后一次坐标点

                long curTimestamp = new Date().getTime();
                long t = curTimestamp - lastTimestamp;
                float dataY = curPos.y - lastPos.y;
                mRopHeight += dataY;
                downHeight += dataY;
                if (downHeight > 50) {//下拉高度大于50才算移动
                    isMove = true;
                    if (t > 50) {//时间大于50ms才切换颜色
                        mRopColor = ColUtils.randomRGB();
                    }
                }

                if (mOnRopDownListener != null) {//下拉过程中的监听
                    mOnRopDownListener.ropDown(downHeight);
                }
                lastPos = curPos;//更新位置
                lastTimestamp = curTimestamp;//更新时间
                break;
        }
        return true;
    }

    /**
     * 适配dp
     *
     * @param dp
     * @return
     */
    public float dip2px(Float dp) {
        if (dp != null) {
            final Float scale = getContext().getResources().getDisplayMetrics().density;
            return dp * scale + 0.5f;
        }
        return dp;
    }

    /////////////////////////////////////////////拉绳拉下监听

    /**
     * 拉绳拉下监听
     */
    public interface OnRopDownListener {
        /**
         * 拉绳拉下回调函数
         * @param dataY 下移量
         */
        void ropDown(float dataY);
    }

    private OnRopDownListener mOnRopDownListener;

    public void setOnRopDownListener(OnRopDownListener onRopDownListener) {
        mOnRopDownListener = onRopDownListener;
    }

    /////////////////////////////////////////////拉绳松开监听

    /**
     * 拉绳松开监听
     */
    public interface OnRopUPListener {
        /**
         * 拉绳松开回调函数
         */
        void ropUp();
    }

    private OnRopUPListener mOnRopUPListener;

    public void setOnRopUPListener(OnRopUPListener onRopUPListener) {
        mOnRopUPListener = onRopUPListener;
    }
}
