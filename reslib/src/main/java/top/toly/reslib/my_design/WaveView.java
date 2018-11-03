package top.toly.reslib.my_design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import top.toly.zutils.core.ui.common.ColUtils;

/**
 * 水波纹效果
 *
 * @author leo
 */
public class WaveView extends View {

    private static final int DIS_SOLP = 13;

    protected boolean isRunning = false;

    private ArrayList<Wave> wList;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wList = new ArrayList<Wave>();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //刷新数据
            flushData();
            //刷新页面
            invalidate();
            //循环动画
            if (isRunning) {
                handler.sendEmptyMessageDelayed(0, 50);
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < wList.size(); i++) {
            Wave wave = wList.get(i);
            canvas.drawCircle(wave.cx, wave.cy, wave.r, wave.p);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                addPoint((int) event.getX(), (int) event.getY());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 添加新的波浪中心点
     *
     * @param x
     * @param y
     */
    private void addPoint(int x, int y) {
        if (wList.size() == 0) {
            addPoint2List(x, y);
            /*
             * 第一次启动动画
             */
            isRunning = true;
            handler.sendEmptyMessage(0);
        } else {
            Wave w = wList.get(wList.size() - 1);
            if (Math.abs(w.cx - x) > DIS_SOLP || Math.abs(w.cy - y) > DIS_SOLP) {
                addPoint2List(x, y);
            }
        }

    }

    /**
     * 添加新的波浪
     *
     * @param x
     * @param y
     */
    private void addPoint2List(int x, int y) {
        Wave w = new Wave();
        w.cx = x;
        w.cy = y;
        Paint pa = new Paint();
        pa.setColor(ColUtils.randomRGB());
        pa.setAntiAlias(true);
        pa.setStyle(Style.STROKE);
        w.p = pa;
        wList.add(w);
    }

    private int[] colors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};

    /**
     * 刷新数据
     */
    private void flushData() {

        for (int i = 0; i < wList.size(); i++) {

            Wave w = wList.get(i);

            //如果透明度为 0 从集合中删除
            int alpha = w.p.getAlpha();
            if (alpha == 0) {
                wList.remove(i);    //删除i 以后，i的值应该再减1 否则会漏掉一个对象，不过，在此处影响不大，效果上看不出来。
                continue;
            }

            alpha -= 5;
            if (alpha < 5) {
                alpha = 0;
            }
            //降低透明度
            w.p.setAlpha(alpha);

            //扩大半径
            w.r = w.r + 3;
            //设置半径厚度
            w.p.setStrokeWidth(w.r / 3);
        }

        /*
         * 如果集合被清空，就停止刷新动画
         */
        if (wList.size() == 0) {
            isRunning = false;
        }
    }

    /**
     * 定义一个波浪
     *
     * @author leo
     */
    private class Wave {
        //圆心
        int cx;
        int cy;

        //画笔
        Paint p;
        //半径
        int r;
    }
}
