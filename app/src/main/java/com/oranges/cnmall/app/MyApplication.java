package com.oranges.cnmall.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.utils.UserLocalDataUtil;

import org.xutils.x;

/**
 * Created by orages on 2016/9/22.
 */
public class MyApplication extends Application {

    private User user;
    private Intent intent;

    private static MyApplication application;

    // 获得MyApplication
    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 初始化UserLocalDataUtil工具类
        initUser();
        // 配置渐进式显示图片
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();
        // 初始化Fresco框架
        Fresco.initialize(this,config);
        // 初始化xUtils3框架
        x.Ext.init(this);
    }

    // 初始化UserLocalDataUtil
    private void initUser() {
        this.user = UserLocalDataUtil.getUser(this);
    }

    // 获取当前登录用户信息
    public User getUser() {
        return user;
    }

    // 保存当前登录用户信息
    public void putUser(User user, String token) {
        this.user = user;
        UserLocalDataUtil.putUser(this, user);
        UserLocalDataUtil.putToken(this, token);
    }

    public String getToken() {
        return UserLocalDataUtil.getToken(this);
    }

    // 清除登录用户信息
    public void clearUser() {
        this.user = null;
        UserLocalDataUtil.clearUser(this);
        UserLocalDataUtil.clearToken(this);
    }

    // 保存意图
    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    // 获得意图
    public Intent getIntent() {
        return this.intent;
    }

    // 清除意图
    public void clearIntent() {
        this.intent = null;
    }

    // 跳转Activity
    public void doTargetActivity(Context context){
        context.startActivity(application.getIntent());
        clearIntent();
    }
}
