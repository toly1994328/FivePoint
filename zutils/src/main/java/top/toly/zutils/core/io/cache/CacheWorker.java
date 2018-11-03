package top.toly.zutils.core.io.cache;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/26 0026:6:23<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：缓存工作类
 */
public class CacheWorker {
    /**
     * 缓存策略
     */
    private CacheStrategy mCacheStrategy;

    /**
     * 无参构造
     */
    public CacheWorker() {
    }

    /**
     * 一参构造
     * @param cacheStrategy 缓存策略
     */
    public CacheWorker(CacheStrategy cacheStrategy) {
        mCacheStrategy = cacheStrategy;
    }

    /**
     * 存储缓存
     * @param key 文件名
     * @param value 文件内容
     * @param time 有效时间 单位：小时
     */
    public void setCache(String key, String value, long time) {
        mCacheStrategy.setCache(key, value, time);
    }

    /**
     * 获取缓存
     * @param key 文件名
     * @return 文件内容
     */
    public String getCache(String key) {
        return mCacheStrategy.getCache(key);
    }

    /**
     * 设置缓存策略
     * @param cacheStrategy 缓存策略
     */
    public void setCacheStrategy(CacheStrategy cacheStrategy) {
        mCacheStrategy = cacheStrategy;
    }
}
