package com.toly1994.fivepoint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.toly1994.fivepoint.app.Point;
import com.toly1994.fivepoint.app.SPCons;
import com.toly1994.fivepoint.loge.CheckWin;

import java.util.ArrayList;

import top.toly.zutils.core.io.sp.SpUtils;
import top.toly.zutils.core.shortUtils.Change;
import top.toly.zutils.core.shortUtils.L;
import top.toly.zutils.core.shortUtils.ToastUtil;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/20:12:38
 * 邮箱：1981462002@qq.com
 * 说明：五子棋绘图View
 */
public class WuZiView extends View {

    private int mPanelWith;
    private float mLineHeight;
    private Paint mPaint;
    private Paint mPaint2;

    private Bitmap mWhitePiece;//白棋
    private Bitmap mBlackPiece;//黑棋
    private float ratioPieceOfLineHeight = 3.5f * 1.0f / 4;//棋子占行高的比例


    private ArrayList<Point> mWhites = new ArrayList<>();//白棋集合
    private ArrayList<Point> mBlacks = new ArrayList<>();//黑棋集合

    private boolean isFirstDarw = true;

    private boolean isOver;//游戏是否结束
    private boolean isWhiteWin;//是否白棋赢
    //初始化配置


    private int mMax_line;
    private int mLineColor;
    private int mWhiteImg;
    private int mBlackImg;
    private boolean isWhite;

    private SpUtils<Boolean> mBooleanSpUtils;
    private SpUtils<Integer> mIntSpUtils;
    private Point mCurrentPos = new Point();

    public WuZiView(Context context) {
        this(context, null);
    }

    public WuZiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    /**
     * 初始化
     */
    private void init() {
        mBooleanSpUtils = new SpUtils<>(getContext());
        mIntSpUtils = new SpUtils<>(getContext());

        mMax_line = mIntSpUtils.get(SPCons.SP_MAX_LINE, SPCons.MAX_LINE);
        mLineColor = mIntSpUtils.get(SPCons.SP_LINE_COLOR, SPCons.LINE_COLOR);
        mWhiteImg = mIntSpUtils.get(SPCons.SP_WHITE_IMG, SPCons.WHITE_IMG);
        mBlackImg = mIntSpUtils.get(SPCons.SP_BLACK_IMG, SPCons.BLACK_IMG);


        mPaint = new Paint();
        mPaint.setColor(mLineColor);//设置颜色
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(Change.dp2px(getContext(), 1));
        //设置图片
        mWhitePiece = BitmapFactory.decodeResource(getResources(), mWhiteImg);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), mBlackImg);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);//设置颜色
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(Change.dp2px(getContext(), 1));
    }

    /**
     * 1.测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        //嵌套在ScrollView下可能出现Mode为MeasureSpec.UNSPECIFIED
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    /**
     * 确定尺寸
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWith = w;
        mLineHeight = mPanelWith * 1.0f / mMax_line;
        //适配棋子大小
        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);

    }

    /**
     * 绘画
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawBoard2(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkIsOver();
    }

    /**
     * 检查游戏是否结束
     */
    private void checkIsOver() {
        int maxCountInLine = mIntSpUtils.get(SPCons.SP_MAX_COUNT_IN_LINE, SPCons.MAX_COUNT_IN_LINE);

        boolean whiteWin = CheckWin.checkFiveInLineWinner(mWhites, maxCountInLine);


        boolean blackWin = CheckWin.checkFiveInLineWinner(mBlacks, maxCountInLine);
        if (whiteWin || blackWin) {
            isOver = true;
            isWhiteWin = whiteWin;
            if (mOnGameOverListener != null) {
                mOnGameOverListener.gameOver(whiteWin);
            }
        }
    }


    /**
     * 绘制棋子
     *
     * @param canvas
     */
    private void drawPieces(Canvas canvas) {


        //绘制棋
        for (int i = 0, n = mWhites.size(); i < n; i++) {
            Point point = mWhites.get(i);
            float left = point.x * mLineHeight + (mLineHeight - ratioPieceOfLineHeight * mLineHeight) / 2;
            float top = point.y * mLineHeight + (mLineHeight - ratioPieceOfLineHeight * mLineHeight) / 2;
            canvas.drawBitmap(mWhitePiece, left, top, null);
        }
        //绘制棋
        for (int i = 0, n = mBlacks.size(); i < n; i++) {
            Point point = mBlacks.get(i);
            float left = point.x * mLineHeight + (mLineHeight - ratioPieceOfLineHeight * mLineHeight) / 2;
            float top = point.y * mLineHeight + (mLineHeight - ratioPieceOfLineHeight * mLineHeight) / 2;
            canvas.drawBitmap(mBlackPiece, left, top, null);
        }

        if (mOnDrawListener != null) {
            mOnDrawListener.drawing(isWhite);
        }


    }

    /**
     * 绘制棋盘
     *
     * @param canvas
     */
    private void drawBoard(Canvas canvas) {
        int with = mPanelWith;
        float lineHeight = mPanelWith * 1.0f / mMax_line;
        for (int i = 0; i < mMax_line; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (with - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    /**
     * 绘制棋盘
     *
     * @param canvas
     */
    private void drawBoard2(Canvas canvas) {
        int with = mPanelWith;
        float lineHeight = mPanelWith * 1.0f / mMax_line;
        for (int i = 0; i < mMax_line; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (with - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight - 1f);

            canvas.drawLine(startX, y, endX, y, mPaint2);
            canvas.drawLine(y, startX, y, endX, mPaint2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isOver) {
            return false;//游戏结束，苹果我不想吃了
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point point = new Point((int) (x / mLineHeight), (int) (y / mLineHeight));

            if (mWhites.contains(point) || mBlacks.contains(point)) {
                return false;
            }
            mCurrentPos = point;
            mCurrentPos.isWhite = isWhite;

            //添加到黑白集合
            if (isWhite) {
                mWhites.add(point);
            } else {
                mBlacks.add(point);
            }
            L.d(mCurrentPos + L.l());
            invalidate();//重绘
            isWhite = !isWhite;
            return true;//按下时返回true，表示孩子对苹果感兴趣，要吃苹果
        }


        return true;//按下时返回true，表示孩子对苹果感兴趣，要吃苹果
    }


    /**
     * 再来一局
     */
    public void start() {
        mWhites.clear();
        mBlacks.clear();
        isOver = false;
        isWhite = true;
        invalidate();
    }

    /**
     * 悔棋逻辑
     */
    public void backStep() {

        if ((mWhites.size() > 0) && (mBlacks.size() > 0)) {
            if (!isWhite) {
                mWhites.remove(mWhites.size() - 1);
                isWhite = !isWhite;
                invalidate();
            } else {
                mBlacks.remove(mBlacks.size() - 1);
                isWhite = !isWhite;
                invalidate();
            }
        } else {
            ToastUtil.show(getContext(), "棋子不够耶，再下几个呗！");
        }
    }

    public void setPoints(ArrayList<Point> whites, ArrayList<Point> blacks) {
        mWhites = whites;
        mBlacks = blacks;
        invalidate();
    }

    public ArrayList<Point> getWhites() {
        return mWhites;
    }

    public ArrayList<Point> getBlacks() {
        return mBlacks;
    }

    public Point getCurrentPos() {
        return mCurrentPos;
    }


    public void drawPoint(ArrayList<Point> points) {
        mWhites.clear();
        mBlacks.clear();
        for (Point point : points) {
            if (point.isWhite) {
                mWhites.add(point);

            } else {
                mBlacks.add(point);
            }
        }
    }

    ///////////////////////胜利时接口回调////////////////////////////

    public interface OnGameOverListener {
        /**
         * 回调方法
         *
         * @param isWhiteWin 是否白棋胜利
         */
        void gameOver(boolean isWhiteWin);
    }

    private OnGameOverListener mOnGameOverListener;

    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        mOnGameOverListener = onGameOverListener;
    }


    //////////////////////绘制时回调接口////////////////////
    public interface OnDrawListener {
        void drawing(boolean isWhite);

    }

    private OnDrawListener mOnDrawListener;

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }


    ////////////////储存临时数据，避免丢失!!!必须添加id///////////////////

    private static final String INSTANCE = "instanse";
    private static final String INSTANCE_WHITES = "instance_whites";
    private static final String INSTANCE_BLACKS = "instance_blacks";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putParcelableArrayList(INSTANCE_WHITES, mWhites);
        bundle.putParcelableArrayList(INSTANCE_BLACKS, mBlacks);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mWhites = bundle.getParcelableArrayList(INSTANCE_WHITES);
            mBlacks = bundle.getParcelableArrayList(INSTANCE_BLACKS);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }

        super.onRestoreInstanceState(state);
    }

///////////////////////////////////////


}
