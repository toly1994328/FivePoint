package com.toly1994.fivepoint.socket.client;

import android.os.AsyncTask;

import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.loge.ParseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:13:37<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ClientHelper {
    private Socket mSocket;
    private boolean isConned;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String mIp;
    private int mPort;
    private ExecutorService mExecutor;

    public ClientHelper(String ip, int port) {
        mIp = ip;
        mPort = port;
    }

    public DataInputStream getDis() {
        return dis;
    }

    /**
     * 发送所有落点的位置到服务端
     */
    public void writePos2Service(ArrayList<Point> whites, ArrayList<Point> blacks) {
        new Thread(() -> {
            if (isConned) {
                try {
                    String whiteStr = ParseUtils.point2String(whites);
                    String blackStr = ParseUtils.point2String(blacks);
                    dos.writeUTF(whiteStr + "#" + blackStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 连接到服务器
     *
     * @param callback 连接回调
     */
    public void conn2Server(IConnCallback callback) {

        if (isConned) {
            return;
        }

        if (callback == null) {
            callback = IConnCallback.DEFAULT_CONN_CALLBACK;
        }

        final IConnCallback finalCallback = callback;

        //开始回调：onStart函数
        finalCallback.onStart();
        //使用AsyncTask来实现异步通信(子线程-->主线程)
        new AsyncTask<Void, Void, String>() {

            @Override//子线程运行：耗时操作
            protected String doInBackground(Void... voids) {
                try {
                    //通过ip和端口连接到到服务端
                    mSocket = new Socket(mIp, mPort);
                    //通过mSocket拿到输入、输出流
                    dis = new DataInputStream(mSocket.getInputStream());
                    dos = new DataOutputStream(mSocket.getOutputStream());
                    //这里通过输入流获取连接时服务端发送的信息，并返回到主线程
                    return dis.readUTF();

                } catch (IOException e) {//异常处理及回调
                    e.printStackTrace();
                    finalCallback.onError(null);
                    isConned = false;
                    return null;
                }
            }

            @Override//此处是主线程，可进行UI操作
            protected void onPostExecute(String msg) {
                if (msg == null) {
                    //错误回调：onError函数
                    finalCallback.onError(null);
                    isConned = false;
                    return;
                }
                //成功的回调---此时onFinish在主线程
                finalCallback.onFinish(msg);
                isConned = true;
            }
        }.execute();
    }

    public interface OnReceiveListener {
        void receive(String arrayList);
    }

    private OnReceiveListener mOnReceiveListener;

    public void setOnReceiveListener(OnReceiveListener onReceiveListener) {
        mOnReceiveListener = onReceiveListener;
    }
}
