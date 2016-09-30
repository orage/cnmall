package com.oranges.cnmall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.bean.Tab;
import com.oranges.cnmall.fragment.CartFragment;
import com.oranges.cnmall.fragment.CategoryFragment;
import com.oranges.cnmall.fragment.HomeFragment;
import com.oranges.cnmall.fragment.HotFragment;
import com.oranges.cnmall.fragment.MineFragment;
import com.oranges.cnmall.widget.FragmentTabHost;
import com.oranges.cnmall.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private CartFragment cartFragment;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    public void init() {
        // 初始化view
        initView();
    }

    private void initView() {
        // 初始化tab
        initTab();
    }

    private void initTab() {
        // 实例化tab
        Tab tabHome = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tabHot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tabCategory = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tabCart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tabMine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);
        // 给mTabs添加tab
        mTabs.add(tabHome);
        mTabs.add(tabHot);
        mTabs.add(tabCategory);
        mTabs.add(tabCart);
        mTabs.add(tabMine);
        // 实例化mTabHost
        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_tab_content);
        // 给mTabHost添加mTabs
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
        // 去掉分隔线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        // 监听选中状态
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)) {
                    refData();
                }
            }
        });
        mTabHost.setCurrentTab(0);
    }

    // 刷新购物车数据
    private void refData() {
        if (null == cartFragment) {
            Fragment f = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (null != f) {
                cartFragment = (CartFragment) f;
                cartFragment.refData();
            }
        } else {
            cartFragment.refData();
        }
    }

    // 封装tab的Indicator
    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon_tab);
        TextView tvTxt = (TextView) view.findViewById(R.id.tv_txt_indicator);
        ivIcon.setBackgroundResource(tab.getIcon());
        tvTxt.setText(tab.getTitle());
        return view;
    }

}
