package top.toly.zutils.core.zhttp.req;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.toly.zutils.core.zhttp.itf.Listener;

/**
 * 作者：张风捷特烈
 * 时间：2018/7/12:15:04
 * 邮箱：1981462002@qq.com
 * 说明：请求执行器：枚举单例
 */
public enum RequestExecutor {
    INSTANCE();


    private ExecutorService mExecutorService;

    RequestExecutor() {
        mExecutorService = Executors.newSingleThreadExecutor();
    }


    /**
     * 执行请求
     *
     * @param request 请求
     */
    public void execute(Request request, Listener listener) {
        mExecutorService.execute(new RequestTask(request, listener));
    }
}
