package top.toly.zutils.core.ui.common;

import android.view.View;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/23 0023:8:40<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：视图工具类
 */
public class VUtils {

    /**
     * 显示若干视图
     *
     * @param views 视图
     */
    public static void showView(View... views) {

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏若干视图
     *
     * @param views 视图
     */
    public static void hideView(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }
}
