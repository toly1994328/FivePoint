package top.toly.reslib.my_design.interpolator;

import android.animation.TimeInterpolator;

import com.toly1994.logic_canvas.logic.Logic;


/**
 * 作者：张风捷特烈
 * 时间：2018/7/9:10:08
 * 邮箱：1981462002@qq.com
 * 说明：sin函数实现加速插值器
 */
public class D_Sin_Inter implements TimeInterpolator {
    private static final String TAG = "D_Sin_Inter";

    @Override
    public float getInterpolation(float input) {
        //input是一个从0~1均匀变化的值
        //从0到PI/2均匀变化的值
        float rad = Logic.rad(90 * input);
        //返回这个弧度的sin值--sin曲线在0~PI/2区域是增长越来越缓慢，是的小球运动越来越缓慢
        return (float) (Math.sin(rad));
    }
}
