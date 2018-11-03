package top.toly.zutils.core.zhttp.req;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import top.toly.zutils.core.shortUtils.L;
import top.toly.zutils.core.zhttp.itf.Listener;
import top.toly.zutils.core.zhttp.Poster;
import top.toly.zutils.core.zhttp.rep.Message;
import top.toly.zutils.core.zhttp.rep.Response;
import top.toly.zutils.core.zhttp.urlconn.URLConnFactory;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:16:43<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：请求任务：Runnable
 */
public class RequestTask<T> implements Runnable {
    /**
     * 请求
     */
    private Request<T> mRequest;
    /**
     * 回调监听
     */
    private Listener<T> mListener;
    /**
     * 响应体
     */
    private byte[] mRepBody;


    public RequestTask(Request<T> request, Listener<T> listener) {
        mListener = listener;
        mRequest = request;
    }

    @Override
    public void run() {
        Exception e = null;
        int code = -1;
        Map<String, List<String>> reqHeads = null;

        String path = mRequest.getUrl();
        RequestMethod method = mRequest.getMethod();
        OutputStream os = null;

        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            //1.建立连接
            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            handleHttps(conn);
            //切换URLConnection和OKHttp
            URLConnFactory urlConnFactory = URLConnFactory.newInstance();
            HttpURLConnection conn = urlConnFactory.openUrl(url);

            //1.1设置请求头信息
            conn.setRequestMethod(method.value());
            conn.setDoInput(true);
            conn.setDoOutput(method.canOutputStream());

            //设置请求头
            setRequestHead(conn, mRequest);

            //2.发送数据
            if (method.canOutputStream()) {
                os = conn.getOutputStream();
                mRequest.onWriteBody(os);
            }

            //3.读取响应
            code = conn.getResponseCode();
            if (hasRepBody(method, code)) {
                is = getInputStream(conn, code);
                baos = new ByteArrayOutputStream();
                int len = 0;
                byte[] buf = new byte[1024 * 2];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                mRepBody = baos.toByteArray();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //4.解析服务器响应数据
        T result = mRequest.parseResponse(mRepBody);

        Response<T> response = new Response<T>(mRequest, 200, null, null);
        response.setRepBody(result);

        //5.发送数据到主线程
        Message msg = new Message(response, mListener);
        Poster.newInstance().post(msg);
    }

    /**
     * 根据响应码拿到客户端输入流
     *
     * @param conn HttpURLConnection
     * @param code 响应码
     * @return 服务端到客户端的输入流
     */
    private InputStream getInputStream(HttpURLConnection conn, int code) throws IOException {
        InputStream is = null;
        if (code >= 400) {
            is = conn.getErrorStream();
        } else {
            is = conn.getInputStream();
        }
        String contentEncoding = conn.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
            is = new GZIPInputStream(is);
        }
        return is;
    }

    /**
     * 判断是否有响应体
     *
     * @param method 请求方法
     * @param code   响应码
     * @return 是否有响应体
     */
    private boolean hasRepBody(RequestMethod method, int code) {
        return method != RequestMethod.HEAD
                && !(100 <= code && code < 200)
                && code != 204 && code != 205
                && !(300 <= code && code < 400);
    }

    /**
     * 为HttpURLConnection设置请求头
     *
     * @param conn    HttpURLConnection
     * @param request 请求头
     */
    private void setRequestHead(HttpURLConnection conn, Request request) {
        Map<String, String> requestHead = mRequest.getRequestHead();
        //处理Content-Type
        String contentType = request.getContentType();
        requestHead.put("Content-Type", contentType);

        //处理ContentLength
        long contentLength = request.getContentLength();
        requestHead.put("Content-Length", Long.toString(contentLength));

        for (Map.Entry<String, String> entry : requestHead.entrySet()) {
            String headKey = entry.getKey();
            String headValue = entry.getValue();
            L.d(headKey + " = " + headValue + L.l());

            conn.setRequestProperty(headKey, headValue);
        }

    }

    private void handleHttps(HttpURLConnection conn) {
        //https处理
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection conns = (HttpsURLConnection) conn;

            SSLSocketFactory ssl = mRequest.getSSLSocketFactory();
            if (ssl != null) {
                conns.setSSLSocketFactory(ssl);
            }

            HostnameVerifier host = mRequest.getHostnameVerifier();
            if (host != null) {
                conns.setHostnameVerifier(host);//
            }
        }
    }


}
