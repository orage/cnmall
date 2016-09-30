package com.oranges.cnmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.WareDetailActivity;
import com.oranges.cnmall.adapter.CategoryAdapter;
import com.oranges.cnmall.adapter.WaresAdapter;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.decortion.DividerGridItemDecoration;
import com.oranges.cnmall.adapter.decortion.DividerItemDecoration;
import com.oranges.cnmall.bean.Banner;
import com.oranges.cnmall.bean.Category;
import com.oranges.cnmall.bean.Page;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.BaseCallback;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oranges on 16/9/22.
 */
@ContentView(R.layout.fragment_category)
public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @ViewInject(R.id.rv_category)
    RecyclerView rvCategory;
    @ViewInject(R.id.sl_category_slider)
    SliderLayout slSlider;
    @ViewInject(R.id.mrl_refresh)
    MaterialRefreshLayout mrlRefresh;
    @ViewInject(R.id.rv_wares)
    RecyclerView rvWares;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;
    private int pageSize = 6;
    private int totalPage = 1;
    private int curPage = 1;
    private long mCategoryId = 0;
    private int curState = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initRefreshLayout();
        getCategoryData();
        getSliderBanner();
        return view;
    }

    // 设置mrlRefresh属性
    private void initRefreshLayout() {
        // 启用加载更多
        mrlRefresh.setLoadMore(true);
        // 手势监听
        mrlRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            // 下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            // 上拉加载更多
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage < totalPage)
                    loadMoreData();
                else {
                    mrlRefresh.finishRefreshLoadMore();
                    Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 加载数据(下拉刷新)
    private void refreshData() {
        curPage = 1;
        curState = STATE_REFRESH;
        getWares(mCategoryId);
    }

    // 加载数据(上拉加载更多)
    private void loadMoreData() {
        curPage += 1;
        curState = STATE_MORE;
        getWares(mCategoryId);
    }

    // 加载一级分类
    private void getCategoryData() {
        httpHelper.get(Const.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                // 默认显示第一个分类下的商品
                if (null != categories && categories.size() > 0){
                    mCategoryId = categories.get(0).getId();
                    getWares(mCategoryId);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 显示一级分类
    private void showCategoryData(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getActivity(), categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                // 点击分类显示该分类下的商品
                Category category = mCategoryAdapter.getData(position);
                mCategoryId = category.getId();
                curPage = 1;
                curState = STATE_NORMAL;
                getWares(category.getId());
            }
        });
        rvCategory.setAdapter(mCategoryAdapter);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCategory.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // rvCategory.setItemAnimator(new DefaultItemAnimator());
    }

    // 加载轮播图
    private void getSliderBanner() {
        String url = Const.API.BANNER + "?type=1";
        httpHelper.get(url, new SpotsCallback<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderBanner(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 显示轮播图
    private void showSliderBanner(List<Banner> bannerList) {
        // 绑定数据
        if (bannerList != null) {
            for (Banner banner : bannerList) {
                DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                defaultSliderView.image(banner.getImgUrl());
                defaultSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                slSlider.addSlider(defaultSliderView);
            }
        }
        // 设置指示器底部居中
        slSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slSlider.setCustomAnimation(new DescriptionAnimation());
        slSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        slSlider.setDuration(3000);
        slSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 加载分类下的商品
    private void getWares(long categoryId) {
        String url = Const.API.WARES_LIST + "?curPage=" + curPage + "&pageSize=" + pageSize + "&categoryId=" + categoryId;
        httpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showWares(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    // 显示分类下的商品
    private void showWares(List<Wares> waresList) {
        switch (curState) {
            case STATE_NORMAL: // 正常(默认)
                if (null == mWaresAdapter) {
                    mWaresAdapter = new WaresAdapter(getActivity(), waresList);
                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void OnClick(View view, int position) {
                            // 跳转商品详情
                            Wares wares = mWaresAdapter.getData(position);
                            Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                            intent.putExtra(Const.WARE,wares);
                            startActivity(intent);
                        }
                    });
                    rvWares.setAdapter(mWaresAdapter);
                    rvWares.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    rvWares.addItemDecoration(new DividerGridItemDecoration(getActivity()));
                }else{
                    mWaresAdapter.clear();
                    mWaresAdapter.addData(waresList);
                }
                break;
            case STATE_REFRESH: // 下拉刷新
                mWaresAdapter.refreshData(waresList);
                // 滚动到初始位置
                rvWares.scrollToPosition(0);
                mrlRefresh.finishRefresh();
                break;
            case STATE_MORE: // 上拉加载更多
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), waresList);
                // 滚动到最后
                rvWares.scrollToPosition(mWaresAdapter.getDatas().size());
                mrlRefresh.finishRefreshLoadMore();
                break;
        }
    }

}



