package com.toly1994.fivepoint.socket.client;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/18 0018:11:17<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：客户端连接时客户端的回调
 */
public interface IConnCallback {
    /**
     * 开始连接时回调
     */
    void onStart();
    /**
     * 连接错误回调
     *
     * @param e 异常
     */
    void onError(Exception e);
    /**
     * 连接成功回调
     */
    void onFinish(String msg);

    //给一个默认的接口对象--也可以在不写，在用时判断非空
    DefaultCnnCallback DEFAULT_CONN_CALLBACK = new DefaultCnnCallback();

    /**
     * 默认的连接时回调
     */
    class DefaultCnnCallback implements IConnCallback {
        @Override
        public void onStart() {

        }
        @Override
        public void onError(Exception e) {

        }
        @Override
        public void onFinish(String msg) {

        }
    }
}

