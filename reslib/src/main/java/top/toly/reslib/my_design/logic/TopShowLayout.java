package top.toly.reslib.my_design.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;
import com.toly1994.logic_canvas.utils.CanvasUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/14 0014:16:25<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class TopShowLayout extends FrameLayout {
    private static final String TAG = "TopShowLayout";
    private View mChild0;
    private View mChild1;
    private View mChild2;
    private int mChild2H;
    private int mChild2W;
    private int mChild1H;
    private int mChild1W;

    public TopShowLayout(Context context) {
        super(context);
    }

    public TopShowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopShowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild0 = getChildAt(0);
        mChild1 = getChildAt(1);
        mChild2 = getChildAt(2);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

        mChild1H = mChild1.getMeasuredHeight();
        mChild1W = mChild1.getMeasuredWidth();
        mChild2H = mChild2.getMeasuredHeight();
        mChild2W = mChild2.getMeasuredWidth();

    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        childAt.layout(0,0,200,200);
//        Log.e(TAG, "onLayout() called with: \n" + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");


//        childAt2.layout(0,200,mChild2W,mChild2H);
//        mChild2.layout(0,0,mChild2W,mChild2H);
        mChild0.layout(0,0,100,100);
        mChild1.layout(100,100,200,200);
        mChild2.layout(200,200,400,400);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Painter painter = PainterEnum.INSTANCE.getInstance(canvas);
        CanvasUtils.drawGrid(getContext(), 100, canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
/*
        maxHeight和maxWidth就是我们最后计算汇总后的ViewGroup需要的宽和高。用来报告给ViewGroup的parent。

        在计算maxWidth时，我们首先简单地把所有子View的宽度加起来，
        如果该ViewGroup所有的子View的宽度加起来都没有
        超过parent的宽度限制，那么我们把该ViewGroup的measured宽度设为maxWidth，
        如果最后的结果超过了parent的宽度限制，我们就设置measured宽度为parent的限制宽度，
        这是通过对maxWidth进行resolveSizeAndState处理得到的。

        对于maxHeight，在每一行中找出最高的一个子View，然后把所有行中最高的子View加起来。
        这里我们在报告maxHeight时，也进行一次resolveSizeAndState处理。

*/

        int maxHeight = 0;
        int maxWidth = 0;

        /*
            mLeftHeight表示当前行已有子View中最高的那个的高度。当需要换行时，把它的值加到maxHeight上，
            然后将新行中第一个子View的高度设置给它。
            mLeftWidth表示当前行中所有子View已经占有的宽度，
当新加入一个子View导致该宽度超过parent的宽度限制时，
增加maxHeight的值，同时将新行中第一个子View的宽度设置给它。

         */

        int mLeftHeight = 0;
        int mLeftWidth = 0;


        final int count = getChildCount();

        final int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize =  MeasureSpec.getSize(heightMeasureSpec);


        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);//一次获取子view

            //可见性为gone的子View，我们就当它不存在。
            if (child.getVisibility() == GONE) {
                continue;
            }

            // 测量该子View
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);



            //简单地把所有子View的测量宽度相加。
            maxWidth += child.getMeasuredWidth();
            maxHeight += child.getMeasuredHeight();
            mLeftWidth += child.getMeasuredWidth();

//

        }

        Log.e(TAG, "onMeasure: "+maxHeight);
        setMeasuredDimension(maxWidth, maxHeight);

    }
}
