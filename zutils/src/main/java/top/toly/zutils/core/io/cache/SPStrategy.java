package top.toly.zutils.core.io.cache;

import android.content.Context;

import top.toly.zutils.core.io.sp.SpUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/26 0026:8:03<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：SharedPreferences缓存
 */
public class SPStrategy implements CacheStrategy {

    private Context mContext;

    public SPStrategy(Context context) {
        mContext = context;
    }

    @Override
    public void setCache(String key, String value, long time) {
        SpUtils<String> spString= new SpUtils<>(mContext);
        spString.put(key, value);

        SpUtils<Long> spLong = new SpUtils<>(mContext);
        spLong.put("LiftTime", System.currentTimeMillis() + time * 60 * 60 * 1000);
    }

    @Override
    public String getCache(String key) {
        SpUtils<Long> spLong = new SpUtils<>(mContext);
        Long liftTime = spLong.get("LiftTime", 0L);
        if (System.currentTimeMillis() < liftTime) {// 当前时间小于截止时间,
            SpUtils<String> spString= new SpUtils<>(mContext);
            return spString.get(key, "");
        }else {
            setCache(key, "", 0);//缓存过期清空
        }
        return null;
    }
}
