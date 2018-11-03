package top.toly.zutils.view.listview.ev;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/6:11:30
 * 邮箱：1981462002@qq.com
 * 说明：MyLVHolder
 */
public class MyLVHolder {

    /**
     * 条目内部控件的view集合
     */
    private SparseArray<View> mViews;
    /**
     * 位置
     */
    private int mPosition;
    /**
     * 条目视图
     */
    private View mItemView;
    private List<Integer> mPos;

    /**
     * @param ctx      上下文
     * @param parent   parent
     * @param layoutId 布局ID
     * @param position 位置
     */
    public MyLVHolder(Context ctx, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<>();
        //生成条目的View
        mItemView = LayoutInflater.from(ctx).inflate(layoutId, parent, false);
        //用MyLVHolder为条目View设置标签
        mItemView.setTag(this);
    }

    /**
     * 获取 MyLVHolder 对象
     *
     * @param ctx         上下文
     * @param convertView convertView
     * @param parent      parent
     * @param layoutId    布局ID
     * @param position    位置
     * @return MyLVHolder 对象
     */
    public static MyLVHolder get(Context ctx, View convertView, ViewGroup parent, int layoutId, int position) {
        //convertView为空
        if (convertView == null) {
            //创建MyLVHolder对象
            return new MyLVHolder(ctx, parent, layoutId, position);


        } else {
            //复用MyLVHolder
            MyLVHolder holder = (MyLVHolder) convertView.getTag();
            holder.mPosition = position;//更新position
            return holder;
        }
    }

    public List<Integer> handleCheckBox(int checkBoxId) {
        mPos = new ArrayList<>();
        final CheckBox cb = this.getView(checkBoxId);
        cb.setChecked(false);
        if (mPos.contains(this.getPosition())) {
            cb.setChecked(true);
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    mPos.add(getPosition());
                } else {
                    mPos.remove((Integer) getPosition());
                }
            }
        });
        return mPos;//返回选中的CheckBox位置集合
    }


    public View getItemView() {
        return mItemView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId 条目内部控件的id
     * @param <T>    数据泛型
     * @return view
     */
    public <T extends View> T getView(int viewId) {
        //通过viewId为键获取View值
        View view = mViews.get(viewId);
        //如果view为空
        if (view == null) {
            //条目findViewById获取View
            view = mItemView.findViewById(viewId);
            //以id为键，View为值存入mViews集合
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mItemView;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 设置TextView文本方法
     *
     * @param viewId 条目内部控件的id
     * @param text 文本
     * @return MyLVHolder对象
     */
    public MyLVHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (view!=null) {
            view.setText(text);
        }

        return this;
    }

    /**
     * 通过id设置图片
     * @param viewId 条目内部控件的id
     * @param resId 资源id
     * @return MyLVHolder对象
     */
    public MyLVHolder setImageViewRes(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 通过id设置图片
     * @param viewId 条目内部控件的id
     * @param bitmap 图片
     * @return MyLVHolder对象
     */
    public MyLVHolder setImageViewBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }
}
