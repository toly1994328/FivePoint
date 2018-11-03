package top.toly.zutils.core.zhttp.urlconn;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/17 0017:9:17<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：把OKHttp转化为URLConnection 单例模式
 */
public class URLConnFactory {
    private static URLConnFactory sURLConnFactory;

    public static URLConnFactory newInstance() {
        if (sURLConnFactory == null) {
            synchronized (URLConnFactory.class) {
                if (sURLConnFactory == null) {
                    sURLConnFactory = new URLConnFactory();
                }
            }
        }
        return sURLConnFactory;
    }

    private OkHttpClient mOkHttpClient;

    private URLConnFactory() {
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 打开一个url
     *
     * @param url 地址
     * @return HttpURLConnection {@link HttpURLConnection}
     */
    public HttpURLConnection openUrl(URL url) {
        return openUrl(url, null);
    }

    /**
     * 打开一个url
     *
     * @param url   地址
     * @param proxy 代理
     * @return HttpURLConnection {@link HttpURLConnection}
     */
    public HttpURLConnection openUrl(URL url, Proxy proxy) {
        String protocol = url.getProtocol();
        OkHttpClient copy = mOkHttpClient.newBuilder()
                .proxy(proxy)
                .build();

        if (protocol.equals("http")) return new OkHttpURLConnection(url, copy);
        if (protocol.equals("https")) return new OkHttpsURLConnection(url, copy);
        throw new IllegalArgumentException("Unexpected protocol: " + protocol);

    }

}
