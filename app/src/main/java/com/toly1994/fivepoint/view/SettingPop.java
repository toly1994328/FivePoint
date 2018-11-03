package com.toly1994.fivepoint.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.toly1994.fivepoint.MainActivity;
import com.toly1994.fivepoint.R;
import com.toly1994.fivepoint.app.SPCons;
import com.toly1994.fivepoint.service.MusicService;

import top.toly.reslib.my_design.useful.SwitchButton;
import top.toly.zutils.core.base.BasePopWindow;
import top.toly.zutils.core.io.sp.DataType;
import top.toly.zutils.core.io.sp.SPFactory;
import top.toly.zutils.core.io.sp.Shareable;
import top.toly.zutils.core.io.sp.SpUtils;
import top.toly.zutils.core.shortUtils.StrUtil;
import top.toly.zutils.core.shortUtils.ToastUtil;
import top.toly.zutils.core.ui.common.BMUtils;
import top.toly.zutils.core.ui.common.ScrUtil;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/22:8:55
 * 邮箱：1981462002@qq.com
 * 说明：设置界面PopWindow
 */
public class SettingPop extends BasePopWindow {
    private WuZiView mIWuzi;
    private Context mContext;
    private RelativeLayout mRlRoot;

    public SettingPop(Context context, WuZiView IWuzi, RelativeLayout rlRoot) {
        super(context);
        mIWuzi = IWuzi;
        mRlRoot = rlRoot;
        mContext = context;

        isSoundEffect();//是否开启音效
        isBGM();//是否开启背景音乐
        reStart();//重新开始
        SetLineCont();//设置网格线数
        changeBg();//切换背景
        saveBitmap();//保存截图
    }

    /**
     * 保存截图
     */
    private void saveBitmap() {
        Button id_btn_save = getView(R.id.id_btn_save);
        id_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = ScrUtil.snapShotWithStatusBar((Activity) mContext);
                        String path = "快乐五子棋/截图保存/" + StrUtil.getCurrentTime_yyyyMMddHHmmss();
                        BMUtils.saveBitmap(path, bitmap);
                        ToastUtil.show(mContext, "截图保存成功！\n" + path);
                    }
                });

            }
        });
    }

    /**
     * 切换背景
     */
    private void changeBg() {
        Button id_btn_switch_img = getView(R.id.id_btn_switch_img);
        id_btn_switch_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, PickPictureActivity.class));
                ToastUtil.show(mContext, "长按<更换背景>,上滑\n可还原默认背景哦!");

            }
        });

        id_btn_switch_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                mRlRoot.setBackground(UIUtils.getDrawable(R.drawable.bg0));
//                SpUtils.setString(SPCons.SP_BG_IMG, "");
                return false;
            }
        });

    }


    /**
     * 设置网格线数
     */
    private void SetLineCont() {

        final SpUtils<Integer> spUtils = new SpUtils<>(mContext);

        Button id_btn_grid = getView(R.id.id_btn_grid);
        id_btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("设置格数");
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_setgrid_num, null);
                final EditText id_tv_grid_num = view.findViewById(R.id.id_tv_grid_num);
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num = Integer.parseInt(id_tv_grid_num.getText().toString().trim());
                        if (num > 0) {

                            spUtils.put(SPCons.SP_MAX_LINE, num);


                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            ((Activity) mContext).finish();
                        } else {
                            ToastUtil.show(mContext, "格数不能小于1");
                        }

                    }

                });
                builder.setNegativeButton("退出", null);
                builder.show();
            }
        });
    }

    /**
     * 重新开始
     */
    private void reStart() {
        Button id_btn_refrash = getView(R.id.id_btn_refrash);
        id_btn_refrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIWuzi.start();
            }
        });
    }


    /**
     * 是否开启背景音乐
     */
    private void isBGM() {
        SwitchButton sw_bgm_sound = getView(R.id.sw_bgm_sound);
        sw_bgm_sound.setChecked(false);
        sw_bgm_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mContext.startService(new Intent(mContext, MusicService.class));
                } else {
                    mContext.stopService(new Intent(mContext, MusicService.class));
                }


            }
        });
    }

    /**
     * 是否开启音效按钮
     */
    private void isSoundEffect() {
        //获取按钮
        SwitchButton soundBtn = getView(R.id.sw_sound);

        Shareable shareable = SPFactory.getSP(DataType.BOOLEAN);
        boolean allowSound = (boolean) shareable.get(SPCons.SP_ALLOW_SOUND, SPCons.ALLOW_SOUND);
        soundBtn.setChecked(allowSound);

        soundBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                shareable.put(SPCons.SP_ALLOW_SOUND, true);

            } else {
                shareable.put(SPCons.SP_ALLOW_SOUND, false);
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.pop_main;
    }
}
