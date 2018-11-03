package top.toly.zutils.core.compat;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/1 0001:10:40<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：以View确定popWindow适配
 */
public class CompatShowPop {
    //pop顶对顶
    public static final int TOP2TOP = 0x01;
    //pop顶对底
    public static final int TOP2BOTTOM = 0x02;
    //pop底对顶
    public static final int BOTTOM2TOP = 0x03;

    /**
     * 以View确定popWindow适配
     *
     * @param popupWindow popupWindow
     * @param view        view
     * @param type        类型
     */
    public static void handle(PopupWindow popupWindow, View view, int type) {
        //处理PopWindow7.0适配
        if (Build.VERSION.SDK_INT < 24) {
            switch (type) {
                case TOP2TOP:
                    popupWindow.showAsDropDown(view, 0, -view.getHeight());
                    break;
                case TOP2BOTTOM:
                    popupWindow.showAsDropDown(view, 0, 0);
                    break;
                case BOTTOM2TOP:
//                    popupWindow.showAsDropDown(view, 0, -view.getHeight() - popupWindow.getHeight());
                    popupWindow.showAtLocation(view, Gravity.CLIP_HORIZONTAL, 0, 0);
            }
        } else {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            switch (type) {
                case TOP2TOP:
                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
                    break;
                case TOP2BOTTOM:
                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y + view.getHeight());
                    break;
                case BOTTOM2TOP:
//                    popupWindow.showAtLocation(view, Gravity.CLIP_HORIZONTAL, 0, 0);
                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - popupWindow.getHeight());
            }
        }
    }
}
