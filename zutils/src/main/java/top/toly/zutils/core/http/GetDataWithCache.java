package top.toly.zutils.core.http;//package top.toly.zutils._okhttp;//package top.toly.zutils.core.io;

import android.text.TextUtils;

import java.util.Map;

import top.toly.zutils.core.io.cache.CacheStrategy;
import top.toly.zutils.core.io.cache.CacheWorker;

/**
 * 作者：张风捷特烈
 * 时间：2018/3/29:10:21
 * 邮箱：1981462002@qq.com
 * 说明：网络封装，连接网络获取数据，并写缓存
 */
public abstract class GetDataWithCache {

    private CacheStrategy mCacheStrategy;
    private String mUrl;
    private Map<String, String> mParam;

    public GetDataWithCache(String url, Map<String, String> param, CacheStrategy cacheStrategy) {

        mCacheStrategy = cacheStrategy;
        mUrl = url;
        mParam = param;
        String cache = new CacheWorker(mCacheStrategy).getCache(mUrl);//读缓存

        if (!TextUtils.isEmpty(cache)) {//如果有缓存
            success(cache);//解析数据抽象方法
        }

        getDataFromService();//连接网络获取数据，并写缓存
    }

    /**
     * 从网络获取数据
     */
    private void getDataFromService() {

        new ZHttp(new ZHttp.HttpResponse() {
            @Override
            public void onSuccess(String response) {
                if (response != null) {
                    success(response);
                }
                mCacheStrategy.setCache(mUrl, response, 10);//写缓存
            }

            @Override
            public void onFail(String error) {
                if (mCacheStrategy.getCache(mUrl) != null) {
                    success(mCacheStrategy.getCache(mUrl));
                } else {
                    error(error);
                }
            }
        }).sendGetHttp(mUrl, mParam);
    }


    public abstract void success(String response);

    public abstract void error(String error);


}
