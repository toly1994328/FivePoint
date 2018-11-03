//package com.toly1994.fivepoint.part.pickphoto;
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.view.WindowManager;
//import android.widget.GridView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.toly1994.fivepoint.R;
//import com.toly1994.fivepoint.part.cropphoto.CropActivity;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import top.toly.zutils.core.base.BaseActivity;
//import top.toly.zutils.core.io.FileHelper;
//import top.toly.zutils.core.shortUtils.Change;
//import top.toly.zutils.view.listview.ev.MyLVAdapter;
//
//
//public class PickPictureActivity extends BaseActivity {
//
//    public static final int IMG_LOADED = 0x111;
//    private static final int TO_CROP = 0x112;
//
//    @BindView(R.id.tv_all_photo)
//    TextView mTvAllPhoto;
//    @BindView(R.id.tv_photo_count)
//    TextView mTvPhotoCount;
//    @BindView(R.id.rl_bottom)
//    RelativeLayout mRlBottom;
//    @BindView(R.id.gv_photo)
//    GridView mGvPhoto;
//    @BindView(R.id.tv_select_count)
//    TextView mTvSelectCount;
//    @BindView(R.id.btn_select_count)
//    TextView mBtnSelectCount;
//
//    private List<String> mImgs;
//
//    private List<PictureFolderBean> mFolderBeans = new ArrayList<>();
//
//    private File mCurrentDir;
//    private int mMaxCount;
//    private FileHelper mFileHelper;
//    private ProgressDialog mProgressDialog;
//
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == IMG_LOADED) {
//                mProgressDialog.dismiss();
//                Zutil.toastShow("点击底部可以查看文件夹");
//                data2View();
//                initPopWindow();
//            }
//        }
//    };
//
//
//    private MyLVAdapter mImgAdapter;
//    private PickPicturePop mPopWindow;
//
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pick_picture_activity_home);
//        ButterKnife.bind(this);
//
//        boolean hasPermission = hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (hasPermission) {
//            doPermission();
//        }
//
//
//    }
//
//    @Override
//    public void doPermission() {
//        super.doPermission();
//        initData();
//    }
//
//    /**
//     * 初始化PopWindow
//     */
//    private void initPopWindow() {
//        mPopWindow = new PickPicturePop(this, mFolderBeans);
//        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                lightOn();
//            }
//        });
//    }
//
//    /**
//     * 内容区域变亮
//     */
//    private void lightOn() {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 1.0f;
//        getWindow().setAttributes(lp);
//    }
//
//    /**
//     * 内容区域变暗
//     */
//    private void lightOff() {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = .3f;
//        getWindow().setAttributes(lp);
//
//    }
//
//    private void data2View() {
//        if (mCurrentDir == null) {
//            Zutil.toastShow("未扫描到任何图片");
//            return;
//        }
//        mImgs = Arrays.asList(mCurrentDir.list());
//        mImgAdapter = new PickPictureImgAdapter(mImgs, R.layout.simple_item_iv, mCurrentDir, mTvSelectCount);
//
//        mGvPhoto.setAdapter(mImgAdapter);
//        mTvPhotoCount.setText("当前" + mMaxCount + "张");
//        mTvAllPhoto.setText("选择文件夹");
//
//    }
//
//
//    /**
//     * 利用cp扫描手机图片
//     */
//    private void initData() {
////        PickPictureImgAdapter.mSelectedImg.clear();
//        mFileHelper = FileHelper.getFileHelper();
//        if (mFileHelper.hasSdCard()) {
//
//        } else {
//            Zutil.toastShow("当前存储卡不可用");
//            return;
//        }
//        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
//
//        new Thread() {
//            @Override
//            public void run() {
//                //查询获得游标
//                Uri mIngUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                ContentResolver resolver = PickPictureActivity.this.getContentResolver();
//                Cursor cursor = resolver.query(mIngUri, null,
//                        MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=?",
//                        new String[]{"image/jpeg", "image/png"},
//                        MediaStore.Images.Media.DATE_MODIFIED);
//
//                //通过游标获取path，创建folderBean对象并赋值
//                Set<String> mDirPaths = new HashSet<>();//为避免重复扫描，将dirPath放入集合
//                while (cursor.moveToNext()) {
//                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                    File parentFile = new File(path).getParentFile();
//                    if (parentFile == null) continue;
//
//                    PictureFolderBean folderBean = null;
//                    String dirPath = parentFile.getAbsolutePath();
//                    if (mDirPaths.contains(dirPath)) {
//                        continue;
//                    } else {
//                        mDirPaths.add(dirPath);//获取文件夹所在路径
//                        folderBean = new PictureFolderBean();
//                        folderBean.setDir(dirPath);
//                        folderBean.setFirstImgPath(path);//图片的路径
//                    }
//
//                    if (parentFile.list() == null) {
//                        continue;
//                    } else {//过滤文件夹中的所有文件以.jpg，.png.jpeg结尾的
//                        int imgCount = parentFile.list(new FilenameFilter() {
//                            @Override
//                            public boolean accept(File dir, String name) {
//                                if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
//                                    return true;
//                                }
//                                return false;
//                            }
//                        }).length;
//
//                        folderBean.setCount(imgCount);
//                        mFolderBeans.add(folderBean);
//
//                        if (imgCount > mMaxCount) {
//                            mMaxCount = imgCount;
//                            mCurrentDir = parentFile;
//                        }
//                    }
//                }
//                cursor.close();
//                mHandler.sendEmptyMessage(IMG_LOADED);//通知mHandler扫描完成
//            }
//
//        }.start();
//
//    }
//
//
//    @OnClick(R.id.rl_bottom)
//    public void onViewClicked() {
//        mPopWindow.showAsDropDown(mRlBottom, 0, -Change.dp2px(48));
//        lightOff();
//        mPopWindow.setOnPopClickListener(new PickPicturePop.OnPopClickListener() {
//            @Override
//            public void onPopClick(PictureFolderBean folderBean) {
//
//                mCurrentDir = new File(folderBean.getDir());
//
//                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
//                    @Override
//                    public boolean accept(File dir, String name) {
//                        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
//                            return true;
//                        }
//                        return false;
//                    }
//                }));
//
//                mImgAdapter = new PickPictureImgAdapter(mImgs, R.layout.simple_item_iv, mCurrentDir.getAbsoluteFile(), mTvSelectCount);
//                mGvPhoto.setAdapter(mImgAdapter);
//                String name = folderBean.getName();
//                String[] strings = name.split("/");
//                mTvAllPhoto.setText(strings[strings.length - 1]);
//                mTvPhotoCount.setText(mImgs.size() + "张");
//                mPopWindow.dismiss();
//
//
//            }
//        });
//
//    }
//
//    /**
//     * 选中图片，打开编辑器
//     */
//    @OnClick(R.id.btn_select_count)
//    public void btn_select_count() {
//        ArrayList<String> paths = new ArrayList<>(PickPictureImgAdapter.mSelectedImg);
//        if (paths.size() == 1) {
//            Intent intent = new Intent(this, CropActivity.class);
//            intent.putExtra("PHOTO_PATH", paths.get(0));
//            startActivity(intent);
//            finish();
//            PickPictureImgAdapter.mSelectedImg.clear();
//        } else {
//            Zutil.toastShow("请选择一张图片!");
//        }
//
//    }
//
//
//}
