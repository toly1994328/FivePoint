package top.toly.zutils.core.io.sp;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/28 0028:23:45<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：SpUtils的抽象接口
 */
public interface Shareable<T> {

    Boolean put(String key, T value);

    T get(String key, T defValue);

    Boolean remove(String key);

    Boolean clear();
}
