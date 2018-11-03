package top.toly.zutils.core.io.cache;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/26 0026:6:20<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：缓存策略接口
 */
public interface CacheStrategy {
    /**
     * 存储缓存
     * @param key 文件名
     * @param value 文件内容
     * @param time 有效时间 单位：小时
     */
    void setCache(String key, String value,long time);

    /**
     * 获取缓存
     * @param key 文件名
     * @return 文件内容
     */
    String getCache(String key);

}
