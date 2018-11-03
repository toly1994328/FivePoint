package top.toly.reslib.my_design.interpolator;

import android.animation.TimeInterpolator;
import android.util.Log;

import com.toly1994.logic_canvas.logic.Logic;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/9:10:08
 * 邮箱：1981462002@qq.com
 * 说明：三角函数实现减速插值器
 */
public class A_Sin_Inter implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {

        float result;

        result = (float) (Math.sin((Logic.rad(90 * input + 90))));

        Log.d("TAG", "getInterpolation: " + Logic.rad(90 * input) + ":" + result);
        return result;
    }
}
