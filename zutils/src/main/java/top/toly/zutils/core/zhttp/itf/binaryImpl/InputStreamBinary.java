package top.toly.zutils.core.zhttp.itf.binaryImpl;

import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import top.toly.zutils.core.zhttp.Poster;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:19:15<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class InputStreamBinary extends AbstractBinary {
    private InputStream mInputStream;
    private String mFileName;
    private String mMimeType;

    public InputStreamBinary(InputStream inputStream, String fileName) {
        this(inputStream, fileName, "application/octet-stream");
    }

    public InputStreamBinary(InputStream inputStream, @NonNull String fileName, @NonNull String mimeType) {
        if (!(inputStream instanceof ByteArrayInputStream) && !(inputStream instanceof FileInputStream)) {
            throw new IllegalArgumentException("This inputStream has not length");
        }

        mInputStream = inputStream;
        mFileName = fileName;
        mMimeType = mimeType;
    }

    @Override
    public String getFileName() {
        return mFileName;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }

    @Override
    public long getBinaryLength() {
        try {
            return mInputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        long allLen = getBinaryLength();
        float writtenLen = 0.f;

        byte[] buf = new byte[1024 * 2];
        int len = 0;
        while ((len = mInputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
            writtenLen += len;
            int progress = (int) (writtenLen / allLen * 100);
            //发出进度
            Poster.newInstance().post(new ProgressMessage(progress));
        }
    }
}
