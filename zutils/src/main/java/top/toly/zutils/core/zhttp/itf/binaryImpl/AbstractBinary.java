package top.toly.zutils.core.zhttp.itf.binaryImpl;

import java.io.IOException;
import java.io.OutputStream;

import top.toly.zutils.core.zhttp.itf.Binary;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:19:40<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public abstract class AbstractBinary implements Binary {

    protected OnProgressListener mOnProgressListener;

    /**
     * 设置上传进度监听
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }

    @Override
    public abstract String getFileName();

    @Override
    public abstract String getMimeType();

    @Override
    public abstract long getBinaryLength();

    @Override
    public abstract void onWriteBinary(OutputStream outputStream) throws IOException;

    //进度监听接口
    public interface OnProgressListener {
        void onProgress(int progress);
    }

    /**
     * 内部监听的Runnable类,用来向主线程发送信息
     */
    protected class ProgressMessage implements Runnable {
        private int progress;

        public ProgressMessage(int progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            if (mOnProgressListener != null) {
                mOnProgressListener.onProgress(progress);
            }
        }
    }

}
