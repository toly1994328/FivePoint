package com.toly1994.fivepoint.app;

import android.app.Application;

import top.toly.zutils.core.io.sp.SPFactory;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:8:26<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPFactory.init(this);
    }
}
