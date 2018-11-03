package top.toly.zutils.core.shortUtils;

import android.content.Context;
import android.util.TypedValue;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 作者：张风捷特烈
 * 时间：2018/3/2:10:13
 * 邮箱：1981462002@qq.com
 * 说明：换算相关工具类
 */
public class Change {

    /**
     * dp转px
     *
     * @param dpVal 值(dp)
     * @return 值(px)
     */
    public static int dp2px(Context ctx, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, ctx.getResources().getDisplayMetrics());
    }

    /**
     * px转dip
     *
     * @param px 值(px)
     * @return 值(dip)
     */
    public static float getDip(Context ctx, int px) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (px / density);
    }

    /**
     * 弧度转角度
     *
     * @param radian 弧度
     * @return 角度
     */
    public static double radian2degree(double radian) {
        double v = radian * 180 / Math.PI;
        BigDecimal bigDecimal = new BigDecimal(v);
        return bigDecimal.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * long类型的值转化为以M单位
     *
     * @param size 文件大小（long）
     * @return 以M单位
     */
    public static double getBit2M(long size) {
        double fSize = size / (double) 1024 / 1024;
        fSize = new BigDecimal(fSize).
                divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return fSize;
    }

    /**
     * 切割小数保留两位
     *
     * @param f 小数
     * @return 两位小数--String类
     */
    public static String cutNum2String(float f) {
        return new DecimalFormat("#.00").format(f);

    }

    /**
     * 切割小数保留两位
     *
     * @param f 小数
     * @return 两位小数
     */
    public static double cutNum2Double(float f) {
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * long类型的值转化为时间
     *
     * @param timeNum 时间 （long）
     * @return 时间（String）
     */
    public static String getTime(long timeNum) {
        return getTime("yyyy-MM-dd HH:mm:ss", timeNum);
    }

    /**
     * long类型的值转化为时间,自定义模式
     *
     * @param mode    时间模式
     * @param timeNum 时间 （long）
     * @return 时间（String）
     */
    public static String getTime(String mode, long timeNum) {
        SimpleDateFormat formatter = new SimpleDateFormat(mode);
        Date curDate = new Date(timeNum);
        return formatter.format(curDate);
    }

}
