package top.toly.zutils._recycler_view.ev;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import top.toly.zutils.view.listview.ev.ItemBean;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/11:9:11
 * 邮箱：1981462002@qq.com
 * 说明：多样式的RecyclerView适配器
 */
public abstract class MyRVsAdapter<T> extends RecyclerView.Adapter<MyRVsHolder<T>> {
    private final Context mCtx;
    /**
     * 数据
     */
    private List<T> mDatas;


    /**
     * 布局映射
     */
    protected Map<Integer, Integer> mTypeMap;

    /**
     * 类型
     */
    private int mType;


    public MyRVsAdapter(Context ctx, List<T> datas, Map<Integer, Integer> typeMap) {
        mDatas = datas;
        mCtx = ctx;
        mTypeMap = typeMap;
    }



    @Override
    public MyRVsHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        mType = viewType;
        View itemView = LayoutInflater.from(mCtx).inflate(mTypeMap.get(viewType), null);

        final MyRVsHolder myRVHolder = new MyRVsHolder<T>(itemView,viewType);
//        myRVHolder.setIsRecyclable(false);//TODO 设置不可用缓存
        myRVHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnRvItemClick(v, myRVHolder.getPos());
                }
            }
        });
        return myRVHolder;
    }

    @Override
    public void onBindViewHolder(MyRVsHolder<T> holder, final int position) {

        setData(holder, mDatas.get(position), position,mType);
    }

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
    public int getItemCount() {
        return mDatas.size();
    }

///////////////////////////////////////////////////////////

    public abstract void setData(MyRVsHolder<T> holder, T data, int position, int type);

    /**
     * 添加item
     *
     * @param i
     * @param aNew
     */
    public void addData(int i, T aNew) {
        mDatas.add(i, aNew);
        notifyItemInserted(i);//刷新数据
    }

    /**
     * 删除item
     *
     * @param i
     */
    public void deleteData(int i) {
        mDatas.remove(i);
        notifyItemRemoved(i);//刷新数据
    }

/////////////////为RecyclerView设置点击监听接口/////////////////////////

    /**
     * 为RecyclerView设置点击监听接口
     */
    public interface OnItemClickListener {
        void OnRvItemClick(View v, int pos);//item被点击的时候回调方法
    }

    /**
     * 声明监听器接口对象
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView某个的监听方法
     *
     * @param onItemClickListener
     */
    public void setOnRvItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
