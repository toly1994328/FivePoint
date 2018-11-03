package top.toly.reslib.my_design.effect.stellarmap;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import top.toly.zutils.core.ui.common.ColUtils;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/10:13:42
 * 邮箱：1981462002@qq.com
 * 说明：
 */
public class RecommendAdapter implements StellarMap.Adapter {

    private ArrayList<String> mData;
    private Context mCtx;


    public RecommendAdapter(ArrayList<String> data, Context ctx) {
        mData = data;
        mCtx = ctx;
    }

    // 返回组的个数
    @Override
    public int getGroupCount() {
        return 2;
    }

    // 返回某组的item个数
    @Override
    public int getCount(int group) {
        int count = 15;
        // mTestData.size() / getGroupCount();
        if (group == getGroupCount() - 1) {
            // 最后一页, 将除不尽,余下来的数量追加在最后一页, 保证数据完整不丢失
            count += mData.size() % getGroupCount();
        }

        return count;
    }

    // 初始化布局
    @Override
    public View getView(int group, int position, View convertView) {
        // 因为position每组都会从0开始计数, 所以需要将前面几组数据的个数加起来,才能确定当前组获取数据的角标位置
        position += (group) * getCount(group - 1);

        final String keyword = mData.get(position);

        TextView view = new TextView(mCtx);
        view.setText(keyword);

        Random random = new Random();
        // 随机大小, 16-25
        int size = 16 + random.nextInt(8);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        view.setTextColor(ColUtils.randomColor());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, keyword, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // 返回下一组的id
    @Override
    public int getNextGroupOnZoom(int group, boolean isZoomIn) {
        if (isZoomIn) {
            // 往下滑加载上一页
            if (group > 0) {
                group--;
            } else {
                // 跳到最后一页
                group = getGroupCount() - 1;
            }
        } else {
            // 往上滑加载下一页
            if (group < getGroupCount() - 1) {
                group++;
            } else {
                // 跳到第一页
                group = 0;
            }
        }
        return group;
    }

}
