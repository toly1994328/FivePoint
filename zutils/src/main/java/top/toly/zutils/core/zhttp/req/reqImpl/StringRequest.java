package top.toly.zutils.core.zhttp.req.reqImpl;

import top.toly.zutils.core.zhttp.req.Request;
import top.toly.zutils.core.zhttp.req.RequestMethod;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:16:01<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：字符串型请求
 */
public class StringRequest extends Request<String> {
    public StringRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public StringRequest(String url, RequestMethod method) {
        super(url, method);
//        setRequestHead("Accept","application/json");
//        setRequestHead("Accept","application/xml");
        setRequestHead("Accept", "*");
    }

    @Override
    public String parseResponse(byte[] repByteArray) {
        if (repByteArray != null && repByteArray.length > 0) {
            return new String(repByteArray);
        }
        return "EMPTY";
    }
}
