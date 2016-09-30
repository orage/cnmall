package com.oranges.cnmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.AddressListActivity;
import com.oranges.cnmall.activity.LoginActivity;
import com.oranges.cnmall.activity.MyFavoriteActivity;
import com.oranges.cnmall.activity.MyOrderActivity;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.fragment.base.BaseFragment;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Oranges on 16/9/22.
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.civ_head)
    private CircleImageView civHead;
    @ViewInject(R.id.tv_username)
    private TextView tvUsername;
    @ViewInject(R.id.btn_logout)
    private Button btnLogout;
    @ViewInject(R.id.tv_my_orders)
    private TextView tvMyOrders;
    @ViewInject(R.id.tv_my_favorite)
    private TextView tvMyFavorite;
    @ViewInject(R.id.tv_my_address)
    private TextView tvMyAddress;

    @Override
    public View initX(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    // 初始化
    @Override
    public void init() {
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }

    @Event(value = {R.id.tv_my_orders, R.id.tv_my_favorite, R.id.tv_my_address},
            type = View.OnClickListener.class)
    private void onClickMenu(View view) {
        switch (view.getId()) {
            case R.id.tv_my_orders: // 我的订单
                startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
                break;
            case R.id.tv_my_favorite: // 我的收藏
                startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
                break;
            case R.id.tv_my_address: // 收货地址
                startActivity(new Intent(getActivity(), AddressListActivity.class),true);
                break;
        }
    }

    // 去登录
    @Event(value = {R.id.civ_head, R.id.tv_username},
            type = View.OnClickListener.class)
    private void toLogin(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Const.REQUEST_DO_LOGIN);
    }

    // 退出登录
    @Event(value = {R.id.btn_logout}, type = View.OnClickListener.class)
    private void logout(View view) {
        MyApplication.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }

    // 显示登录的User信息
    private void showUser(User user) {
        if (null != user) {
            btnLogout.setVisibility(View.VISIBLE);
            if (null == user.getLogo_url() || "".equals(user.getLogo_url()))
                civHead.setImageResource(R.drawable.icon_default_head);
            else
                Picasso.with(getActivity()).load(user.getLogo_url()).into(civHead);
            if (null == user.getUsername() || "".equals(user.getUsername()))
                tvUsername.setText("None");
            else
                tvUsername.setText(user.getUsername());
            civHead.setEnabled(false);
            tvUsername.setEnabled(false);
        } else {
            btnLogout.setVisibility(View.GONE);
            tvUsername.setText(R.string.to_login);
            civHead.setImageResource(R.drawable.icon_default_head);
            civHead.setEnabled(true);
            tvUsername.setEnabled(true);
        }
    }
}
