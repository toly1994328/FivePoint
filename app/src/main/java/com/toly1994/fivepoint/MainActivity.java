package com.toly1994.fivepoint;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.app.SPCons;
import com.toly1994.fivepoint.loge.ParseUtils;
import com.toly1994.fivepoint.presenter.WuZiPresenter;
import com.toly1994.fivepoint.service.MusicService;
import com.toly1994.fivepoint.socket.client.ClientHelper;
import com.toly1994.fivepoint.socket.client.IConnCallback;
import com.toly1994.fivepoint.socket.server.IAcceptCallback;
import com.toly1994.fivepoint.socket.server.ServerHelper;
import com.toly1994.fivepoint.view.IView;
import com.toly1994.fivepoint.view.SettingPop;
import com.toly1994.fivepoint.view.WuZiView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.toly.zutils.core.base.PermissionActivity;
import top.toly.zutils.core.compat.CompatNavigationBar;
import top.toly.zutils.core.http.IpUtils;
import top.toly.zutils.core.io.sp.DataType;
import top.toly.zutils.core.io.sp.SPFactory;
import top.toly.zutils.core.io.sp.Shareable;
import top.toly.zutils.core.shortUtils.Change;
import top.toly.zutils.core.shortUtils.L;
import top.toly.zutils.core.shortUtils.ToastUtil;
import top.toly.zutils.core.template.NClick;
import top.toly.zutils.core.ui.anima.AimUtils;

public class MainActivity extends PermissionActivity implements IView, Runnable {

    @BindView(R.id.i_wuzi)
    WuZiView mIWuzi;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.iv_current)
    ImageView mIvCurrent;
    @BindView(R.id.id_tv_count)
    TextView mIdTvCount;
    private SettingPop mSettingPop;
    private WuZiPresenter mPresenter;
    private ClientHelper mClientHelper;
    private ServerHelper mServerHelper;

    private boolean drawByServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        ButterKnife.bind(this);
        CompatNavigationBar.handle(this);

        applyPermissions(_WRITE_EXTERNAL_STORAGE());
    }

    @Override
    protected void permissionOk() {

        mPresenter = new WuZiPresenter(this);
        mClientHelper = new ClientHelper("192.168.43.39", 8080);
//        mClientHelper = new ClientHelper("192.168.234.231", 8080);

        mPresenter.attach(this);
        mPresenter.loadSound(this);

        initWuZi();

        mPresenter.renderFromFile();

        useFab();//使用fab按钮，弹出设置页
        bakeStep();//三击悔棋
        initPopWindow();//设置
    }

    private void initWuZi() {
        //设置游戏结束监听
        mIWuzi.setOnGameOverListener(this::winView);

        //设置绘制监听
        mIWuzi.setOnDrawListener((iswhite) -> {
            if (drawByServer) {
                drawByServer = false;
                return;
            }

            mPresenter.playSound();
            //更换图片和文字
            Shareable sp = SPFactory.getSP(DataType.INT);
            int whiteImg = (int) sp.get(SPCons.SP_WHITE_IMG, SPCons.WHITE_IMG);
            int blackImg = (int) sp.get(SPCons.SP_BLACK_IMG, SPCons.BLACK_IMG);
            mIvCurrent.setImageResource(iswhite ? whiteImg : blackImg);
            mIdTvCount.setText(iswhite ? "白/" + mIWuzi.getWhites().size() + "个" : "黑/" + mIWuzi.getBlacks().size() + "个");
            //设置动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mIvCurrent, "ScaleX", 1f, .5f, 1f).setDuration(300);//设置时常
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mIvCurrent, "ScaleY", 1f, .5f, 1f).setDuration(300);//设置时常;
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mIvCurrent, "alpha", 1f, .1f, 1f).setDuration(300);//设置时常;
            AnimatorSet set = new AnimatorSet();
            set.play(scaleX).with(scaleY).with(alpha);
            set.start();
            mPresenter.savePoint2File(mIWuzi.getWhites(), mIWuzi.getBlacks());
            mClientHelper.writePos2Service(mIWuzi.getWhites(), mIWuzi.getBlacks());
//            mClientHelper.writePos2Service(mIWuzi.getCurrentPos());
        });

    }

    /**
     * 设置
     */
    private void initPopWindow() {
        mSettingPop = new SettingPop(MainActivity.this, mIWuzi, mRlRoot);
        mSettingPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                AimUtils.lightOn(MainActivity.this);
                mPresenter.setBackground4View(mRlRoot);//设置背景图片
            }
        });
    }


    /**
     * 使用fab按钮
     */
    private void useFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            mSettingPop.showAsDropDown(mRlRoot, 0, -Change.dp2px(MainActivity.this, 84));
            AimUtils.lightOff(MainActivity.this);
        });


        fab.setOnLongClickListener(v -> {
            mClientHelper.conn2Server(new IConnCallback() {
                @Override
                public void onStart() {
                    L.d("onStart" + L.l());
                }

                @Override
                public void onError(Exception e) {
                    L.d("onError" + L.l());
                }

                @Override
                public void onFinish(String msg) {
                    //已在主线程
                    ToastUtil.show(MainActivity.this, msg);
                    //开启接收服务器数据的轮回线程
                    new Thread(MainActivity.this).start();
                    L.d("onConnect" + L.l());
                }
            });
            return true;
        });
    }


    /**
     * 三击悔棋
     */
    private void bakeStep() {
        new NClick(mRlRoot, 3, 500) {
            @Override
            public void run() {
                mIWuzi.backStep();
                mPresenter.savePoint2File(mIWuzi.getWhites(), mIWuzi.getBlacks());
            }
        };

        mRlRoot.setOnLongClickListener(v -> {
            if (mServerHelper != null) {
                return false;
            }
            new Thread(() -> {
                mServerHelper = new ServerHelper().open(new IAcceptCallback() {
                    @Override
                    public void onConnect(String msg) {
                        ToastUtil.showAtOnce(MainActivity.this, msg);
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                });

                runOnUiThread(() -> {
                    ToastUtil.showAtOnce(this, IpUtils.getIPAddress(this).toString());
                    L.d(IpUtils.getIPAddress(this).toString() + L.l());
                });
            }).start();

            return false;
        });

    }

    @Override
    public void updateView(ArrayList<Point> white, ArrayList<Point> black) {
        mIWuzi.setPoints(white, black);
        mIWuzi.invalidate();
    }

    @Override
    public void winView(boolean isWhiteWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("五子棋提示");
        builder.setMessage(isWhiteWin ? "白棋胜利！" : "黑棋胜利！");
        builder.setPositiveButton("再来一局", (dialog, which) -> mIWuzi.start());
        builder.setNegativeButton("退出", null);
        builder.show();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(this, MusicService.class));
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msgFromServer != null) {
            mServerHelper.close();
        }
        mPresenter.onDestroy();


    }

    private String msgFromServer;

    @Override
    public void run() {
        while (true) {
            try {//每隔200毫秒，间断的监听客户端的发送消息情况
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                msgFromServer = mClientHelper.getDis().readUTF();
                runOnUiThread(() -> {

                    String[] split = msgFromServer.split("#");
                    if (split.length > 0) {
                        ArrayList<Point> whitePoints = ParseUtils.parseData(split[0]);
                        ArrayList<Point> blackPoints = new ArrayList<>();
                        if (split.length > 1) {
                            blackPoints = ParseUtils.parseData(split[1]);
                        }
                        drawByServer = true;
                        mIWuzi.setPoints(whitePoints, blackPoints);
                        ToastUtil.showAtOnce(MainActivity.this, msgFromServer);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
