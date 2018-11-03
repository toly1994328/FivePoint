package top.toly.reslib.my_design.logic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.toly1994.logic_canvas.base.BaseView;
import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;

import top.toly.reslib.R;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/12 0012:8:43<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class DotNumView extends BaseView {
    /**
     * 大圆高
     */
    private float mBigHeight = dp2px(20);
    /**
     * 大圆颜色
     */
    private int mBigCircleColor = 0x88000000;
    /**
     * 小圆颜色
     */
    private int mCenterColor = 0x885DFBF9;
    /**
     * 文字
     */
    private String mText = "";
    /**
     * 是否选中
     */
    private boolean isChecked;


    public DotNumView(Context context) {
        this(context, null, 0);
    }

    public DotNumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.DotNumView);
        mBigHeight = ta.getDimension(R.styleable.DotNumView_z_Dot_Height, mBigHeight);
        mBigCircleColor = ta.getColor(R.styleable.DotNumView_z_Dot_BigColor, mBigCircleColor);
        mCenterColor = ta.getColor(R.styleable.DotNumView_z_Dot_SmallColor, mCenterColor);
        mText = ta.getString(R.styleable.DotNumView_z_Dot_text);
        isChecked = ta.getBoolean(R.styleable.DotNumView_z_Dot_isChecked, false);
        ta.recycle();//一定记得回收！！！
    }

    @Override
    public boolean init(AttributeSet attrs) {


        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取绘画者
        Painter painter = PainterEnum.INSTANCE.getInstance(canvas);
        float R = mBigHeight / 2;
        if (isChecked) {//选中状态绘制
            painter.draw(sa.deepClone().r(R).ang(360).p(R, -R).fs(mCenterColor));
            painter.drawText(st.deepClone()
                    .size((int) (0.6 * mBigHeight)).str(mText)
                    .p(R, R + (int) (0.2 * mBigHeight)).fs(Color.WHITE));
        } else {//未中状态绘制
            painter.draw(sa.deepClone().r(R).ang(360).p(R, -R).fs(mBigCircleColor));
            painter.draw(sa.deepClone().r((float) (0.3 * R)).ang(360).p(R, -R).fs(mCenterColor));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension((int) mBigHeight, (int) mBigHeight);
    }

    public float getBigHeight() {
        return mBigHeight;
    }

    public void setBigHeight(int bigHeight) {
        mBigHeight = bigHeight;
    }

    public int getBigCircleColor() {
        return mBigCircleColor;
    }

    public void setBigCircleColor(int bigCircleColor) {
        mBigCircleColor = bigCircleColor;
    }

    public int getCenterColor() {
        return mCenterColor;
    }

    public void setCenterColor(int centerColor) {
        mCenterColor = centerColor;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {

        this.isChecked = checked;
    }

    /**
     * 更新视图
     * @param text 文字
     * @param checked 是否选中
     */
    public void update(String text, boolean checked) {
        mText = text;
        this.isChecked = checked;
        invalidate();
    }
}
