package top.toly.zutils.core.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/1 0001:11:42<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：PopupWindow的封装
 */
public abstract class BasePopWindow extends PopupWindow {
    private View mRootView;
    private Context mContext;

    private SparseArray<View> mViews;

    public BasePopWindow(Context context) {
        this(context, -1);
    }


    public BasePopWindow(Context context, int animStyleId) {

        mContext = context;
        //获取屏幕尺寸
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mRootView = LayoutInflater.from(context).inflate(layoutId(), null);
        mViews = new SparseArray<>();
        //基本设置
        setContentView(mRootView);
        setWidth(outMetrics.widthPixels);
        setHeight(outMetrics.heightPixels);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        //沉浸标题栏
        setClippingEnabled(false);
//        fitPopupWindowOverStatusBar(this, true);
        setBackgroundDrawable(new BitmapDrawable());
        if (animStyleId != -1) {
            setAnimationStyle(animStyleId);//设置mPopWindow进出动画
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId pop里的控件id
     * @param <T>    控件视图
     * @return 控件视图
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public static void fitPopupWindowOverStatusBar(PopupWindow mPopupWindow, boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(needFullScreen);
                mLayoutInScreen.set(mPopupWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract int layoutId();

}
