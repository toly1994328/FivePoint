package top.toly.reslib.my_design.logic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;
import com.toly1994.logic_canvas.core.shape.Shape;
import com.toly1994.logic_canvas.core.shape.ShapeStar;

import top.toly.reslib.R;
import top.toly.zutils.core.ui.common.ColUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/3 0003:19:01<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class StarView extends View {
    /**
     * 星星的角数
     */
    private int mAngleNum = 5;
    /**
     * 星星高
     */
    private float mHeight = dp2px(20f);
    /**
     * 星星内接圆直径
     */
    private float mr = dp2px(10f);
    /**
     * 星星颜色
     */
    private int mStarColor = Color.BLUE;
    /**
     * 是否填充
     */
    private boolean isFill = true;


    public StarView(Context context) {
        this(context, null, 0);
    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs 自定义属性
     */
    private void init(AttributeSet attrs) {
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.StarView);

        mAngleNum = ta.getInteger(R.styleable.StarView_z_star_AngleNum, mAngleNum);

        mHeight = ta.getDimension(R.styleable.StarView_z_star_height, mHeight);
        mr = ta.getDimension(R.styleable.StarView_z_star_r, mr);
        mStarColor = ta.getColor(R.styleable.StarView_z_star_color, mStarColor);
        isFill = ta.getBoolean(R.styleable.StarView_z_star_isFill, isFill);
        ta.recycle();//一定记得回收！！！


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) mHeight, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Painter painter = PainterEnum.INSTANCE.getInstance(canvas);
        //设置绘图对象
        Shape shape = new ShapeStar().num(mAngleNum).R(mHeight / 2).r(mr / 2).b(dp2px(1f));
        //判断是否填充
        if (isFill) {
            shape.fs(mStarColor);
        } else {
            shape.ss(mStarColor);
        }
        painter.draw(shape);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isFill = !isFill;
                mStarColor = ColUtils.randomRGB();
                invalidate();

//                if (mOnStarClickListener != null) {
//                    mOnStarClickListener.onClick(isFill);
//                    invalidate();
//                }
        }

        return true;
    }

    /**
     * 适配dp
     *
     * @param dp
     * @return
     */
    public Float dp2px(Float dp) {
        if (dp != null) {
            final Float scale = getContext().getResources().getDisplayMetrics().density;
            return dp * scale + 0.5f;
        }
        return dp;
    }

    public void setAngleNum(int angleNum) {
        mAngleNum = angleNum;
    }

    public void setHeight(float height) {
        mHeight = height;
    }

    public void setMr(float mr) {
        this.mr = mr;
    }

    public void setStarColor(int starColor) {
        mStarColor = starColor;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    /////////////////////////////点击监听
    public interface OnStarClickListener {
        void click(boolean isFill);
    }

    private OnStarClickListener mOnStarClickListener;

    public void setOnStarClickListener(OnStarClickListener onStarClickListener) {
        mOnStarClickListener = onStarClickListener;
    }
}
