package top.toly.reslib.my_design.logic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.toly1994.logic_canvas.base.BaseView;
import com.toly1994.logic_canvas.base.Pos;
import com.toly1994.logic_canvas.core.Painter;
import com.toly1994.logic_canvas.core.PainterEnum;
import com.toly1994.logic_canvas.core.shape.ShapeStar;
import com.toly1994.logic_canvas.go.interpolator.D_Sin_Inter;

import top.toly.reslib.my_design.evaluator.PosLineEvaluator;
import top.toly.reslib.my_design.interpolator.D_Log_Inter;
import top.toly.zutils.core.shortUtils.L;
import top.toly.zutils.core.shortUtils.ToastUtil;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/9/16 0016:19:18<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class AnimationView extends BaseView {

    /**
     * 下移动画
     */
    private ObjectAnimator mMoveDown;

    private float mCircleR = 20;
    private float mStarR;

    private int mColor = Color.RED;


    /**
     * 动画集
     */
    private ObjectAnimator mMoveRight;
    private Painter mPainter;
    private float value;
    private Pos currentPoint = v2(mCircleR, 0);


    /**
     * 取路径的长度
     */
    private PathMeasure pathMeasure;
    /**
     * 路径画笔
     */
    private Paint mPaint;
    private Path mPath;

    /**
     * 路径--用来获取对勾的路径
     */
//    private Path path = new Path();
    public AnimationView(Context context) {
        this(context, null);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean init(AttributeSet attributeSet) {
        mStarR = 200;
//        moveRight();
        //初始画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        //实例路径
        mPath =new ShapeStar(ShapeStar.MODE_REGULAR).num(8).R(mStarR).formPath();
        //测量路径
        pathMeasure = new PathMeasure(mPath, false);

        setOnEventListener(new OnEventListener() {

            @Override
            public void down(Pos pos) {
//                插值器();

                ValueAnimator pathAnimator = ValueAnimator.ofFloat(1, 0);
                pathAnimator.setDuration(5000);
                pathAnimator.setInterpolator(new D_Sin_Inter());
                pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        DashPathEffect effect = new DashPathEffect(
                                new float[]{
                                        pathMeasure.getLength(),
                                        pathMeasure.getLength()},
                                value * pathMeasure.getLength());
                        mPaint.setPathEffect(effect);
                        invalidate();
                    }
                });

                pathAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        ObjectAnimator translationX = translationX();
                        translationX.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ToastUtil.showAtOnce(getContext(),"OVER");
                                rotation().start();
                            }
                        });


                        translationX.start();
                    }
                });





                pathAnimator.start();


//                mMoveDown.start();//开启动画

//                translationY().start();

//                ObjectAnimator translationX = translationX();
//                translationX.setStartDelay(1000);
//                translationX.setRepeatCount(ValueAnimator.INFINITE);
//                translationX.setRepeatMode(ValueAnimator.REVERSE);
//                translationX.start();

//                setPivotX(200);
//                setPivotY(200);
//                rotation().start();


//                rotationX().start();
//                scaleX().start();
//                alpha().start();
//                circleR().start();
//                color().start();

//                AnimatorSet set = new AnimatorSet();
//                set
//                        .play(translationX())
//                        .with(alpha())
//                        .after(1000)
//                        .before(scaleX())
//                        .before(scaleY());


//                scaleY().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//
//                    }
//                });
//
//                scaleY().addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                    }
//
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                    }
//                });


//                set.addListener(new AnimatorListenerAdapter() {
//
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        setColor(Color.BLUE);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//
//                        setColor(Color.GREEN);
//
//                    }
//
//
//                });

//                set.start();


//                mTranslationX = translationX();
//                mTranslationX.setRepeatMode(ValueAnimator.REVERSE);
//                mTranslationX.setRepeatCount(ValueAnimator.INFINITE);

//                mTranslationX.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        //开始时设为绿色
//                        setColor(Color.GREEN);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        //结束时设为白色
//                        setColor(Color.BLUE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        //取消是大小变为50
//                        setCircleR(50);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                        //重复时设为随机色
//                        setColor(ColUtils.randomColor());
//                    }
//                });
//                mTranslationX.addPauseListener(new Animator.AnimatorPauseListener() {
//                    @Override
//                    public void onAnimationPause(Animator animation) {
//                        setColor(Color.YELLOW);//暂停黄色
//                    }
//
//                    @Override
//                    public void onAnimationResume(Animator animation) {
//                        setColor(Color.BLUE);//恢复红色
//                    }
//                });
//
//                mTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        mCircleR = (Float) animation.getAnimatedValue()/2;
//                        invalidate();
//                    }
//                });
//                mTranslationX.start();


            }


            @Override
            public void up(Pos pos, MoveSpeed moveSpeed, Orientation orientation) {

            }

            @Override
            public void move(Pos pos, double v, float v1, float v2, double v3, Orientation orientation) {
                L.d(orientation + L.l());
                switch (orientation) {
                    case RIGHT:

                        break;
                    case LEFT:
//                        mTranslationX.pause();//动画暂停
                        break;
                    case BOTTOM:
//                        mTranslationX.resume();//动画恢复
                        break;

                }

            }
        });

        return true;
    }

    private void 插值器() {
        Pos startP = currentPoint;//初始值（起点）
        Pos endP = new Pos(1000, mCircleR);//结束值（终点）

        ValueAnimator animator = ValueAnimator.ofObject(new PosLineEvaluator(), startP, endP);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(5000);

        animator.setInterpolator(new D_Log_Inter());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Pos) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPainter = PainterEnum.INSTANCE.getInstance(canvas);


//        Path path = ss.num(5).coo(0, 600).formPath();

//
//        mPainter.draw(ss.deepClone().num()
//                .fs(mColor).coo(0, 600));
        //绘制路径
        canvas.drawPath(mPath, mPaint);

    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator translationY() {

        float translationY = getTranslationY();//当前Y偏移量
        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "translationY", translationY, translationY + 100)
                .setDuration(1000);//设置时常
    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator translationX() {

        float translationY = getTranslationY();//当前Y偏移量
        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "translationX", translationY, translationY + 200)
                .setDuration(1000);//设置时常
    }


    /**
     * 设置view下移的动画
     */
    private ObjectAnimator alpha() {

        float translationY = getTranslationY();//当前Y偏移量

//        setPivotX(0);
//        setPivotY(0);

        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "alpha", 1, 0.2f)
                .setDuration(1000);//设置时常
    }


    /**
     * 设置view下移的动画
     */
    private ObjectAnimator rotation() {

        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "rotation", 0, 360, 360, 0, 0, 90)
                .setDuration(5000);//设置时常
    }

    /**
     * f
     * 设置view下移的动画
     */
    private ObjectAnimator rotationX() {


        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "rotationX", 0, 180)
                .setDuration(1000);//设置时常
    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator scaleY() {


        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "scaleY", 1, 0.5f)
                .setDuration(1000);//设置时常
    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator scaleX() {


        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "scaleX", 1, 0.5f)
                .setDuration(1000);//设置时常
    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator circleR() {
        return ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofFloat(this, "circleR", 100, 50, 100, 20, 100)
                .setDuration(3000);//设置时常
    }

    /**
     * 设置view下移的动画
     */
    private ObjectAnimator color() {
        ObjectAnimator color = ObjectAnimator//创建实例
                //(View,属性名,初始化值,结束值)
                .ofInt(this, "color", 0xff0000ff, 0xffF2BA38, 0xffDD70BC)
                .setDuration(3000);
        color.setEvaluator(new ArgbEvaluator());//颜色的估值器
        return color;//设置时常
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (mStarR * 2), (int) (mStarR * 2));
    }

    /**
     * 设置view上移的动画
     */
    private void moveRight() {
        final float curTranslationY = this.getTranslationY();

        mMoveRight = ObjectAnimator
                .ofFloat(this, "translationX",
                        curTranslationY, curTranslationY + 300);

        mMoveRight.setDuration(1000);

        mMoveRight.setInterpolator(new AccelerateDecelerateInterpolator());

//        mMoveDown.setAutoCancel(true);
    }

    public void setCircleR(float circleR) {
        mCircleR = circleR;
        invalidate();//记得重绘
    }

    public float getCircleR() {
        return mCircleR;
    }

    public int getColor() {
        return mColor;
    }


    public void setColor(int color) {
        mColor = color;
        invalidate();
    }
}
