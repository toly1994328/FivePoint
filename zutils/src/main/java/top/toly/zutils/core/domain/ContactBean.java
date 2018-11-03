package top.toly.zutils.core.domain;

import android.content.Context;
import android.graphics.Bitmap;

import top.toly.zutils.phone.call.PhoneUtils_Contact;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/14:9:23
 * 邮箱：1981462002@qq.com
 * 说明：联系人实体类
 */
public class ContactBean {
    /**
     * 联系人姓名
     */
    public String name;
    /**
     * 联系人地址
     */
    public String address;
    /**
     * 联系人邮箱
     */
    public String email;
    /**
     * 联系人电话
     */
    public String phone;
    /**
     * 联系人头像
     */
    public Bitmap photo;

    @Override
    public String toString() {
        return "ContactBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", photo=" + photo +
                '}';
    }

    /**
     * 根据号码获得联系人图片
     *
     * @param ctx 上下文
     */
    public void setPhoto(Context ctx) {
        this.photo = PhoneUtils_Contact.getContactPhoto(ctx, this.phone);
    }
}
