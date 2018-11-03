package top.toly.zutils.core.domain;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/12:16:46
 * 邮箱：1981462002@qq.com
 * 说明：短信实体类
 */
public class SMSBean {
    /**
     * 短信发送方
     */
    public String address;

    /**
     * 号码在通讯录中的姓名：无为null
     */
    public String name;
    /**
     * 短信时间
     */
    public String date;
    /**
     * 短信内容
     */
    public String body;
    /**
     * 1 接收短信 2 发送短信
     */
    public int type;

    /**
     * 同一个手机号互发的短信，其序号是相同的
     */
    public int thread_id;


    @Override
    public String toString() {
        return "SMSBean{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", body='" + body + '\'' +
                ", type=" + type +
                ", thread_id=" + thread_id +
                '}';
    }
}
