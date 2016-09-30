package com.oranges.cnmall.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.LoginActivity;
import com.oranges.cnmall.app.MyApplication;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

/**
 * 对话框SpotsDialog的回调封装
 * Created by oranges on 2016/9/22.
 */
public abstract class SpotsCallback<T> extends BaseCallback<T> {

    Context context;
    SpotsDialog dialog;

    public SpotsCallback(Context context) {
        this.context = context;
        dialog = new SpotsDialog(context);
    }

    // 显示对话框
    public void showDialog(){
        dialog.show();
    }

    // 隐藏对话框
    public void dismissDialog(){
        if (null!=dialog)
            dialog.dismiss();
    }

    // 设置对话框内容
    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        // 请求前显示对话框(loading)
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        Toast.makeText(context, R.string.token_error,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        MyApplication.getInstance().clearUser();
    }
}
