package top.toly.zutils.core.zhttp;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:10:54<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：自定义流统计字节的长度
 */
public class CountOutputStream extends OutputStream {
    /**
     * 同步的Long类型
     */
    private AtomicLong mAtomicLong = new AtomicLong();

    @Override
    public void write(int b) throws IOException {
        mAtomicLong.addAndGet(1);
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        mAtomicLong.addAndGet(b.length);
    }

    public void write(@NonNull long b) throws IOException {
        mAtomicLong.addAndGet(b);
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) throws IOException {
        mAtomicLong.addAndGet(len);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    /**
     * @return 流的字节长度
     */
    public long length() {
        return mAtomicLong.get();
    }
}
