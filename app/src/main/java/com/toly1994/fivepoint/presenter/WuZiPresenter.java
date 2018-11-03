package com.toly1994.fivepoint.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.SoundPool;
import android.view.View;

import com.toly1994.fivepoint.app.CfgCons;
import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.app.SPCons;
import com.toly1994.fivepoint.loge.ParseUtils;
import com.toly1994.fivepoint.model.IModel;
import com.toly1994.fivepoint.model.PointModel;
import com.toly1994.fivepoint.view.IView;

import java.util.ArrayList;
import java.util.List;

import top.toly.zutils.core.io.FileHelper;
import top.toly.zutils.core.io.sp.DataType;
import top.toly.zutils.core.io.sp.SPFactory;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:10:02<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class WuZiPresenter extends BasePresenter {
    private IView mIView;
    private IModel mPointModel;
    private SoundPool mSp;
    private int mSoundId;

    public WuZiPresenter(IView IView) {
        mIView = IView;
        mPointModel = new PointModel();
    }

    /**
     * 从文件渲染出棋子位置
     */
    public void renderFromFile() {
        ArrayList<Point> whitePos = mPointModel.getData(CfgCons.SAVE_WHITE_PATH);
        ArrayList<Point> blackPos = mPointModel.getData(CfgCons.SAVE_BLACK_PATH);
        if (whitePos == null) {
            return;
        }
        mIView.updateView(whitePos, blackPos);
    }


    public void setBackground4View(View view) {
        String bgPath = (String) SPFactory.getSP().get(SPCons.SP_BG_IMG, "");
        if (!bgPath.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(bgPath);
            BitmapDrawable drawable = new BitmapDrawable(getCtx().getResources(), bitmap);
            view.setBackground(drawable);
        }
    }

    /**
     * 将黑棋和白棋的数据写入文件
     *
     * @param whites 白棋坐标列表
     * @param blacks 黑棋坐标列表
     */
    public void savePoint2File(List<Point> whites, List<Point> blacks) {


        String whiteStr = ParseUtils.point2String(whites);
        String blackStr = ParseUtils.point2String(blacks);

        FileHelper.get().writeFile2SD(CfgCons.SAVE_WHITE_PATH, whiteStr);
        FileHelper.get().writeFile2SD(CfgCons.SAVE_BLACK_PATH, blackStr);
    }


    //////////////////////////音效处理
    public void loadSound(Context ctx) {
        int rawId = (int) SPFactory.getSP(DataType.INT).get(SPCons.SP_FALL_SOUND, SPCons.FALL_SOUND);
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(10);
        //创建SoundPool对象
        mSp = spb.build();
        mSoundId = mSp.load(ctx, rawId, 1);

    }

    public void playSound() {
        Boolean allowSound = (Boolean) SPFactory.getSP(DataType.BOOLEAN).get(SPCons.SP_ALLOW_SOUND, SPCons.ALLOW_SOUND);
        if (allowSound) {
            mSp.play(mSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void releaseSound() {
        mSp.release();
    }

    public void writePos2Service(List<Point> whites, List<Point> blacks) {

    }


}
