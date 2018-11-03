package top.toly.reslib.my_design.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.toly1994.logic_canvas.Jutils.core.test.ZRandom;
import com.toly1994.logic_canvas.base.BaseView;
import com.toly1994.logic_canvas.base.Cons;
import com.toly1994.logic_canvas.base.Pos;
import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;
import com.toly1994.logic_canvas.core.shape.Shape;
import com.toly1994.logic_canvas.core.shape.ShapeLine;
import com.toly1994.logic_canvas.go.NumGo;
import com.toly1994.logic_canvas.utils.CanvasUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/2 0002:6:53<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ArrowView extends BaseView {
    private static final String TAG = "ArrowView";

    Pos coo = v2(500, 800);
    private float mRotateRate;
    private NumGo mRunNum;


    float aX = 100f;
    float aY = 100f;
    float bX = 0;
    float bY = 100f;

    private float mAng = 0;

    public ArrowView(Context context) {
        super(context);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean init(AttributeSet attrs) {
        mRunNum = new NumGo(false, 10, 10000).setOnUpdate(new NumGo.OnUpdate() {
            @Override
            public void onUpdate(float rate) {
                mRotateRate = rate;
                invalidate();
            }
        });
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Painter painter = PainterEnum.INSTANCE.getInstance(canvas);
        //绘制网格
        CanvasUtils.drawGrid(getContext(), 50, canvas);
        //绘制坐标系
        CanvasUtils.drawCoord(getContext(), coo, 50, canvas);

        Pos pA = v2(aX, aY);
        Pos pB = v2(bX, bY);
        ShapeLine a = (ShapeLine) sl.deepClone().v(pA).coo(coo).b(5f).ss(Color.RED);
        ShapeLine b = a.deepClone().v(pB).parse();
        Shape c = a.deepClone().v(pA.add(pB)).ss(Color.BLUE);

        painter.draw(a, b, c);


        ShapeLine AC = (ShapeLine) sl.deepClone().ps(pA, pA.add(pB)).coo(coo).b(4).de(Cons.DOT_LINE_8).ss(Color.BLACK);
        ShapeLine BC = AC.deepClone().ps(pB, pA.add(pB));
        painter.draw(AC, BC);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        setOnEventListener(new OnEventListener() {
            @Override
            public void down(Pos pos) {

            }

            @Override
            public void up(Pos pos, MoveSpeed speed, Orientation orientation) {
                aX = ZRandom.rangeInt(-100, 300);
                aY = ZRandom.rangeInt(-100, 300);
                bY = ZRandom.rangeInt(-100, 300);
                bY = ZRandom.rangeInt(-100, 300);
                invalidate();
            }

            @Override
            public void move(Pos pos, double s, float dy, float dx, double dir,Orientation orientation) {

            }

        });

        return super.onTouchEvent(event);
    }
}
