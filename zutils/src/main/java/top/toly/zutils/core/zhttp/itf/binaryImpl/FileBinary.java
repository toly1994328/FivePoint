package top.toly.zutils.core.zhttp.itf.binaryImpl;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import top.toly.zutils.core.zhttp.Poster;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:16:40<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class FileBinary extends AbstractBinary {

    private File mFile;

    public FileBinary(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("The file is not exists");
        }
        mFile = file;
    }

    @Override
    public String getFileName() {
        return mFile.getName();
    }

    @Override
    public String getMimeType() {

        //获取MimeType
        String mimeType = "application/octet-stream";//所有二进制文件通用的 MimeType
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (mimeTypeMap.hasExtension(getFileName())) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(getFileName());
            mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    @Override
    public long getBinaryLength() {
        return mFile.length();
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        long allLen = getBinaryLength();
        float writtenLen = 0.f;

        FileInputStream fis = new FileInputStream(mFile);
        byte[] buf = new byte[1024 * 2];
        int len = 0;
        while ((len = fis.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
            writtenLen += len;
            int progress = (int) (writtenLen / allLen * 100);
            //发出进度
            Poster.newInstance().post(new ProgressMessage(progress));

        }

    }
}
