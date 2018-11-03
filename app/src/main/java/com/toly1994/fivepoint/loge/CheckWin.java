package com.toly1994.fivepoint.loge;


import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.app.SPCons;

import java.util.List;


public class CheckWin {

    private static Point checkPoint;
    private static int checkModel = -1;
    private static int sMaxCountInLine;

    public static boolean checkFiveInLineWinner(List<Point> points, int maxCountInLine) {
        sMaxCountInLine = maxCountInLine;
        for (Point point : points) {
            int x = point.x;
            int y = point.y;

            if (checkHorizontal(x, y, points)) {
                return true;
            } else if (checkVertical(x, y, points)) {
                return true;
            } else if (checkLeftDiagonal(x, y, points)) {
                return true;
            } else if (checkRighttDiagonal(x, y, points)) {
                return true;
            }
        }
        return false;
    }

    private static boolean check(int x, int y, List<Point> points, int checkOri) {
        int count = 1;

        for (int i = 1; i < sMaxCountInLine; i++) {
            switch (checkOri) {
                case SPCons.HORIZONTAL:// 横向判断
                    checkPoint = new Point(x - i, y);
                    break;
                case SPCons.VERTICAL:// 竖向判断
                    checkPoint = new Point(x, y - i);
                    break;
                case SPCons.LEFT_DIAGONAL:// 左斜判断
                    checkPoint = new Point(x - i, y + i);
                    break;
                case SPCons.RIGHT_DIAGONAL:// 右斜判断
                    checkPoint = new Point(x + i, y + i);
                    break;
            }
            if (points.contains(checkPoint)) {
                count++;

            } else {
                break;
            }
        }

        if (count == SPCons.MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    // 横向判断
    private static boolean checkHorizontal(int x, int y, List<Point> points) {
        checkModel = SPCons.HORIZONTAL;
        return check(x, y, points, checkModel);
    }

    // 竖向判断
    private static boolean checkVertical(int x, int y, List<Point> points) {
        checkModel = SPCons.VERTICAL;
        return check(x, y, points, checkModel);
    }

    // 左斜判断
    private static boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        checkModel = SPCons.LEFT_DIAGONAL;
        return check(x, y, points, checkModel);
    }

    // 右斜判断
    private static boolean checkRighttDiagonal(int x, int y, List<Point> points) {
        checkModel = SPCons.RIGHT_DIAGONAL;
        return check(x, y, points, checkModel);
    }
}
