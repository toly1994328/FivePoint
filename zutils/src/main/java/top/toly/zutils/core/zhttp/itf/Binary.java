package top.toly.zutils.core.zhttp.itf;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:16:36<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public interface Binary {
    String getFileName();

    String getMimeType();


    long getBinaryLength();

    void onWriteBinary(OutputStream outputStream) throws IOException;

}
