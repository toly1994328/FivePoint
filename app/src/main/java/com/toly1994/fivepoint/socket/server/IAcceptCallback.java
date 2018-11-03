package com.toly1994.fivepoint.socket.server;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/2 0018:11:17<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：客户端连接时服务端回调
 */
public interface IAcceptCallback {
    /**
     * 连接成功回调
     */
    void onConnect(String msg);

    /**
     * 连接错误回调
     * @param e 异常
     */
    void onError(Exception e);
}

