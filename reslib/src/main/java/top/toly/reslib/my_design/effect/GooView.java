package top.toly.reslib.my_design.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import top.toly.reslib.my_design.utils.GeometryUtil;
import top.toly.zutils.core.shortUtils.Change;
import top.toly.zutils.core.ui.common.ScrUtil;


public class GooView extends View {
    private static final String TAG = "GooView";
    private Paint paint;

    public GooView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GooView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GooView(Context context) {
        super(context);
        init();
    }

    private void init() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 设置抗锯齿
        paint.setColor(Color.RED);
    }

    /**
     *
     */
    private float dragRadius = 40;// 拖拽圆的半径
    /**
     *  固定圆的半径
     */
    private float stickyRadius;

    private PointF dragCenter = new PointF(ScrUtil.getScreenWidth(getContext()) / 2,
            ScrUtil.getScreenHeight(getContext()) / 2);// 拖拽圆的圆心
    private PointF stickyCenter = new PointF(ScrUtil.getScreenWidth(getContext()) / 2,
            ScrUtil.getScreenHeight(getContext()) / 2);// 固定圆的圆心

    private PointF[] stickyPoint =
            {new PointF(150f, 108f), new PointF(150f, 132f)};

    private PointF[] dragPoint =
            {new PointF(100f, 108f), new PointF(100f, 132f)};


    private PointF controlPoint = new PointF(125f, 120f);
    private double lineK;// 斜率

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 让整体画布往上偏移
        canvas.translate(0, -ScrUtil.getStatusHeight(getContext()));

        stickyRadius = getStickyRadius();

        // dragPoint: 2圆圆心连线的垂线与drag圆的交点
        // stickyPoint: 2圆圆心连线的垂线与sticky圆的交点
        // 根据dragCenter动态求出dragPoint和stickyPoint
        float xOffset = dragCenter.x - stickyCenter.x;
        float yOffset = dragCenter.y - stickyCenter.y;
        if (xOffset != 0) {
            lineK = yOffset / xOffset;
        }
        dragPoint = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, lineK);

        stickyPoint = GeometryUtil.getIntersectionPoints(stickyCenter, stickyRadius, lineK);

        // 动态计算控制点
        controlPoint = GeometryUtil.getPointByPercent(dragCenter, stickyCenter, 0.618f);

        // 1.绘制2个圆
        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, paint);// 绘制拖拽圆

        if (!isDragOutOfRange) {
            canvas.drawCircle(stickyCenter.x, stickyCenter.y, stickyRadius, paint);// 绘制固定圆
            // 2.使用贝塞尔曲线绘制连接部分
            Path path = new Path();
            dragRadius = 40;
            path.moveTo(stickyPoint[0].x, stickyPoint[0].y);// 设置起点
            path.quadTo(controlPoint.x, controlPoint.y, dragPoint[0].x, dragPoint[0].y);// 使用贝塞尔曲线连接
            path.lineTo(dragPoint[1].x, dragPoint[1].y);
            path.quadTo(controlPoint.x, controlPoint.y, stickyPoint[1].x, stickyPoint[1].y);
            // path.close();//默认会闭合，所以不用掉
            canvas.drawPath(path, paint);
        }

        // 绘制圈圈，以固定圆圆心为圆心，然后80为半径
        paint.setStyle(Style.STROKE);// 设置只有边线
        canvas.drawCircle(stickyCenter.x, stickyCenter.y, maxDistance, paint);
        paint.setStyle(Style.FILL);
    }

    private float maxDistance = Change.dp2px(getContext(), 179);

    /**
     * 动态求出固定圆的半径
     */
    private float getStickyRadius() {
        float radius;
        // 求出2个圆心之间的距离
        float centerDistance = GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter);
        float fraction = centerDistance / maxDistance;// 圆心距离占总距离的百分比
        radius = GeometryUtil.evaluateValue(fraction, 12f, 4f);
        return radius;
    }

    private boolean isDragOutOfRange = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragOutOfRange = false;



                dragCenter.set(event.getX(), event.getY());

                Log.e(TAG, "onTouchEvent: ");

                break;
            case MotionEvent.ACTION_MOVE:
                dragCenter.set(event.getX(), event.getY());

                if (GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) > maxDistance) {
                    // 超出范围，应该断掉，不再绘制贝塞尔曲线的部分
                    isDragOutOfRange = true;
                }
                break;
            case MotionEvent.ACTION_UP:

                if (GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) > maxDistance) {
                    // 超出范围，应该断掉，不再绘制贝塞尔曲线的部分
                    isDragOutOfRange = true;

                    dragRadius = 0;


                }
                if (GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) > maxDistance) {
                    dragCenter.set(stickyCenter.x, stickyCenter.y);
                } else {
                    if (isDragOutOfRange) {
                        // 如果曾经超出范围过
                        dragCenter.set(stickyCenter.x, stickyCenter.y);
                    } else {
                        // 动画的形式回去
                        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(1);
                        final PointF startPointF = new PointF(dragCenter.x, dragCenter.y);
                        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                // d动画执行的百分比
                                float animatedFraction = animator.getAnimatedFraction();
                                PointF pointF = GeometryUtil.getPointByPercent(startPointF, stickyCenter, animatedFraction);
                                dragCenter.set(pointF);
                                invalidate();
                            }
                        });
                        valueAnimator.setDuration(500);
                        valueAnimator.setInterpolator(new OvershootInterpolator(3));
                        valueAnimator.start();
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

}
