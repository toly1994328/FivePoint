package top.toly.zutils.core.zhttp;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者：张风捷特烈
 * 时间：2018/7/12:15:08
 * 邮箱：1981462002@qq.com
 * 说明：自定义单例主线程Handler
 */
public class Poster extends Handler {
    private static Poster sPoster;

    private Poster() {
        super(Looper.getMainLooper());
    }

    public static Poster newInstance() {
        if (sPoster == null) {
            synchronized (Poster.class) {
                if (sPoster == null) {
                    sPoster = new Poster();
                }
            }
        }
        return sPoster;
    }
}
