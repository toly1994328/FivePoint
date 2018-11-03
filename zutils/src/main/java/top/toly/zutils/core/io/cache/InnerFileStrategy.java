package top.toly.zutils.core.io.cache;

import android.content.Context;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/26 0026:6:28<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：以文件保存缓存 到本包 <br/>
 */
public class InnerFileStrategy extends BaseFileStrategy {

    public InnerFileStrategy(Context context) {
        super(context.getCacheDir().getPath());
    }
}
