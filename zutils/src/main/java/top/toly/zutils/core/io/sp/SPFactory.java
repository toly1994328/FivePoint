package top.toly.zutils.core.io.sp;

import android.content.Context;

import java.util.HashMap;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/28 0028:23:34<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：SpUtils的生产工厂
 */
public class SPFactory{

    //这里的静态上下文最好用全局的
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    /**
     * 一参，必须先初始化，推荐使用Application里通过全局上下文
     *
     * @param type 该类型的任意对象
     * @return
     */
    public static Shareable getSP(DataType type) {
        return getSP(sContext, type, "config");
    }

    /**
     * 默认String类型
     *
     * @return
     */
    public static Shareable getSP() {
        return getSP(sContext, DataType.STRING, "config");
    }


    //为提高性能，不重复new ，采用集合装载，以便复用
    private static HashMap<DataType, Shareable> mSpMap = new HashMap<>();

    public static Shareable getSP(Context context, DataType type, String fileName) {
        Shareable sharedable = mSpMap.get(type);//在映射中，复用
        if (sharedable == null) {
            switch (type) {
                case INT:
                    sharedable = new SpUtils<Integer>(context, fileName);
                    break;
                case LONG:
                    sharedable = new SpUtils<Long>(context, fileName);
                    break;
                case FLOAT:
                    sharedable = new SpUtils<Float>(context, fileName);
                    break;
                case STRING:
                    sharedable = new SpUtils<String>(context, fileName);
                    break;
                case BOOLEAN:
                    sharedable = new SpUtils<Boolean>(context, fileName);
                    break;
            }
            mSpMap.put(type, sharedable);//将新建的fragment加入集合中
        }
        return sharedable;
    }

    public static Shareable getSP(Context context, DataType type) {
        return getSP(context, type, "config");
    }
}
