package top.toly.zutils.core.http;//package top.toly.zutils._okhttp;//package top.toly.zutils.core.io;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import top.toly.zutils.core.io.cache.CacheStrategy;
import top.toly.zutils.core.io.cache.CacheWorker;

/**
 * 作者：张风捷特烈
 * 时间：2018/3/29:10:21
 * 邮箱：1981462002@qq.com
 * 说明：网络封装，连接网络获取数据，并写缓存
 */
public abstract class OKGOWithCache {

    private CacheStrategy mCacheStrategy;
    private String mUrl;

    public OKGOWithCache(String url, CacheStrategy cacheStrategy) {

        mCacheStrategy = cacheStrategy;
        mUrl = url;

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

        OkGo.<String>get(mUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null) {
                            success(response.body());
                        }
                        mCacheStrategy.setCache(mUrl, response.body(), 10);//写缓存
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mCacheStrategy.getCache(mUrl) != null) {
                            success(mCacheStrategy.getCache(mUrl));
                        } else {
                            error(response.body());
                        }
                    }
                });
    }

    public abstract void success(String response);
    public abstract void error(String error);


}
