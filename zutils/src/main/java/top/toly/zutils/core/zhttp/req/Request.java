package top.toly.zutils.core.zhttp.req;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import top.toly.zutils.core.zhttp.CountOutputStream;
import top.toly.zutils.core.zhttp.itf.Binary;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:16:32<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：请求的实体类
 */
public abstract class Request<T> {

    /**
     * 解析服务器返回数据
     *
     * @param repByteArray 服务器返回数据
     * @return 解析后数据
     */
    public abstract T parseResponse(byte[] repByteArray);

    /**
     * 地址
     */
    private String url;
    /**
     * 请求方法：枚举类
     */
    private RequestMethod method;

    /**
     * 请求参数：键值对
     */
    private List<KV> kvs;

    /**
     * 请求头
     */
    private Map<String, String> mRequestHead;
    private String mContentType;
    private String mCharSet = "utf-8";
    /**
     * 分界线
     */
    private String boundary = "--Toly1" + UUID.randomUUID();
    private String fix = "--";//修饰符
    private String preFixBoundary = fix + boundary;//前边界
    private String tailFilBoundary = preFixBoundary + fix;//后边界

    /**
     * 强制开启表单提交
     */
    private boolean enableFormData;

    /**
     * https:ssl证书
     */
    private HostnameVerifier mHostnameVerifier;
    /**
     * https:主机认证规则
     */
    private SSLSocketFactory mSSLSocketFactory;

    public Request(String url) {
        this(url, RequestMethod.GET);
    }

    public Request(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
        kvs = new ArrayList<>();
        mRequestHead = new HashMap<>();
    }

    public void add(String k, Object v) {
        kvs.add(new KV(k, v));
    }

    /**
     * 拼接url
     *
     * @return 完整的url
     */
    public String getUrl() {
        StringBuilder sb = new StringBuilder(url);
        String params = buildParamsString();
        if (!method.canOutputStream()) {
            if (params.length() > 0 && url.contains("?") && url.contains("=")) {
                sb.append("&");
            } else if (params.length() > 0 && !url.endsWith("?")) {
                sb.append("?");
            }
            sb.append(params);
        }
        return sb.toString();
    }

    public RequestMethod getMethod() {
        return method;
    }

    /**
     * 设置请求头
     *
     * @param key   键
     * @param value 值
     */
    public void setRequestHead(String key, String value) {
        mRequestHead.put(key, value);
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public String getContentType() {
        if (!TextUtils.isEmpty(mContentType)) {
            //返回开发者设定的特殊Content-Type
            return mContentType;
        } else if (enableFormData || hasFile()) {//开启模拟表单或请求参数含有文件
            return "multipart/form-data; boundary=" + boundary;
        }
        return "application/x-www-form-urlencoded";//普通提交
    }


    /**
     * 写出请求对象的body
     *
     * @param os 输出流
     */
    public void onWriteBody(OutputStream os) throws IOException {
        if (enableFormData || hasFile()) {
            writeFormData(os);//模拟表单数据
        } else {
            writeString(os);
        }
    }

    /**
     * 获取请求体的大小
     *
     * @return 请求体的大小
     */
    public long getContentLength() {
        //post请求时---文件上传
        //1.form :String表单 2.文件表单
        CountOutputStream cos = new CountOutputStream();
        try {
            onWriteBody(cos);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return cos.length();

    }

    /**
     * 写表单数据：文本与文件
     *
     * @param os 输出流
     */
    private void writeFormData(OutputStream os) throws IOException {
        for (KV kv : kvs) {
            String key = kv.getKey();
            Object value = kv.getValue();
            if (value instanceof Binary) {
                writeFormFileData(os, key, (Binary) value);
            } else {
                writeFormStringData(os, key, (String) value);
            }
            os.write("\r\n".getBytes());
        }
        os.write(tailFilBoundary.getBytes());
    }

    /**
     * 写普通input标签表单<input type="text"/>
     *
     * @param cos   流
     * @param key   键
     * @param value 文件
     */
    private void writeFormStringData(OutputStream cos, String key, String value) throws IOException {
        //===================描述String:==============
        //--boundary
        // Content-Disposition:form-data;name="KeyName"
        // Content-Type: text/plain;charset="utf-8"
        //
        //String数据----------------------------

        StringBuilder sb = new StringBuilder();
        //--boundary
        sb.append(preFixBoundary).append("\r\n");
        // Content-Disposition:form-data;name="KeyName"
        sb.append("Content-Disposition:form-data;name=\"").append(key)
                .append("\"").append("\r\n");
        //Content-Type: text/plain;charset="utf-8"
        sb.append("Content-Type: text/plain;charset=\"").append(mCharSet)
                .append("\"").append("\r\n");
        // 空行
        sb.append("\r\n");//拼接空行
        ////String数据
        sb.append(value);
        cos.write(sb.toString().getBytes(mCharSet));
    }

    /**
     * 写file的input标签类型表单:<input type="file"/>
     *
     * @param os    流
     * @param key   键
     * @param value 文件
     */
    private void writeFormFileData(OutputStream os, String key, Binary value) throws IOException {
        //===================描述file:<input type="file"/>================
        // --boundary
        // Content-Disposition:form-data;name="KeyName";filename="xxx.xxx"
        // Content-Type: 对应MimeTypeMap
        // 空行
        //file stream
        //--boundary--

        StringBuilder sb = new StringBuilder();
        // --boundary
        sb.append(preFixBoundary).append("\r\n");
        // Content-Disposition:form-data;name="KeyName";filename="xxx.xxx"
        sb.append("Content-Disposition: form-data; name=\"").append(key)
                .append("\"; filename=\"").append(value.getFileName()).append("\"").append("\r\n");

        //Content-Type: 对应MimeTypeMap
        sb.append("Content-Type: ").append(value.getMimeType()).append("\r\n");
        // 空行
        sb.append("\r\n");
        os.write(sb.toString().getBytes(mCharSet));

        //file stream
        if (os instanceof CountOutputStream) {
            ((CountOutputStream) os).write(value.getBinaryLength());
        } else {
            value.onWriteBinary(os);
        }
    }


    private void writeString(OutputStream os) throws IOException {
        String params = buildParamsString();
        os.write(params.getBytes());
    }


    /**
     * 构建请求参数
     *
     * @return k1=v1&k2=v2
     */
    protected String buildParamsString() {
        StringBuilder sb = new StringBuilder();
        for (KV kv : kvs) {
            Object v = kv.getValue();
            if (v instanceof String) {
                try {
                    sb.append("&")
                            .append(URLEncoder.encode(kv.getKey(), mCharSet))
                            .append("=")
                            .append(URLEncoder.encode((String) v, mCharSet));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * @return 请求参数是否含有文件
     */
    protected boolean hasFile() {
        for (KV kv : kvs) {
            Object v = kv.getValue();
            if (v instanceof Binary) {
                return true;
            }

        }
        return false;
    }

    public void formData(boolean enable) {
        if (!method.canOutputStream()) {
            throw new IllegalArgumentException(method.value() + " is not support outputStream");
        }
        enableFormData = enable;
    }

    /**
     * @return 获取请求头
     */
    Map<String, String> getRequestHead() {
        return mRequestHead;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", kvs=" + kvs.toString() +
                '}';
    }

    public void setCharSet(String charSet) {
        mCharSet = charSet;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory SSLSocketFactory) {
        mSSLSocketFactory = SSLSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }
}
