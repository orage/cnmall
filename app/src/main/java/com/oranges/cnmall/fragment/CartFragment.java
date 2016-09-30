package com.oranges.cnmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.CreateOrderActivity;
import com.oranges.cnmall.adapter.CartAdapter;
import com.oranges.cnmall.adapter.decortion.DividerItemDecoration;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.fragment.base.BaseFragment;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.provider.CartProvider;
import com.oranges.cnmall.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by Oranges on 16/9/22.
 */
@ContentView(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "CartFragment";
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @ViewInject(R.id.tb_cart_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.rv_cart)
    private RecyclerView rvCart;
    @ViewInject(R.id.cb_all)
    private CheckBox cbAll;
    @ViewInject(R.id.tv_total)
    private TextView tvTotal;
    @ViewInject(R.id.btn_order)
    private Button btnOrder;
    @ViewInject(R.id.btn_del)
    private Button btnDel;

    private static CartFragment cartFragment;
    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    @Override
    public View initX(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getActivity());
        initToolbar();
        showData();
    }

    // 初始化Toolbar参数
    private void initToolbar() {
        toolBar.getRightButton().setOnClickListener(this);
        toolBar.getRightButton().setTag(ACTION_EDIT);
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_EDIT == action) { // 编辑
            showDelControl();
        } else if (ACTION_CAMPLATE == action) { // 完成
            hideDelControl();
        }
    }

    // 显示编辑状态
    private void showDelControl() {
        toolBar.getRightButton().setText("完成");
        tvTotal.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
        btnDel.setVisibility(View.VISIBLE);
        toolBar.getRightButton().setTag(ACTION_CAMPLATE);
        mAdapter.checkAllORNone(false);
        cbAll.setChecked(false);
    }

    // 隐藏编辑状态
    private void hideDelControl() {
        toolBar.getRightButton().setText("编辑");
        tvTotal.setVisibility(View.VISIBLE);
        btnOrder.setVisibility(View.VISIBLE);
        btnDel.setVisibility(View.GONE);
        toolBar.getRightButton().setTag(ACTION_EDIT);
        mAdapter.checkAllORNone(true);
        cbAll.setChecked(true);
    }

    // 显示购物车数据
    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter = new CartAdapter(getActivity(), carts, cbAll, tvTotal);
        rvCart.setAdapter(mAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    // 刷新购物车数据(供MainActivity调用)
    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.refreshData(carts);
        mAdapter.showTotalPrice();
    }

    // 删除购物车选中商品
    @Event(value = R.id.btn_del, type = View.OnClickListener.class)
    private void delCart(View view) {
        mAdapter.delCart();
        mAdapter.showTotalPrice();
    }

    // 结算
    @Event(value = R.id.btn_order, type = View.OnClickListener.class)
    private void getOrder(View view) {
        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
        startActivity(intent, true);
    }
}
