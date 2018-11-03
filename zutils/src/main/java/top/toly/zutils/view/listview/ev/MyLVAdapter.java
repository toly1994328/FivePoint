package top.toly.zutils.view.listview.ev;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/6:13:51
 * 邮箱：1981462002@qq.com
 * 说明：ListView适配器
 */
public abstract class MyLVAdapter<T> extends BaseAdapter {
    /**
     * 数据
     */
    protected List<T> mDatas;
    /**
     * 布局ID
     */
    protected int mLayoutId;
    /**
     * 上下文
     */
    protected Context mCtx;


    public MyLVAdapter(Context ctx, T[] datas, int listId) {
        mCtx = ctx;
        mDatas = new ArrayList<>();
        mDatas.addAll(Arrays.asList(datas));
        mLayoutId = listId;
    }

    public MyLVAdapter(Context ctx, List<T> datas, int listId) {
        mCtx = ctx;
        mDatas = datas;
        mLayoutId = listId;
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
        MyLVHolder holder = MyLVHolder.get(mCtx, convertView, parent, mLayoutId, position);
        setData(holder, getItem(position), position);
        return holder.getConvertView();
    }

    /**
     * 设置数据抽象方法
     *
     * @param holder   MyLVHolder
     * @param data     数据
     * @param position 位置
     */
    public abstract void setData(MyLVHolder holder, T data, int position);

}
