package com.toly1994.fivepoint.view;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/27 0027:10:34<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class Loadinger implements ILoadingView {
    private ProgressDialog mProgressDialog;
    private Context ctx;

    public Loadinger(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(ctx, "",
                    "Loading...", true, false);
        } else if (mProgressDialog.isShowing()) {
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
