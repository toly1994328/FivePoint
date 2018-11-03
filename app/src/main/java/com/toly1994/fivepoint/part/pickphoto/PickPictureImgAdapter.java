//package com.toly1994.fivepoint.part.pickphoto;
//
//import android.graphics.Color;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.io.File;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import top.toly.fivepiont.R;
//import utils.ev.listview.MyLVAdapter;
//import utils.ev.listview.MyLVHolder;
//import utils.ui.ImageLoder;
//
///**
// * 作者：张风捷特烈
// * 时间：2018/4/18:15:22
// * 邮箱：1981462002@qq.com
// * 说明：
// */
//public class PickPictureImgAdapter extends MyLVAdapter<String> {
//
//    private File currentDir;
//    private TextView mTvSelectCount;
//    public static Set<String> mSelectedImg = new HashSet<>();//!!!!!!!!!!!!如果不为static每次刷新时会重置选中集合
//
//    public PickPictureImgAdapter(List<String> datas, int listId, File currentDir, TextView tvSelectCount) {
//        super(datas, listId);
//        this.currentDir = currentDir;
//        mTvSelectCount = tvSelectCount;
//
//    }
//
//    @Override
//    public void setDatas(MyLVHolder holder, final String data, int position) {
//        final ImageView imageView = holder.getView(R.id.iv_item1);
//        final ImageButton imageButton = holder.getView(R.id.ib_item1);
//        imageView.setImageResource(R.drawable.no_photo);
//        imageButton.setImageResource(R.drawable.check_default);
//        imageView.setColorFilter(Color.parseColor("#11000000"));
////        imageButton.setColorFilter(Color.parseColor("#44000000"));
//
//        //加载图片
//        final String filePath = currentDir + File.separator + data;
//        ImageLoder.getInstance(3, ImageLoder.Type.LIFO).loadImage(filePath, imageView);
//
//        //点击选中，添加遮罩
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {//已被选择
//                if (mSelectedImg.contains(filePath)) {
//                    mSelectedImg.remove(filePath);
//                    imageView.setColorFilter(Color.parseColor("#22000000"));
//                    imageButton.setImageResource(R.drawable.check_default);
//                    mTvSelectCount.setText("已选"+mSelectedImg.size()+"张");
//                } else {//未被选择
//                    mSelectedImg.add(filePath);
//                    imageView.setColorFilter(Color.parseColor("#77000000"));
//                    imageButton.setImageResource(R.drawable.check_checked);
//                    mTvSelectCount.setText("已选"+mSelectedImg.size()+"张");
//                }
////                notifyDataSetChanged();
//            }
//        });
//
//        if (mSelectedImg.contains(filePath)) {//如果被选中，添加遮罩
//            imageView.setColorFilter(Color.parseColor("#77000000"));
//            imageButton.setImageResource(R.drawable.check_checked);
//
//        }
//    }
//}
