package top.toly.zutils.core.io.sp;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/2/20:17:57<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：SharedPreferences工具类 :默认apply方式提交<br/>
 */
public class SpUtils<T> implements Shareable<T> {
    /**
     * 上下文
     */
    private Context mContext;
    private String mName;

    /**
     * 是否以apply方式提交：否则是commit方式提交
     */
    private boolean isApply = true;

    /**
     * SharedPreferences对象
     */
    private static SharedPreferences sp;

    /**
     * 构造函数 传入上下文
     *
     * @param context 上下文
     */
    public SpUtils(Context context, String name) {
        mContext = context;
        mName = name;
    }

    /**
     * 构造函数 传入上下文
     *
     * @param context 上下文
     */
    public SpUtils(Context context) {
        mContext = context;
        mName = "config";
    }


    /**
     * 写入至sp中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值 boolean
     * @return 是否插入成功 apply 方式返回null
     */
    @Override
    public Boolean put(String key, T value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = null;
        if (value instanceof Boolean) {
            editor = sp.edit().putBoolean(key, (Boolean) value);
        }

        if (value instanceof String) {
            editor = sp.edit().putString(key, (String) value);
        }

        if (value instanceof Integer) {
            editor = sp.edit().putInt(key, (Integer) value);
        }

        if (value instanceof Long) {
            editor = sp.edit().putLong(key, (Long) value);
        }
        if (value instanceof Float) {
            editor = sp.edit().putFloat(key, (Float) value);
        }

        if (editor != null) {
            if (isApply) {
                editor.apply();
                return null;
            } else {
                return editor.commit();
            }
        }
        return false;
    }


    /**
     * 从sp中读取
     *
     * @param key      存储节点名称
     * @param defValue 默认值
     * @return 读取boolean标示从sp中
     */
    @Override
    public T get(String key, T defValue) {
        //(存储节点文件名称,读写方式)
        Object o = new Object();
        if (sp == null) {
            sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        }

        if (defValue instanceof Boolean) {
            o = (sp.getBoolean(key, (Boolean) defValue));
        }

        if (defValue instanceof String) {
            o = sp.getString(key, (String) defValue);
        }

        if (defValue instanceof Integer) {
            o = sp.getInt(key, (Integer) defValue);
        }

        if (defValue instanceof Long) {
            o = sp.getLong(key, (Long) defValue);
        }
        if (defValue instanceof Float) {
            o = sp.getFloat(key, (Float) defValue);
        }
        return (T) o;
    }

    /**
     * 从sp中移除指定节点
     *
     * @param key 需要移除节点的名称
     * @return 是否插入成功 apply 方式返回null
     */
    @Override
    public Boolean remove(String key) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().remove(key);
        if (isApply) {
            editor.apply();
            return null;
        } else {
            return editor.commit();
        }
    }


    /**
     * 从sp中移除指定节点
     *
     * @return 是否插入成功 apply 方式返回null
     */
    public Boolean clear() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().clear();
        if (isApply) {
            editor.apply();
            return null;
        } else {
            return editor.commit();
        }
    }

    /**
     * 设置是否以 apply方式提交
     *
     * @param apply 以apply方式提交，否则：commit
     */
    public void setApply(boolean apply) {
        isApply = apply;
    }
}
