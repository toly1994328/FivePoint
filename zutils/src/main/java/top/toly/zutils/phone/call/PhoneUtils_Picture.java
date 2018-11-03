package top.toly.zutils.phone.call;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import top.toly.zutils.core.domain.FolderBean;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/14:10:15
 * 邮箱：1981462002@qq.com
 * 说明：获取手机，联系人工具类
 */
public class PhoneUtils_Picture {
    /**
     * 最大图片数量的文件夹图片数
     */
    public static int mMaxCount = 0;

    /**
     * 最大图片数量的文件夹
     */
    public static File mMaxCountDir = null;

    //////////////////////////获取手机的图片///////////////////////////////////

    public static List<FolderBean> getAllImagePath(Context ctx) {
        List<FolderBean> mFolderBeans = new ArrayList<>();

        //[1]查询获得游标:content://media/external/images/media
        Uri mIngUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = ctx.getContentResolver();
        Cursor cursor = resolver.query(mIngUri, null,
                 MediaStore.Images.Media.MIME_TYPE+ "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        //[2]通过游标获取path，创建folderBean对象并赋值
        //[2-1]为避免重复扫描，将dirPath放入HashSet集合
        Set<String> mDirPaths = new HashSet<>();
        while (cursor.moveToNext()) {
            //获取数据库中图片路径：/storage/emulated/0/DCIM/Camera/IMG20160501152640.jpg
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取父目录：/storage/emulated/0/DCIM/Camera
            File parentFile = new File(path).getParentFile();
            //没有父目录，跳出本次循环
            if (parentFile == null) continue;
            //声明实体对象
            FolderBean folderBean;
            //父目录的绝对路径：/storage/emulated/0/DCIM/Camera
            String dirPath = parentFile.getAbsolutePath();
            if (mDirPaths.contains(dirPath)) {
                continue;//集合中有这个目录 跳出本次循环
            } else {//集合中没有这个目录
                //加入集合
                mDirPaths.add(dirPath);
                //创建实体对象
                folderBean = new FolderBean();
                //父文件夹设置到folderBean
                folderBean.setDir(dirPath);
                //第一张图片路径设置到folderBean
                folderBean.setFirstImgPath(path);
            }

            if (parentFile.list() != null) {
                //根据父文件夹，过滤出所有以jpg,png,jpeg结尾的文件的数量
                int imgCount = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg");
                    }
                }).length;

                if (mMaxCount <= imgCount) {
                    mMaxCount = imgCount;
                    mMaxCountDir = parentFile;
                }

                //设置文件夹下图片的数量
                folderBean.setCount(imgCount);
                //加入集合
                mFolderBeans.add(folderBean);
            }
        }
        cursor.close();
        return mFolderBeans;
    }
}
