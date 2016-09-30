package com.oranges.cnmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.adapter.AddressAdapter;
import com.oranges.cnmall.adapter.decortion.DividerItemDecoration;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Address;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.msg.base.BaseRespMsg;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ContentView(R.layout.activity_address_list)
public class AddressListActivity extends BaseActivity {

    @ViewInject(R.id.tb_al_toolbar)
    private MyToolBar toolbar;
    @ViewInject(R.id.rv_content)
    private RecyclerView rvContent;
    private AddressAdapter mAdapter;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void init() {
        initToolbar();
        initAddress();
    }

    // 初始化
    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddActivity();
            }
        });
    }

    // 去添加地址
    private void toAddActivity() {
        Intent intent = new Intent(this, AddressAddActivity.class);
        startActivityForResult(intent, Const.REQUEST_DO_ADDRESS_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 添加完地址初始化一次获得最新的地址数据
        initAddress();
    }

    // 初始化收货地址
    private void initAddress() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MyApplication.getInstance().getUser().getId());

        mHttpHelper.get(Const.API.ADDRESS_LIST, params, new SpotsCallback<List<Address>>(this) {

            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 显示地址
    private void showAddress(List<Address> addresses) {
        Collections.sort(addresses);
        if (mAdapter == null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressListener() {
                @Override
                public void setDefault(Address address) {
                    updateAddress(address);
                }

                @Override
                public void modifyAddress(Address address) {
                    Intent intent = new Intent(AddressListActivity.this, AddressAddActivity.class);
                    intent.putExtra("address", address);
                    startActivity(intent);
                    // 结束后刷新一次数据
                    initAddress();
                }

                @Override
                public void deleteAddress(Address address) {
                    delAddress(address);
                }
            });
            rvContent.setAdapter(mAdapter);
            rvContent.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else {
            mAdapter.refreshData(addresses);
            rvContent.setAdapter(mAdapter);
        }

    }

    // 更新地址
    public void updateAddress(Address address) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("id", address.getId());
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault());
        mHttpHelper.post(Const.API.ADDRESS_UPDATE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });

    }

    // 删除地址
    public void delAddress(Address address) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", address.getId());
        mHttpHelper.post(Const.API.ADDRESS_DELETE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });

    }
}
