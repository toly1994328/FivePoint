package com.toly1994.fivepoint.view;

/**
 * 作者：张风捷特烈
 * 时间：2018/4/25:9:37
 * 邮箱：1981462002@qq.com
 * 说明：显示和消失对话框接口--由于非常常见所以单独抽出
 */

public interface ILoadingView {
    /**
     * 显示进度条
     */
    void showLoading();

    /**
     * 隐藏进度条
     */
    void hideLoading();

}
