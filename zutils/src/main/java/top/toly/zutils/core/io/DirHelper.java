package top.toly.zutils.core.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import top.toly.zutils.core.domain.DirBean;
import top.toly.zutils.core.shortUtils.L;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/26 0026:13:18<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class DirHelper {

    ////////////////////////单例模式start//////////////////////////////
    /**
     * 文件帮助类对象
     */
    private static DirHelper sDirHelper;


    /**
     * 私有化构造函数
     */
    private DirHelper() {
    }

    /**
     * 单例模式获取 DirHelper
     *
     * @return DirHelper
     */
    public static DirHelper newInstance() {
        if (sDirHelper == null) {
            synchronized (FileHelper.class) {
                if (sDirHelper == null) {
                    sDirHelper = new DirHelper();
                }
            }
        }
        return sDirHelper;
    }
////////////////////////单例模式end//////////////////////////////

    private long size = 0;
    private int sFileCount = 0;
    private int sDirCount = 0;

    /**
     * 获取文件夹信息--公开方法
     *
     * @param dir
     * @return
     */
    public DirBean getDirBean(File dir) {
        DirBean dirBean = getSizeLocal(dir);
        size = 0;
        sFileCount = 0;
        sDirCount = 0;
        return dirBean;
    }

    /**
     * 获取文件夹信息--私有方法
     *
     * @param dir
     * @return
     */
    private DirBean getSizeLocal(File dir) {
        DirBean dirBean = new DirBean(dir);
        //遍历文件夹
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                sFileCount++;
                size += file.length();//是文件是长度增加
            } else {
                sDirCount++;
                getSizeLocal(file);//不是文件时递归
            }
        }

        dirBean.setLength(size);
        dirBean.setFileCount(sFileCount);
        dirBean.setDirCount(sDirCount);
        return dirBean;//返回文件夹大小
    }

    /**
     * 获取某文件夹下的所有文件夹的路径和大小加入集合
     *
     * @param dir
     * @param list
     * @return
     */
    public long dirListSize(File dir, List<String> list) {
        //将size改成局部变量
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size = dirListSize(file, list);
                if (size < 1024) {
                    list.add(file + "----大小：" + size + "B");
                } else if (size < 1024 * 1024) {
                    list.add(file + "----大小：" + size / 1024.f + "KB");
                } else if (size > 1024 * 1024) {
                    list.add(file + "----大小：" + size / 1024.f / 1024 + "MB");
                }
            }
        }
        return size;
    }

    /**
     * 获取某文件夹下的所有空文件夹
     *
     * @param dir  根文件夹
     * @param list 列表
     * @return 大小
     */
    public long filterDir(File dir, List<String> list, ICondition<Long> condition) {
        //将size改成局部变量
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size = filterDir(file, list, condition);
                if (condition.condition(size)) {
                    list.add(file.getAbsolutePath());
                    L.d(file + L.l());
                }
            }
        }
        return size;
    }

    /**
     * 将文件列表每项的路径保存到目标文件
     *
     * @param list   列表
     * @param target 目标路径
     */
    public void writeList2File(List<String> list, String target) {
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(target));
            for (String s : list) {
                bfw.write(s);
                bfw.newLine();
                bfw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfw != null) {
                    bfw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件夹里的所有文件
     *
     * @param dir
     */
    public void deleteDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                String name = file.getName();
                boolean ok = file.delete();
                System.out.println(ok ? "成功删除--" + name : "删除失败--" + name);
            }
        }
        dir.delete();
    }
}
