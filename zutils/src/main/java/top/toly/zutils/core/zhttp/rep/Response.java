package top.toly.zutils.core.zhttp.rep;

import java.util.List;
import java.util.Map;

import top.toly.zutils.core.zhttp.req.Request;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:17:06<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：响应实体类
 */
public class Response<T> {
    private Request mRequest;
    private int mResponseCode;
    private T mRepBody;
    private Map<String, List<String>> mRepHeads;
    private Exception e;

    public Response(Request request, int responseCode, Map<String, List<String>> repHeads, Exception e) {
        mRequest = request;
        mResponseCode = responseCode;
        this.e = e;
        mRepHeads = repHeads;
    }

    public Request getRequest() {
        return mRequest;
    }


    public int getResponseCode() {
        return mResponseCode;
    }


    /**
     * @return 返回服务器的响应
     */
    public T getRepBody() {
        return mRepBody;
    }

    /**
     * 设置响应
     * @param repBody
     */
    public void setRepBody(T repBody) {
        mRepBody = repBody;
    }

    Exception getE() {
        return e;
    }

    public Map<String, List<String>> getRepHeads() {
        return mRepHeads;
    }
}

