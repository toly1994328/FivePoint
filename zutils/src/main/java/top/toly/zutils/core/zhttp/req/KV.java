package top.toly.zutils.core.zhttp.req;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:16:36<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：请求参数：键值对
 */
public class KV {
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private Object value;

    public KV(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KV{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
