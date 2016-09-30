package com.oranges.cnmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.adapter.HWAdapter;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.decortion.DividerItemDecoration;
import com.oranges.cnmall.bean.Page;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.utils.PagerUtil;
import com.oranges.cnmall.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by oranges on 2016/9/24.
 */
@ContentView(R.layout.activity_warelist)
public class WareListActivity extends BaseActivity implements PagerUtil.OnPageListener, TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "WareListActivity";

    private static final int TAG_DEFAULT = 0;
    private static final int TAG_SALE = 1;
    private static final int TAG_PRICE = 2;

    private static final int ACTION_LIST = 100;
    private static final int ACTION_GIRD = 101;

    @ViewInject(R.id.tb_wl_toolbar)
    private MyToolBar toolbar;
    @ViewInject(R.id.tl_tab)
    private TabLayout tlTab;
    @ViewInject(R.id.tv_summary)
    private TextView tvSummary;
    @ViewInject(R.id.mrl_refresh)
    private MaterialRefreshLayout mrlRefresh;
    @ViewInject(R.id.rv_wares)
    private RecyclerView rvWares;

    private HWAdapter mWaresAdapter;
    private int orderBy = 0; // 排序(0-默认 1-销售 2-价格)
    private long campaignId = 0; // 活动id
    private PagerUtil pager;

    // 初始化
    @Override
    public void init() {
        campaignId = getIntent().getLongExtra(Const.CAMPAIGN_ID, 0);
        initToolbar();
        initTab();
        getData();
    }

    // 初始化Toolbar
    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getRightButton().setTag(ACTION_LIST);
        toolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        toolbar.setRightButtonOnClickListener(this);
    }

    // 初始化TabLayout
    private void initTab() {
        TabLayout.Tab tab = tlTab.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        tlTab.addTab(tab);

        tab = tlTab.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        tlTab.addTab(tab);

        tab = tlTab.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        tlTab.addTab(tab);

        tlTab.setOnTabSelectedListener(this);
    }

    // 获取数据
    private void getData() {
        System.out.println("campaignId:" + campaignId + ",orderBy:" + orderBy);
        pager = PagerUtil.newBuilder()
                .setUrl(Const.API.WARES_CAMPAIGN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mrlRefresh)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());
        pager.request();
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        switch (action) {
            case ACTION_LIST: // 列表布局
                toolbar.setRightButtonIcon(R.drawable.icon_list_32);
                toolbar.getRightButton().setTag(ACTION_GIRD);
                rvWares.setLayoutManager(new GridLayoutManager(this, 2));
                mWaresAdapter.resetLayout(R.layout.template_grid_wares);
                break;
            case ACTION_GIRD: // 网格布局
                toolbar.setRightButtonIcon(R.drawable.icon_grid_32);
                toolbar.getRightButton().setTag(ACTION_LIST);
                rvWares.setLayoutManager(new LinearLayoutManager(this));
                mWaresAdapter.resetLayout(R.layout.template_hot_wares);
                break;
        }
    }

    // -- start OnPageListener
    @Override
    public void load(List datas, int totalPage, int totalCount) {
        tvSummary.setText("共找到" + totalCount + "件商品");
        if (null == mWaresAdapter) {
            mWaresAdapter = new HWAdapter(this, datas);
            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    // 跳转商品详情
                    Wares wares = mWaresAdapter.getData(position);
                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);
                    intent.putExtra(Const.WARE,wares);
                    startActivity(intent);
                }
            });
            rvWares.setAdapter(mWaresAdapter);
            rvWares.setLayoutManager(new LinearLayoutManager(this));
            rvWares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else
            mWaresAdapter.refreshData(datas);
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        rvWares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }
    // end --

    // -- start OnTabSelectedListener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // 根据排序方式加载数据
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    // end --
}
