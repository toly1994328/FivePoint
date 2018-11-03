package top.toly.zutils.core.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：张风捷特烈
 * 时间：2018/5/16 14:55
 * 邮箱：1981462002@qq.com
 * 说明：申请权限的Activity父类
 * <p>
 * 用法：1：继承 PermissionActivity
 * 2：调用applyPermissions
 * 3：permissionOk()回调
 */
public abstract class PermissionActivity extends AppCompatActivity {
    /**
     * 读写SD卡权限标识码
     */
    private static final int WRITE_EXTERNAL_STORAGE = 0x01;
    /**
     * 录音权限标识码
     */
    private static final int RECORD_AUDIO = 0x02;
    /**
     * 相机权限标识码
     */
    private static final int CAMERA = 0x03;
    /**
     * 联系人权限标识码
     */
    private static final int READ_CONTACTS = 0x04;
    /**
     * 打电话权限标识码
     */
    private static final int CALL_PHONE = 0x05;
    /**
     * 短信权限标识码
     */
    private static final int READ_SMS = 0x06;
    /**
     * 短信权限标识码
     */
    private static final int LOCATION = 0x07;

    private PermissionBean mStorageModel, mRecorderModel,
            mCameraModel, mContactsModel, mCallPhoneModel, mSMSModel,mLocationModel;
    private PermissionBean[] mModels;
    /**
     * 当前的权限个数记录码
     */
    private int count = 0;
    /**
     * 权限未允许
     */
    protected boolean noPerm = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 申请一个权限
     */
    private void applyPermission(PermissionBean model) {

        noPerm = PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission);
        if (noPerm) {
            ActivityCompat.requestPermissions(this, new String[]{model.permission}, model.requestCode);
            count++;
        }
    }

    /**
     * 申请所有权限
     */
    public void applyPermissions(PermissionBean... models) {
        mModels = models;
        applyPermission(models[count]);
        if (!noPerm) {
            permissionOk();
        }
    }

    /**
     * 申请SD卡读写权限
     */
    public PermissionBean _WRITE_EXTERNAL_STORAGE() {
        mStorageModel = new PermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "我们需要您允许我们读写你的存储卡，以方便使用该功能!", WRITE_EXTERNAL_STORAGE);
        return mStorageModel;
    }

    /**
     * 申请读取通讯录权限
     */
    public PermissionBean _READ_CONTACTS() {
        mContactsModel = new PermissionBean(Manifest.permission.READ_CONTACTS,
                "我们需要您允许我们读取通讯录，以方便功能使用!", READ_CONTACTS);
        return mContactsModel;
    }

    /**
     * 申请读取通讯录权限
     */
    public PermissionBean _READ_SMS() {
        mSMSModel = new PermissionBean(Manifest.permission.READ_SMS,
                "我们需要您允许我们读取短信，以方便功能使用!", READ_SMS);
        return mSMSModel;
    }

    /**
     * 申请录音权限
     */
    public PermissionBean _RECORD_AUDIO() {
        mRecorderModel = new PermissionBean(Manifest.permission.RECORD_AUDIO,
                "我们需要您允许我们访问录音设备，以方便录音功能使用!", RECORD_AUDIO);
        return mRecorderModel;
    }

    /**
     * 申请拍照权限
     */
    public PermissionBean _CAMERA() {
        mCameraModel = new PermissionBean(Manifest.permission.CAMERA,
                "我们需要您允许我们访问相机，以方便拍照功能使用!", CAMERA);
        return mCameraModel;
    }

    /**
     * 申请电话权限
     */
    public PermissionBean _CALL_PHONE() {
        mCallPhoneModel = new PermissionBean(Manifest.permission.CALL_PHONE,
                "我们需要您允许我们访问电话设备，以方便功能使用!", CALL_PHONE);
        return mCallPhoneModel;
    }

    /**
     * 申请地理位置权限
     */
    public PermissionBean _ACCESS_COARSE_LOCATION() {
        mLocationModel = new PermissionBean(Manifest.permission.ACCESS_COARSE_LOCATION,
                "我们需要您允许我们访问电话设备，以方便功能使用!", LOCATION);
        return mLocationModel;
    }

    /**
     * 当用户处理完授权操作时，系统会自动回调该方法
     *
     * @param requestCode 权限标识码
     * @param permissions 权限集
     * @param grantResults 允许的结果集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (count != mModels.length) {
                applyPermission(mModels[count]);
            } else {
                permissionOk();
                count=0;//count归零
            }
        } else {
            count--;
        }


        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE:
                showInfoDialog(permissions, grantResults[0], mStorageModel);
                break;
            case RECORD_AUDIO:
                showInfoDialog(permissions, grantResults[0], mRecorderModel);
                break;
            case CAMERA:
                showInfoDialog(permissions, grantResults[0], mCameraModel);
                break;
            case READ_CONTACTS:
                showInfoDialog(permissions, grantResults[0], mContactsModel);
                break;
            case CALL_PHONE:
                showInfoDialog(permissions, grantResults[0], mCallPhoneModel);
                break;
            case READ_SMS:
                showInfoDialog(permissions, grantResults[0], mSMSModel);
            case LOCATION:
                showInfoDialog(permissions, grantResults[0], mLocationModel);
                break;
        }

    }

    /**
     * 如果拒绝，弹出对话框，说明为什么需要这个权限
     *
     * @param permissions 权限集
     * @param grantResult 允许的结果
     * @param model 权限实体
     */
    private void showInfoDialog(String[] permissions, int grantResult, final PermissionBean model) {
        if (PackageManager.PERMISSION_GRANTED != grantResult) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("权限申请")
                        .setMessage(model.explain).setPositiveButton(
                                "我知道了",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        applyPermission(model);
                                    }
                                }
                        );
                builder.setCancelable(false);
                builder.show();
            }
        }
        return;
    }

    /**
     * Permission的实体类
     */
    public static class PermissionBean {
        /**
         * 请求的权限
         */
        public String permission;
        /**
         * 解析为什么请求这个权限
         */
        public String explain;
        /**
         * 请求码
         */
        public int requestCode;

        /**
         *
         * @param permission 权限
         * @param explain 解析
         * @param requestCode 请求码
         */
        public PermissionBean(String permission, String explain, int requestCode) {
            this.permission = permission;
            this.explain = explain;
            this.requestCode = requestCode;
        }

    }

    /**
     * 权限成功回调
     */
    protected abstract void permissionOk();
}


