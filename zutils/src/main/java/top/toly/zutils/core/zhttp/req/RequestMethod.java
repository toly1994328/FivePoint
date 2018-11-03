package top.toly.zutils.core.zhttp.req;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:16:33<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：枚举类：请求方法
 */
public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    DELETE("DELETE");

    private String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean canOutputStream() {
        switch (this) {
            case POST:
            case DELETE:
                return true;
            default:
                return false;

        }
    }

    @Override
    public String toString() {
        return "RequestMethod{" +
                "value='" + value + '\'' +
                '}';
    }
}
