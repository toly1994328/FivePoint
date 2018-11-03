package com.toly1994.fivepoint.view;


import com.toly1994.fivepoint.app.Point;

import java.util.ArrayList;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/27 0027:10:38<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public interface IView {
    /**
     * 更新视图
     */
    void updateView(ArrayList<Point> white, ArrayList<Point> black);

    /**
     * 胜利时的视图
     *
     * @param isWhiteWin
     */
    void winView(boolean isWhiteWin);
}
