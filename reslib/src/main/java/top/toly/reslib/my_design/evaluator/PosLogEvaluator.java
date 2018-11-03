package top.toly.reslib.my_design.evaluator;

import android.animation.TypeEvaluator;

import com.toly1994.logic_canvas.base.Pos;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/17 0017:10:41<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class PosLogEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        //初始点
        Pos startPos = (Pos) startValue;
        //结束点
        Pos endPos = (Pos) endValue;
        //计算每次更新时的x坐标
        float x = startPos.x + fraction * (endPos.x - startPos.x);
        //将y坐标进行联动
        float y = (float) (Math.log10(x) * 200);
        //返回更新后的点
        return endPos.clone(x, y);
    }
}
