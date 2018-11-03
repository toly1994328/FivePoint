package top.toly.zutils.core.template;

import android.os.SystemClock;
import android.view.View;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/20:16:04
 * 邮箱：1981462002@qq.com
 * 说明：多次点击事件
 */
public abstract class NClick {

    /**
     * n次点击事件
     *
     * @param view    需要点击的view
     * @param i       想要点击的次数
     * @param time_ms 毫秒数
     */
    public NClick(View view, int i, final int time_ms) {

        final long[] mHits = new long[i];
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - time_ms)) {
                    run();
                }
            }
        });

    }

    public abstract void run();


}
