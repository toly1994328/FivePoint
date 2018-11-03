package top.toly.zutils.view.listview.ev;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/6:13:51
 * 邮箱：1981462002@qq.com
 * 说明：ListView适配器
 */
public abstract class MyLVsAdapter<T> extends BaseAdapter {
    /**
     * 数据
     */
    protected List<T> mDatas;
    /**
     * 布局映射
     */
    protected Map<Integer, Integer> mTypeMap;
    /**
     * 上下文
     */
    protected Context mCtx;


    public MyLVsAdapter(Context ctx, List<T> datas, Map<Integer, Integer> typeMap) {
        mCtx = ctx;
        mDatas = datas;
        mTypeMap = typeMap;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建 MyLVHolder 对象

        T t = mDatas.get(position);
        int type = ((ItemBean) t).getType();
        MyLVsHolder holder = new MyLVsHolder(mCtx, convertView, mTypeMap, type);
        setData(holder, getItem(position), position, type);

        return holder.getConvertView();
    }

    /**
     * 设置数据抽象方法
     *
     * @param holder   MyLVHolder
     * @param data     数据
     * @param position 位置
     * @param type
     */
    public abstract void setData(MyLVsHolder holder, T data, int position, int type);

    /**
     * 根据数据源的position返回需要显示的的layout的type
     *
     * @param position 位置
     * @return 某位置的类型
     */
    @Override
    public int getItemViewType(int position) {
        T t = mDatas.get(position);
        return ((ItemBean) t).getType();
    }

    @Override
    public int getViewTypeCount() {
        return mTypeMap.size();
    }
}
