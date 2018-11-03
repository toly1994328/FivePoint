package com.toly1994.fivepoint.socket.server;

import java.io.IOException;

/**
 * 作者：张风捷特烈
 * 时间：2018/11/3 0015:15:11
 * 邮箱：1981462002@qq.com
 * 说明：用于服务端向所有客户端发送消息
 */
public class BroadCastTask implements Runnable {

    //服务端线程
    private ServerHelper mServerHelper;
    //停止标志
    private boolean isRunning = true;

    public BroadCastTask(ServerHelper serverHelper) {
        mServerHelper = serverHelper;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {//每隔200毫秒，间断的监听客户端的发送消息情况
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str;

            if (mServerHelper.msgs.isEmpty()) {////当消息为空时，不执行下面
                continue;
            }
            str = mServerHelper.msgs.firstElement();

            for (ClientThread client : mServerHelper.mClients) {
                //获取所有的客户端线程,将信息写出
                try {
                    client.dos.writeUTF(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mServerHelper.msgs.removeElement(str);
            }
        }
    }

    public void close() {
        isRunning = false;
    }
}
