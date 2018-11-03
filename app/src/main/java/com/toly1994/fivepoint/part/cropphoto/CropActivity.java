//
//package com.toly1994.fivepoint.part.cropphoto;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//
//import com.toly1994.fivepoint.R;
//import com.toly1994.fivepoint.app.SPCons;
//
//import top.toly.reslib.my_design.cropper.CropImageView;
//import top.toly.zutils.core.io.sp.SpUtils;
//import top.toly.zutils.core.ui.common.BMUtils;
//
//
//public class CropActivity extends Activity {
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_crop);
//
//        String stringExtra = getIntent().getStringExtra("PHOTO_PATH");
//
//        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
//        final Button cropButton = (Button) findViewById(R.id.Button_crop);
//        ImageLoder.getInstance().loadImage(stringExtra, cropImageView);
//        cropImageView.setFixedAspectRatio(true);
//        cropImageView.setAspectRatio(56, 100);
//
//        cropButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Bitmap croppedImage = cropImageView.getCroppedImage();
//                String bgImg = BMUtils.saveBitmap("快乐五子棋/背景图片/imgs", croppedImage);
//                SpUtils.setString(SPCons.SP_BG_IMG, bgImg);
//                CropActivity.this.finish();
//
//            }
//        });
//    }
//}
