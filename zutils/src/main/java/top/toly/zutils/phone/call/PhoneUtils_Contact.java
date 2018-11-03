package top.toly.zutils.phone.call;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import top.toly.zutils.core.domain.ContactBean;
import top.toly.zutils.core.io.FileHelper;
import top.toly.zutils.core.shortUtils.ToastUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/14:10:15
 * 邮箱：1981462002@qq.com
 * 说明：获取手机，联系人工具类
 * 注意添加读取联系人权限
 */
public class PhoneUtils_Contact {

/////////////////////////***联系人start****//////////////////////////

    /**
     * 获取联系人：ContactBean字段：name姓名  address地址  email邮箱 phone手机号
     *
     * @param ctx 上下文
     * @return ContactBean集合
     */
    public static List<ContactBean> getContact(Context ctx) {
        //创建一个容器放结果
        List<ContactBean> contactBeans = new ArrayList<>();
        //[1.]获得ContentResolver对象
        ContentResolver resolver = ctx.getContentResolver();
        //[2.1]得到Uri :访问raw_contacts的url
        Uri raw_contactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        //[2.2]得到Uri ://访问data的url
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        //[3]查询表，获得raw_contact表游标结果集
        Cursor raw_contactsCursor = resolver.query(
                raw_contactsUri, new String[]{"contact_id"}, null, null, null);
        //[4]遍历游标，获取数据，储存在bean中

        Cursor dataCursor = null;
        Cursor cursorPhoto = null;
        while (raw_contactsCursor.moveToNext()) {
            //[4.1]查询到contact_id
            String contact_id = raw_contactsCursor.getString(0);
            if (contact_id != null) {
                //[4.2]查询表，获得data表游标结果集
                dataCursor = resolver.query(dataUri,
                        new String[]{"data1", "mimetype"},//注意不是mimetype_id
                        "raw_contact_id=?",
                        new String[]{contact_id}, null);
                //[4.3]新建实体类
                ContactBean contactBean = new ContactBean();
                while (dataCursor.moveToNext()) {
                    String result = dataCursor.getString(0);
                    //[4.4]根据实体类判断数据，放入实体类中
                    String mimetype = dataCursor.getString(1);
                    if (mimetype != null) {
                        switch (mimetype) {
                            case "vnd.android.cursor.item/phone_v2":
                                contactBean.phone = result;
                                break;
                            case "vnd.android.cursor.item/email_v2":
                                contactBean.email = result;
                                break;
                            case "vnd.android.cursor.item/name":
                                contactBean.name = result;
                                break;
                            case "vnd.android.cursor.item/postal-address_v2":
                                contactBean.address = result;
                                break;
                        }
                    }
                }
                //[5.1]关闭data表Cursor
                dataCursor.close();
//                contactBean.setPhoto(ctx);
                contactBeans.add(contactBean);//加入集合
            }
        }


        //[5.2]关闭raw_contacts表Cursor
        raw_contactsCursor.close();
        return contactBeans;
    }

    /**
     * 备份通信录核心方法
     *
     * @param ctx 上下文
     * @param os  输出流
     */
    private static void backupsContact(Context ctx, OutputStream os) {
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(os, "utf-8");
            serializer.startDocument("utf-8", true);//文档开始
            serializer.startTag(null, "CONTACT");//标签开始
            for (ContactBean contact : getContact(ctx)) {//遍历
                serializer.startTag(null, "contact");

                if (contact.name != null) {
                    serializer.startTag(null, "name");
                    serializer.text(contact.name.trim());
                    serializer.endTag(null, "name");
                }

                if (contact.phone != null) {
                    serializer.startTag(null, "phone");
                    serializer.text(contact.phone.trim());
                    serializer.endTag(null, "phone");
                }

                if (contact.address != null) {
                    serializer.startTag(null, "address");
                    serializer.text(contact.address.trim());
                    serializer.endTag(null, "address");
                }
                if (contact.email != null) {
                    serializer.startTag(null, "email");
                    serializer.text(contact.email.trim());
                    serializer.endTag(null, "email");
                }
                serializer.endTag(null, "contact");
            }
            serializer.endTag(null, "CONTACT");//标签结束
            serializer.endDocument();//文档结束
        } catch (IOException e) {
            ToastUtil.show(ctx, "备份联系人失败！");
            e.printStackTrace();
        }
    }

    /**
     * 备份联系人到SD卡
     *
     * @param ctx  上下文
     * @param name 文件名
     */
    public static void backupsContact2SD(Context ctx, String name) {
        File file = FileHelper.get().writeFile2SD(name, "", false);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        backupsContact(ctx, fos);
        ToastUtil.show(ctx, "备份联系人成功！\n请在SD卡根目录下查看");
    }

    /**
     * 备份短信到本应用文件夹
     *
     * @param name 文件名
     */
    public static void backupsContact2Local(Context ctx, String name) {
        try {
            backupsContact(ctx, ctx.openFileOutput(name, MODE_PRIVATE));
            ToastUtil.show(ctx, "备份联系人成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存联系人
     *
     * @param name    姓名
     * @param phone   手机号
     * @param address 地址
     * @param email   邮箱
     */
    public static void saveContact(Context ctx, String name, String phone, String address, String email) {
        ContentResolver resolver = ctx.getContentResolver();//获得ContentResolver对象
        Uri raw_contactsUri = Uri.parse("content://com.android.contacts/raw_contacts");//访问raw_contacts的url
        Uri dataUri = Uri.parse("content://com.android.contacts/data");//访问data的url
        //在raw_contacts最后插入contact_id
        Cursor raw_contactsCursor = resolver.query(//查询表，获得游标结果集
                raw_contactsUri, new String[]{"contact_id"}, null, null, null);
        int count = raw_contactsCursor.getCount();
        ContentValues values = new ContentValues();
        values.put("contact_id", count + 1);
        resolver.insert(raw_contactsUri, values);
        //data表中插入姓名
        ContentValues nameValues = new ContentValues();
        nameValues.put("raw_contact_id", count + 1);
        nameValues.put("data1", name);
        nameValues.put("mimetype", "vnd.android.cursor.item/name");
        resolver.insert(dataUri, nameValues);
        //data表中插入号码
        ContentValues phoneValues = new ContentValues();
        phoneValues.put("raw_contact_id", count + 1);
        phoneValues.put("data1", phone);
        phoneValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
        resolver.insert(dataUri, phoneValues);
        //data表中插入地址
        ContentValues adressValues = new ContentValues();
        adressValues.put("raw_contact_id", count + 1);
        adressValues.put("data1", address);
        adressValues.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
        resolver.insert(dataUri, adressValues);
        //data表中插入email
        ContentValues emailValues = new ContentValues();
        emailValues.put("raw_contact_id", count + 1);
        emailValues.put("data1", email);
        emailValues.put("mimetype", "vnd.android.cursor.item/email_v2");
        resolver.insert(dataUri, emailValues);
    }

    /**
     * 根据号码获得联系人头像
     *
     * @param ctx    上下文
     * @param number 号码
     * @return 图片
     */
    public static Bitmap getContactPhoto(Context ctx, String number) {
        Bitmap bmpHead = null;
        ContentResolver resolver = ctx.getContentResolver();//获得ContentResolver对象
        // 获得Uri
        Uri uriNumber2Contacts = Uri.parse("content://com.android.contacts/"
                + "data/phones/filter/" + number);
        // 查询Uri，返回数据集
        Cursor cursorCantacts = resolver.query(uriNumber2Contacts, null, null, null, null);
        // 如果该联系人存在
        if (cursorCantacts.getCount() > 0) {
            // 移动到第一条数据
            cursorCantacts.moveToFirst();
            // 获得该联系人的contact_id
            Long contactID = cursorCantacts.getLong(cursorCantacts.getColumnIndex("contact_id"));
            // 获得contact_id的Uri
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
            // 打开头像图片的InputStream
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
            // 从InputStream获得bitmap
            bmpHead = BitmapFactory.decodeStream(input);
        }
        return bmpHead;
    }
    /////////////////////////***联系人end****//////////////////////////


}
