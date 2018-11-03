package top.toly.zutils.core.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/7 0007:14:54<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class IpUtils {
    private static final String TAG = "IpUtils";


    /**
     * 获取IPv4
     *
     * @param context
     * @return
     */
    public static Properties getIPAddress(Context context) {
        //将ip信息保存在Properties对象中
        Properties prop = new Properties();
        //获取网络信息
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        //网络信息非空，并且已连接
        if (info != null && info.isConnected()) {
            //当前使用2G/3G/4G网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    //获取NetworkInterface的迭代枚举
                    Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                    while (nis.hasMoreElements()) {//开始迭代
                        NetworkInterface ni = nis.nextElement();//获取每一个NetworkInterface
                        //获取InetAddress的迭代枚举
                        Enumeration<InetAddress> ias = ni.getInetAddresses();
                        while (ias.hasMoreElements()) {//开始迭代
                            InetAddress ia = ias.nextElement();//获取每一个InetAddress
                            //只去IPv4
                            if (ia instanceof Inet4Address) {
                                //将属性设置到集合
                                prop.setProperty(ni.getDisplayName(), ia.getHostAddress());
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                //通过WIFI管理者获取Wifi信息
                WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
                //将属性设置到集合
                prop.setProperty("ip", int2StrIP(wifiInfo.getIpAddress()));//得到IPV4地址
                prop.setProperty("name", wifiInfo.getSSID());
                prop.setProperty("BSSID", wifiInfo.getBSSID());
                prop.setProperty("Mac", wifiInfo.getMacAddress());
            }
        } else {
            //当前无网络连接
            prop.setProperty("ERROR", "请打开网络");
        }
        return prop;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String int2StrIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
