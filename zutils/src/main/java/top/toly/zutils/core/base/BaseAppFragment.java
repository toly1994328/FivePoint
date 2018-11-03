package top.toly.zutils.core.base;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/8/29 0029:13:46<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：Fragment封装类
 */
public abstract class BaseAppFragment extends Fragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载布局
        mRootView = inflater.inflate(setLayoutId(), container, false);
        render(mRootView);
        return mRootView;
    }


    /**
     * 设置布局里的控件
     */
    protected abstract void render(View rootView);

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int setLayoutId();

    /**
     * 找出对应的控件
     *
     * @param id  控件id
     * @param <T> 控件类型
     * @return 控件
     */
    protected <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }

    /**
     * 为textView设置文字
     *
     * @param viewId TextView控件id
     * @param str    控件id
     * @return BaseV4Fragment
     */
    protected BaseAppFragment setTextView(int viewId, String str) {
        TextView textView = findViewById(viewId);
        textView.setText(str);
        return this;
    }

    /**
     * 通过id设置ImageView图片
     *
     * @param viewId 条目内部控件的id
     * @param o      图片对象
     * @return BaseV4Fragment
     */
    public BaseAppFragment setImageView(int viewId, Object o) {
        ImageView view = findViewById(viewId);
        if (o instanceof Integer) {
            view.setImageResource((Integer) o);
        } else if (o instanceof Bitmap) {
            view.setImageBitmap((Bitmap) o);
        }
        return this;
    }

}
