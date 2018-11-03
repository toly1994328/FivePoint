package com.toly1994;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.toly1994.fivepoint.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.toly.zutils.core.io.sp.DataType;
import top.toly.zutils.core.io.sp.SPFactory;
import top.toly.zutils.core.shortUtils.ToastUtil;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/28 0028:23:54<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class TestSPActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(   this);
        //初始化：可在自定义Application里初始化
        SPFactory.init(getApplicationContext());
        //设置值
        SPFactory.getSP(DataType.INT).put("SP_DEFAULT_LIFE", 1000);
        SPFactory.getSP().put("SP_DEFAULT_NAME", "Toly");
        SPFactory.getSP(DataType.BOOLEAN).put("SP_MAN", false);
        SPFactory.getSP(DataType.FLOAT).put("PI", 3.1415f);
        SPFactory.getSP(DataType.LONG).put("SP_CURRENT_TIME", System.currentTimeMillis());


    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        //获取值
        String name = (String) SPFactory.getSP().get("SP_DEFAULT_NAME", "");
        //删除键值对
        SPFactory.getSP(DataType.BOOLEAN).remove("SP_MAN");
        //清楚配置
        SPFactory.getSP().clear();
        ToastUtil.show(this, name);

    }
}
