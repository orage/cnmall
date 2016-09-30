package com.oranges.cnmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.adapter.FavoriteAdapter;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.decortion.CardViewItemDecoration;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Favorites;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_my_favorite)
public class MyFavoriteActivity extends BaseActivity {

    @ViewInject(R.id.tb_mf_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.rv_content)
    private RecyclerView rvContent;

    private FavoriteAdapter mAdapter;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public void init() {
        initToolBar();
        getFavorites();
    }

    // 初始化ToolBar
    private void initToolBar() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // 获取我的收藏
    private void getFavorites() {
        Long userId = MyApplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        httpHelper.get(Const.API.FAVORITE_LIST, params, new SpotsCallback<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showFavorites(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    // 显示我的收藏列表
    private void showFavorites(List<Favorites> favorites) {
        mAdapter = new FavoriteAdapter(this, favorites);
        rvContent.setAdapter(mAdapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.addItemDecoration(new CardViewItemDecoration());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }
        });
    }


}
