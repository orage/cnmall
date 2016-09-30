package com.oranges.cnmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.WareListActivity;
import com.oranges.cnmall.adapter.HomeCategoryAdapter;
import com.oranges.cnmall.adapter.decortion.CardViewItemDecoration;
import com.oranges.cnmall.bean.Banner;
import com.oranges.cnmall.bean.Campaign;
import com.oranges.cnmall.bean.HomeCampaign;
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
@ContentView(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private List<Banner> mBannerList;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    @ViewInject(R.id.sl_home_slider)
    SliderLayout slSlider;
    @ViewInject(R.id.rv_content)
    RecyclerView rvContent;
    HomeCategoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initRecyclerView();
        requestImages();
        return view;
    }

    private void requestImages() {
        String url = Const.API.BANNER + "?type=1";
        httpHelper.get(url, new SpotsCallback<List<Banner>>(getActivity()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBannerList = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 初始化商品分类
    private void initRecyclerView() {
        httpHelper.get(Const.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
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

    // 初始化轮播图
    private void initSlider() {
        // 绑定数据
        if (mBannerList != null) {
            for (Banner banner : mBannerList) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                slSlider.addSlider(textSliderView);
            }
        }
        // 设置指示器底部居中
        slSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slSlider.setCustomAnimation(new DescriptionAnimation());
        slSlider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        slSlider.setDuration(3000);
    }

    private void initData(List<HomeCampaign> homeCampaignList) {
        mAdapter = new HomeCategoryAdapter(homeCampaignList, getActivity(), LayoutInflater.from(getActivity()));
        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                // 活动跳转
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Const.CAMPAIGN_ID, campaign.getId());
                startActivity(intent);
            }
        });
        rvContent.setAdapter(mAdapter);
        rvContent.addItemDecoration(new CardViewItemDecoration());
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
