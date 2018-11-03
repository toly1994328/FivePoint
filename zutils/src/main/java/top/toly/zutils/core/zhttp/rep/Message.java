package top.toly.zutils.core.zhttp.rep;

import top.toly.zutils.core.zhttp.itf.Listener;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:7:36<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：信息类
 */
public class Message implements Runnable {
    private Response mResponse;

    private Listener mListener;

    public Message(Response response, Listener listener) {
        mListener = listener;
        mResponse = response;
    }

    @Override
    public void run() {
        //回调到主线程
        Exception e = mResponse.getE();
        if (e != null) {
            mListener.onFailed(e);
        } else {
            mListener.onSuccess(mResponse);
        }
    }
}
