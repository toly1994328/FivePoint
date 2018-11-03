package top.toly.zutils.core.zhttp.itf;

import top.toly.zutils.core.zhttp.rep.Response;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:7:40<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：请求监听器
 */
public interface Listener<T> {
    /**
     * 请求成功
     * @param response 响应
     */
    void onSuccess(Response<T> response);

    /**
     * 请求失败
     * @param e 异常
     */
    void onFailed(Exception e);

}
