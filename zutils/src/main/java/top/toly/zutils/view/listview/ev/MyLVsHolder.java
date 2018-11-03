package top.toly.zutils.view.listview.ev;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/6:11:30
 * 邮箱：1981462002@qq.com
 * 说明：MyLVHolder
 */
public class MyLVsHolder<T> {

    /**
     * 条目内部控件的view集合
     */
    private SparseArray<View> mViews;
    /**
     * 条目视图
     */
    private View mItemView;

    /**
     * @param ctx     上下文
     * @param typeMap 布局ID
     * @param type    类型
     */
    public MyLVsHolder(Context ctx, View convertView, Map<Integer, Integer> typeMap, int type) {
        mViews = new SparseArray<>();
        //生成条目的View
        if (convertView == null) {
            mItemView = LayoutInflater.from(ctx).inflate(typeMap.get(type), null);
        } else {
            mItemView = convertView;
            //用MyLVHolder为条目View设置标签
            mItemView.setTag(this);
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId 条目内部控件的id
     * @param <W>    数据泛型
     * @return view
     */
    public <W extends View> W getView(int viewId) {
        //通过viewId为键获取View值
        View view = mViews.get(viewId);
        //如果view为空
        if (view == null) {
            //条目findViewById获取View
            view = mItemView.findViewById(viewId);
            //以id为键，View为值存入mViews集合
            mViews.put(viewId, view);
        }
        return (W) view;
    }

    public View getConvertView() {
        return mItemView;
    }

    /**
     * 设置TextView文本方法
     *
     * @param viewId 条目内部控件的id
     * @param text   文本
     * @return MyLVHolder对象
     */
    public MyLVsHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(text);
        }

        return this;
    }

    /**
     * 通过id设置图片
     *
     * @param viewId 条目内部控件的id
     * @param resId  资源id
     * @return MyLVHolder对象
     */
    public MyLVsHolder setImageViewRes(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 通过id设置图片
     *
     * @param viewId 条目内部控件的id
     * @param bitmap 图片
     * @return MyLVHolder对象
     */
    public MyLVsHolder setImageViewBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }
}
