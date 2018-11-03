package com.toly1994.fivepoint.loge;


import com.toly1994.fivepoint.app.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:15:19<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ParseUtils {
    /**
     * 将黑棋和白棋的数据写入文件：格式x1,y1-x2,y2
     *
     * @param pos 棋坐标列表
     */
    public static String point2String(List<Point> pos) {

        //白棋字落点符串
        StringBuilder sbPos = new StringBuilder();

        for (Point p : pos) {
            sbPos.append(p.x).append(",").append(p.y).append("-");
        }
        return sbPos.toString();
    }

    /**
     * 从字符串解析出坐标点
     *
     * @param pointStr 坐标字符串
     */
    public static ArrayList<Point> parseData(String pointStr) {
        ArrayList<Point> points;
        if (pointStr != null) {
            points = new ArrayList<>();
            String[] strings = pointStr.split("-");
            for (String s : strings) {
                if (s.split(",").length >= 2) {
                    int x = Integer.parseInt(s.split(",")[0].trim());
                    int y = Integer.parseInt(s.split(",")[1].trim());
                    points.add(new Point(x, y));
                }
            }
            return points;
        }
        return null;
    }

}
