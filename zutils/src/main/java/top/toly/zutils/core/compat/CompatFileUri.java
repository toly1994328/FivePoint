package top.toly.zutils.core.compat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/30 0030:18:38<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class CompatFileUri {
    public static void handle(Context context, String pkg, Intent intent, File file, String type) {
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, pkg + ".fileProvider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
}
