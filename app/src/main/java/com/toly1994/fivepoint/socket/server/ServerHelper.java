package com.toly1994.fivepoint.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import top.toly.zutils.core.zhttp.Poster;

/**
 * 作者：张风捷特烈
 * 时间：2018/11/2 0015:14:53
 * 邮箱：1981462002@qq.com
 * 说明：服务端线程---=创建服务器端、监听客户端的连接、维护客户端消息集合
 */
public class ServerHelper extends Thread {

    //ServerSocket服务
    private ServerSocket mServerSocket;
    // 监听端口
    public static final int PORT = 8080;
    //维护客户端集合，记录客户端线程
    final Vector<ClientThread> mClients;
    //维护消息集合
    final Vector<String> msgs;
    //监听服务端连接的回调
    private IAcceptCallback mAcceptCallback;
    //向所有客户端发送消息的Runnable
    private final BroadCastTask mBroadCastTask;


    public ServerHelper() {
        mClients = new Vector<>();//实例化客户端集合
        msgs = new Vector<>();//实例化消息集合
        try {
            mServerSocket = new ServerSocket(PORT);//实例化Socket服务
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建广播线程并启动：这里只是在启动服务端时创建线程，不会频繁创建，不需要创建线程池
        mBroadCastTask = new BroadCastTask(this);
        new Thread(mBroadCastTask).start();
    }


    @Override
    public void run() {
        while (true) {
            try {
                //socket等待客户端连接
                Socket socket = mServerSocket.accept();
                //走到这里说明有客户端连接了，该客户端的Socket流即为socket,
                ClientThread clientThread = new ClientThread(socket, this);
                clientThread.start();
                //设置连接的回调
                if (mAcceptCallback != null) {
                    Poster.newInstance().post(() -> {
                        String ip = socket.getInetAddress().getHostAddress();
                        mAcceptCallback.onConnect(ip);
                    });
                }
                mClients.addElement(clientThread);
            } catch (IOException e) {
                e.printStackTrace();
                mAcceptCallback.onError(e);
            }
        }
    }

    /**
     * 开启服务发热方法
     *
     * @param iAcceptCallback 客户端连接监听
     * @return 自身
     */
    public ServerHelper open(IAcceptCallback iAcceptCallback) {
        mAcceptCallback = iAcceptCallback;
        new Thread(this).start();
        return this;
    }

    /**
     * 关闭服务端和发送线程
     */
    public void close() {
        try {
            mServerSocket.close();
            mBroadCastTask.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mServerSocket = null;
    }
}
