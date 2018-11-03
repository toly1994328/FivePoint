package top.toly.zutils.phone.call;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import top.toly.zutils.core.domain.SMSBean;
import top.toly.zutils.core.io.FileHelper;
import top.toly.zutils.core.shortUtils.ToastUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/14:10:15
 * 邮箱：1981462002@qq.com
 * 说明：获取手机对象工具类
 */
public class PhoneUtils_SMS {

/////////////////////////***短信start****//////////////////////////

    /**
     * 获取短信：SMSBean：address发信人  date时间  body信息内容
     *
     * @param ctx 上下文
     * @return 短信bean集合 注意添加读取短信权限
     */
    public static List<SMSBean> getSMS(Context ctx) {
        List<SMSBean> smsBeans = new ArrayList<>();
        //[1.]获得ContentResolver对象
        ContentResolver resolver = ctx.getContentResolver();
        //[2.1]得到Uri :访问raw_contacts的url
        Uri uri = Uri.parse("content://sms");

        //[3]查询表，获得sms表游标结果集
        String[] projection = {"address", "date", "body", "type","person","thread_id"};//访问表的字段
        Cursor cursor = resolver.query(
                uri, projection, null, null, null);
        while (cursor.moveToNext()) {//遍历游标，获取数据，储存在bean中
            SMSBean smsBean = new SMSBean();
            smsBean.address = cursor.getString(0);
            smsBean.date = cursor.getString(1);
            smsBean.body = cursor.getString(2);
            smsBean.type = cursor.getInt(cursor.getColumnIndex("type"));
            smsBean.name = cursor.getString(cursor.getColumnIndex("person"));
            smsBean.thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
            smsBeans.add(smsBean);
        }
        cursor.close();
        return smsBeans;
    }

    /**
     * 备份短信核心方法
     *
     * @param ctx 上下文
     * @param os  输出流
     */
    private static void backupsSMS(Context ctx, OutputStream os) {
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(os, "utf-8");
            serializer.startDocument("utf-8", true);//文档开始
            serializer.startTag(null, "SMS");//标签开始
            for (SMSBean sms : getSMS(ctx)) {//遍历
                serializer.startTag(null, "sms");
                serializer.startTag(null, "address");
                serializer.text(sms.address.trim());
                serializer.endTag(null, "address");

                serializer.startTag(null, "date");
                serializer.text(sms.date.trim());
                serializer.endTag(null, "date");

                serializer.startTag(null, "body");
                serializer.text(sms.body.trim());
                serializer.endTag(null, "body");
                serializer.endTag(null, "sms");
            }
            serializer.endTag(null, "SMS");//标签结束
            serializer.endDocument();//文档结束
        } catch (IOException e) {
            ToastUtil.show(ctx, "备份短信失败！");
            e.printStackTrace();
        }
    }

    /**
     * 备份短信到SD卡
     *
     * @param ctx  上下文
     * @param name 文件名
     */
    public static void backupsSMS2SD(Context ctx, String name) {

        File file = FileHelper.get().writeFile2SD(name, "", false);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        backupsSMS(ctx, fos);
        ToastUtil.show(ctx, "备份短信成功！请在SD卡根目录下查看");
    }

    /**
     * 备份短信到本应用文件夹
     *
     * @param ctx  上下文
     * @param name 文件名
     */
    public static void backupsSMS2Local(Context ctx, String name) {
        try {
            backupsSMS(ctx, ctx.openFileOutput(name, MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
/////////////////////////***短信end****//////////////////////////////

}
