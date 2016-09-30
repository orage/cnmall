package com.oranges.cnmall.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.oranges.cnmall.activity.LoginActivity;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.User;

import org.xutils.x;

/**
 * Created by oranges on 2016/9/25.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    // 初始化
    public abstract void init();

    // 登录拦截
    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) { // 是否需要登录
            User user = MyApplication.getInstance().getUser();
            if(null!=user){
                super.startActivity(intent);
            }else{
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else{
            super.startActivity(intent);
        }
    }
}


