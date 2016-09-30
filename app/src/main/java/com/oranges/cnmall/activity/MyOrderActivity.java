package com.oranges.cnmall.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.adapter.MyOrderAdapter;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.decortion.CardViewItemDecoration;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Order;
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

@ContentView(R.layout.activity_my_order)
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    private int status = STATUS_ALL;

    @ViewInject(R.id.tb_mo_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.tl_tab)
    private TabLayout tlTab;
    @ViewInject(R.id.rv_content)
    private RecyclerView rvContent;

    private MyOrderAdapter mAdapter;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public void init() {
        initToolBar();
        initTab();
        getOrders();
    }

    // 初始化Toolbar
    private void initToolBar() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 初始化TabLayout
    private void initTab() {
        TabLayout.Tab tab = tlTab.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        tlTab.addTab(tab);

        tab = tlTab.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        tlTab.addTab(tab);

        tab = tlTab.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        tlTab.addTab(tab);

        tab = tlTab.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        tlTab.addTab(tab);
        tlTab.setOnTabSelectedListener(this);
    }

    // 获取订单信息
    private void getOrders() {
        Long userId = MyApplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("status", status);
        httpHelper.get(Const.API.ORDER_LIST, params, new SpotsCallback<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    // 显示订单信息
    private void showOrders(List<Order> orders) {
        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this, orders);
            rvContent.setAdapter(mAdapter);
            rvContent.setLayoutManager(new LinearLayoutManager(this));
            rvContent.addItemDecoration(new CardViewItemDecoration());
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    toDetailActivity(position);
                }
            });
        } else {
            mAdapter.refreshData(orders);
            rvContent.setAdapter(mAdapter);
        }
    }

    // 订单详情
    private void toDetailActivity(int position) {
//        Intent intent = new Intent(this,OrderDetailActivity.class);
//        Order order = mAdapter.getData(position);
//        intent.putExtra("order",order);
//        startActivity(intent,true);
    }


    // -- start OnTabSelectedListener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    // end --
}
