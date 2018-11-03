package top.toly.zutils.view.listview.ev;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/27 0027:19:30<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：多类型ListView实体类父类
 */
public class ItemBean {
    private int type;

    public ItemBean(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
