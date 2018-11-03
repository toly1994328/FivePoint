package top.toly.reslib.my_design.useful;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.toly.reslib.R;


/**
 * 作者：张风捷特烈
 * 时间：2018/3/21:8:52
 * 邮箱：1981462002@qq.com
 * 说明：快速检索指示器视图
 */
public class QuickIndexBar extends View {

    private Context mContext;
    private String[] indexArr = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint paint;
    private int width;
    private float cellHeight;
    private int mDColor;//默认颜色
    private int mSColor;//选中时颜色
    private float mTextSize;//文字大小

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
        mDColor = typedArray.getColor(R.styleable.QuickIndexBar_Q_DColor, 0xff595959);
        mSColor = typedArray.getColor(R.styleable.QuickIndexBar_Q_SColor, 0x99CFDDEA);
        mTextSize = typedArray.getDimension(R.styleable.QuickIndexBar_Q_textSize, dp2Px(14));
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 设置抗锯齿
        paint.setColor(mDColor);
        paint.setTextSize(mTextSize);
        paint.setTextAlign(Align.CENTER);// 设置文本的起点是文字边框底边的中心
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        // 得到一个格子的高度
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            float x = width / 2;
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + i * cellHeight;
            //
            paint.setColor(lastIndex == i ? mSColor : mDColor);

            canvas.drawText(indexArr[i], x, y, paint);
        }
    }

    private int lastIndex = -1;// 记录上次的触摸字母的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / cellHeight);// 得到字母对应的索引
                if (lastIndex != index) {
                    // 对index做安全性的检查
                    if (index >= 0 && index < indexArr.length) {
                        if (listener != null) {
                            listener.onTouchLetter(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                // 重置lastIndex
                lastIndex = -1;
                break;
        }
        // 引起重绘
        invalidate();
        return true;
    }

    /**
     * 获取文本的高度
     *
     * @param text
     * @return
     */
    private int getTextHeight(String text) {
        // 获取文本的高度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    private OnTouchLetterListener listener;

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    /**
     * 触摸字母的监听器
     *
     * @author Administrator
     */
    public interface OnTouchLetterListener {
        void onTouchLetter(String letter);
    }

    /**
     * dip为单位的值转化为px为单位的值
     *
     * @param dip 值(dip)
     * @return 值(px)
     */
    public int dp2Px(float dip) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    //*******使用代码设置属性=> 初始颜色 按下颜色 文字大小**********//

    public void setDColor(int DColor) {
        mDColor = DColor;
    }

    public void setSColor(int SColor) {
        mSColor = SColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }
}
