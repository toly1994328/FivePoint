package com.toly1994.fivepoint.model;


import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.loge.ParseUtils;

import java.util.ArrayList;

import top.toly.zutils.core.io.FileHelper;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:9:38<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class PointModel implements IModel {


    @Override
    public ArrayList<Point> getData(String path) {
        String pointStr = FileHelper.get().readFromSD(path);
        return ParseUtils.parseData(pointStr);
    }

}
