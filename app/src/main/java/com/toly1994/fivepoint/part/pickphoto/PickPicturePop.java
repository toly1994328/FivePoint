//package com.toly1994.fivepoint.part.pickphoto;
//
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import java.util.List;
//
//import top.toly.fivepiont.R;
//import top.toly.www.zlibrary.bean.PictureFolderBean;
//import utils.ev.listview.MyLVAdapter;
//import utils.ev.listview.MyLVHolder;
//import utils.ui.ImageLoder;
//import utils.ui.ScrUtil;
//import utils.ui.UIUtils;
//
///**
// * 作者：张风捷特烈
// * 时间：2018/4/18:16:11
// * 邮箱：1981462002@qq.com
// * 说明：
// */
//public class PickPicturePop extends PopupWindow {
//
//    private int mWidth;
//    private int mHeight;
//    private ListView mListView;
//
//    private List<PictureFolderBean> mDatas;
//    private final View mInflate;
//
//    public PickPicturePop(Context context, List<PictureFolderBean> datas) {
//        super(context);
//        mDatas = datas;
//
//        mWidth = ScrUtil.getScreenWidth();
//        mHeight = (int) (ScrUtil.getScreenHeight() * 0.75f);
//        mInflate = UIUtils.inflate(R.layout.pick_picture_pop_main);
//
//        setContentView(mInflate);
//        setWidth(mWidth);
//        setHeight(mHeight);
//        setFocusable(true);
//        setTouchable(true);
//        setOutsideTouchable(true);
//        setBackgroundDrawable(new BitmapDrawable());
//        setAnimationStyle(R.style.slide_anim);//设置mPopWindow进出动画
//        setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        initViews();
//        initEvent();
//
//    }
//
//    private void initEvent() {
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mOnPopClickListener != null) {
//                    mOnPopClickListener.onPopClick(mDatas.get(position));
//                }
//            }
//        });
//    }
//
//
//    private void initViews() {
//        mListView = mInflate.findViewById(R.id.lv_pop);
//        mListView.setAdapter(new MyLVAdapter<PictureFolderBean>(mDatas, R.layout.pick_picture_item_pop) {
//            @Override
//            public void setDatas(MyLVHolder holder, PictureFolderBean data, int position) {
//                ImageView iv_pop = holder.getView(R.id.iv_pop);
//                TextView tv_name = holder.getView(R.id.tv_name);
//                TextView tv_count = holder.getView(R.id.tv_count);
//                //重置iv_pop默认图片，不然会暂时复用
//                iv_pop.setImageResource(R.drawable.no_photo);
//                ImageLoder.getInstance(3, ImageLoder.Type.LIFO)
//                        .loadImage(data.getFirstImgPath(), iv_pop);
//
//                tv_name.setText(data.getName());
//                tv_count.setText(data.getCount() + "-张图片");
//
//            }
//        });
//
//    }
//
//    ////////////////////////////接口/////////////////////////////
//    public interface OnPopClickListener {
//        void onPopClick(PictureFolderBean folderBean);
//    }
//
//    public OnPopClickListener mOnPopClickListener;
//
//    public void setOnPopClickListener(OnPopClickListener onPopClickListener) {
//        mOnPopClickListener = onPopClickListener;
//
//    }
//}
