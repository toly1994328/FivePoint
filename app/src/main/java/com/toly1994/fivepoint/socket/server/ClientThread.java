package com.toly1994.fivepoint.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 作者：张风捷特烈
 * 时间：2018/10/15 0015:14:57
 * 邮箱：1981462002@qq.com
 * 说明：连接的客户端在此线程,用来向收集客户端发来的信息
 */
public class ClientThread extends Thread {

    //持有服务线程引用
    private ServerHelper mServerHelper;
    //输入流----接收客户端数据
    private DataInputStream dis = null;
    //输出流----用于向客户端发送数据
    DataOutputStream dos = null;

    public ClientThread(Socket socket, ServerHelper serverHelper) {

        mServerHelper = serverHelper;
        try {
            //通过传入的socket获取读写流
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            //服务端发送连接成功反馈
            dos.writeUTF("~连接服务器成功~!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ClientThread IO ERROR");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //此处读取客户端的消息,并加入消息集合
                String msg = dis.readUTF();
                mServerHelper.msgs.addElement(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
