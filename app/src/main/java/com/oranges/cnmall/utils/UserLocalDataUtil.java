package com.oranges.cnmall.utils;

import android.content.Context;
import android.text.TextUtils;

import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.consts.Const;


/**
 * 本地用户数据操作 UserLocalDataUtil
 * Created by oranges on 2016/9/25.
 */
public class UserLocalDataUtil {

    // 保存当前登录用户
    public static void putUser(Context context, User user) {
        String user_json = JSONUtil.toJSON(user);
        PreferencesUtil.putString(context, Const.USER_JSON, user_json);
    }

    // 保存当前用户身份令牌
    public static void putToken(Context context, String token) {
        PreferencesUtil.putString(context, Const.TOKEN, token);
    }

    // 获取当前登录用户
    public static User getUser(Context context) {
        String user_json = PreferencesUtil.getString(context, Const.USER_JSON);
        if (!TextUtils.isEmpty(user_json))
            return JSONUtil.fromJson(user_json, User.class);
        return null;
    }

    // 获取当前用户身份令牌
    public static String getToken(Context context) {
        return PreferencesUtil.getString(context, Const.TOKEN);
    }

    // 清除当前登录用户
    public static void clearUser(Context context) {
        PreferencesUtil.putString(context, Const.USER_JSON, "");
    }

    // 清除当前用户身份令牌
    public static void clearToken(Context context) {
        PreferencesUtil.putString(context, Const.TOKEN, "");
    }
}
