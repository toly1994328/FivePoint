package top.toly.reslib.my_design.interpolator;

import android.animation.TimeInterpolator;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/9:10:08
 * 邮箱：1981462002@qq.com
 * 说明：log函数实现加速插值器
 */
public class D_Log_Inter implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        return (float) (Math.log10(1 + 10 * input));
    }
}
