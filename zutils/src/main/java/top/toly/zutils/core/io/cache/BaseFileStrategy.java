package top.toly.zutils.core.io.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import top.toly.zutils.core.io.IOUtils;
import top.toly.zutils.core.shortUtils.Md5Util;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/26 0026:6:38<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：文件缓存基类
 */
public abstract class BaseFileStrategy implements CacheStrategy {
    /**
     * 缓存文件的文件夹名称
     */
    private String mDirName;

    /**
     * 构造函数
     * @param dirName 缓存文件的文件夹名称
     */
    public BaseFileStrategy(String dirName) {
        mDirName = dirName;
    }

    @Override
    public void setCache(String key, String value, long time) {
        // 以url为文件名, 以json为文件内容,保存在本地
        // 生成缓存文件
        File cacheFile = new File(mDirName + File.separator + Md5Util.getMD5(key));
        FileWriter writer = null;

        try {
            if (!cacheFile.exists()) {
                cacheFile.getParentFile().mkdirs();
                cacheFile.createNewFile();
            }
            writer = new FileWriter(cacheFile);
            // 缓存失效的截止时间
            long deadline = System.currentTimeMillis() + time * 60 * 60 * 1000;//有效期
            writer.write(deadline + "\n");// 在第一行写入缓存时间, 换行
            writer.write(value);// 写入文件
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    @Override
    public String getCache(String key) {
        // 生成缓存文件
        File cacheFile = new File(mDirName + File.separator + Md5Util.getMD5(key));
        // 判断缓存是否存在
        if (cacheFile.exists()) {
            // 判断缓存是否有效
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String deadline = reader.readLine();// 读取第一行的有效期
                long deadTime = Long.parseLong(deadline);

                if (System.currentTimeMillis() < deadTime) {// 当前时间小于截止时间,
                    // 说明缓存有效
                    // 缓存有效
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                } else {
                    setCache(key, "", 0);//缓存过期清空
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }
}
