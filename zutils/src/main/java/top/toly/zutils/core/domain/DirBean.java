package top.toly.zutils.core.domain;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import top.toly.zutils.core.shortUtils.L;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/26 0026:13:20<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：文件夹对象
 */
public class DirBean {

    private File dir;//文件对象
    private String path; //文件路径
    private String name;//文件夹名
    private int dirCount;//文件夹数量
    private int fileCount;//文件的个数
    private long length; //文件夹大小
    private Long modifyTime;//最后修改时间

    public DirBean(File dir) {
        if (dir.isFile()) {
            return;
        }
        this.dir = dir;
        path = dir.getAbsolutePath();
        name = dir.getName();
        modifyTime = dir.lastModified();
    }

    public int getDirCount() {
        return dirCount;
    }

    public void setDirCount(int dirCount) {
        this.dirCount = dirCount;
    }

    public long getLength() {
        return length;
    }


    public void setLength(long length) {
        this.length = length;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLengthFormated() {
        String result = "";
        if (length < 1024) {
            result = length + "B";
        } else if (length < 1024 * 1024) {
            result = length / 1024.f + "KB";
            L.d(length + "----大小：" + length / 1024.f + "KB");
        } else if (length > 1024 * 1024) {
            result = length / 1024.f / 1024 + "MB";
        }
        if (length > 1024 * 1024 * 1024) {
            result = length / 1024.f / 1024 / 1024 + "GB";
        }
        return result;
    }

    public String getModifyTimeFormated() {
        return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.CHINA).format(getModifyTime());
    }



    @Override
    public String toString() {
        return "DirBean{" +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", dirCount=" + dirCount +
                ", fileCount=" + fileCount +
                ", length=" + getLengthFormated() +
                ", modifyTime=" + getModifyTimeFormated() +
                '}';
    }
}
