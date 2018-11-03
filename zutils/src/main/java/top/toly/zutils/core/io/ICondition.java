package top.toly.zutils.core.io;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/26 0026:16:14<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：比较接口
 */
public interface ICondition<T> {
    /**
     * 比较方法接口
     * @param param 待比较参数
     * @return 是否比较成功
     */
    boolean condition(T param);
}
