package com.oranges.cnmall.fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oranges.cnmall.activity.LoginActivity;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.User;

/**
 * Fragment基类 BaseFragment
 * Created by oranges on 2016/9/25.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initX(this, inflater, container);
        init();
        return view;
    }

    // 创建视图
    public abstract View initX(Fragment fragment, LayoutInflater inflater, ViewGroup container);

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
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else{
            super.startActivity(intent);
        }
    }
}
