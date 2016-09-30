package com.oranges.cnmall.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.adapter.WareOrderAdapter;
import com.oranges.cnmall.adapter.layoutmanager.FullyLinearLayoutManager;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Charge;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.msg.CreateOrderRespMsg;
import com.oranges.cnmall.msg.base.BaseRespMsg;
import com.oranges.cnmall.provider.CartProvider;
import com.oranges.cnmall.utils.JSONUtil;
import com.oranges.cnmall.widget.MyToolBar;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下单 CreateOrderActivity
 * Created by oranges on 2016/9/26.
 */
@ContentView(R.layout.activity_create_order)
public class CreateOrderActivity extends BaseActivity {

    @ViewInject(R.id.tb_co_toolbar)
    private MyToolBar toolbar;
    @ViewInject(R.id.rv_order)
    private RecyclerView rvOrder;
    @ViewInject(R.id.rl_alipay)
    private RelativeLayout rlAlipay;
    @ViewInject(R.id.rl_wechat)
    private RelativeLayout rlWechat;
    @ViewInject(R.id.rl_bdpay)
    private RelativeLayout rlBdpay;
    @ViewInject(R.id.rb_alipay)
    private RadioButton rbAlipay;
    @ViewInject(R.id.rb_webchat)
    private RadioButton rbWechat;
    @ViewInject(R.id.rb_bdpay)
    private RadioButton rbBdpay;
    @ViewInject(R.id.tv_total)
    private TextView tvTotal;
    @ViewInject(R.id.btn_create_order)
    private Button btnCreateOrder;

    private CartProvider cartProvider;
    private WareOrderAdapter mAdapter;
    private String orderNum; // 订单数
    private String payChannel = Const.PAY.CHANNEL_ALIPAY; // 默认支付方式
    private float amount; // 应付
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private HashMap<String, RadioButton> channels = new HashMap<>(3);
    private List<ShoppingCart> cartsList;

    @Override
    public void init() {
        // 拿到cart说明是从WareDetailActivity跳转
        initToolbar();
        initData(getShoppingCart());
        initOther();
    }

    // 拿到ShoppingCart
    private List<ShoppingCart> getShoppingCart() {
        ShoppingCart cart = (ShoppingCart) getIntent().getSerializableExtra("cart");
        if (null != cart) {
            cartsList = new ArrayList<>(1);
            cartsList.add(cart);
            return cartsList;
        }
        return null;
    }

    // 初始化Toolbar
    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 获取购物车订单数据
    public void initData(List<ShoppingCart> cartList) {
        if (null != cartList) {
            mAdapter = new WareOrderAdapter(this, cartList);
        } else {
            cartProvider = new CartProvider(this);
            mAdapter = new WareOrderAdapter(this, cartProvider.getAll());
        }
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(mAdapter);
    }

    // 初始化应付金额和监听器
    public void initOther() {
        channels.put(Const.PAY.CHANNEL_ALIPAY, rbAlipay);
        channels.put(Const.PAY.CHANNEL_WECHAT, rbWechat);
        channels.put(Const.PAY.CHANNEL_BFB, rbBdpay);
        if(cartsList==null)
            amount = mAdapter.getTotalPrice();
        else
            amount = cartsList.get(0).getPrice();
        tvTotal.setText("应付款：￥" + amount);
    }

    @Event(value = {R.id.rl_alipay, R.id.rl_wechat, R.id.rl_bdpay},
            type = View.OnClickListener.class)
    private void onClick(View v) {
        selectPayChannel(v.getTag().toString());
    }

    // 选择支付方式的判断
    public void selectPayChannel(String payChannel) {
        for (Map.Entry<String, RadioButton> entry : channels.entrySet()) {
            this.payChannel = payChannel;
            RadioButton rb = entry.getValue();
            if (entry.getKey().equals(payChannel)) {
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            } else
                rb.setChecked(false);
        }
    }

    @Event(value = R.id.btn_create_order, type = View.OnClickListener.class)
    private void createNewOrder(View view) {
        postNewOrder();
    }

    // 提交新订单
    private void postNewOrder() {
        final List<ShoppingCart> carts = mAdapter.getDatas();
        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c : carts) {
            WareItem item = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(item);
        }
        String item_json = JSONUtil.toJSON(items);
        Map<String, Object> params = new HashMap<>(5);
        params.put("user_id", MyApplication.getInstance().getUser().getId() + "");
        params.put("item_json", item_json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount);
        params.put("addr_id", 1 + "");
        btnCreateOrder.setEnabled(false);
        httpHelper.post(Const.API.ORDER_CREATE, params, new SpotsCallback<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {
                if(null==cartsList){
                    // 清除购物车已买的商品
                    cartProvider.delete(carts);
                }
                btnCreateOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();
                openPaymentActivity(JSONUtil.toJSON(charge));
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                btnCreateOrder.setEnabled(true);
            }
        });
    }

    // 跳转支付界面
    private void openPaymentActivity(String charge) {
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Const.REQUEST_DO_PAYMENT);
    }

    //支付页面返回处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 处理返回值
        // "success" - payment succeed
        // "fail"    - payment failed
        // "cancel"  - user canceld
        // "invalid" - payment plugin not installed
        // 如果是银联渠道返回 invalid 调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件
        if (requestCode == Const.REQUEST_DO_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                if (result.equals(Const.PAY_RESULT.SUCCESS))
                    changeOrderStatus(Const.PAY_RESULT.RESULT_SUCCESS);
                else if (result.equals(Const.PAY_RESULT.FAIL))
                    changeOrderStatus(Const.PAY_RESULT.RESULT_FAIL);
                else if (result.equals(Const.PAY_RESULT.CANCEL))
                    changeOrderStatus(Const.PAY_RESULT.RESULT_CANCEL);
                else
                    changeOrderStatus(Const.PAY_RESULT.RESULT_OTHERS);
            }
        }
    }

    // 更新订单状态
    private void changeOrderStatus(final int status) {
        Map<String, Object> params = new HashMap<>(5);
        params.put("order_num", orderNum);
        params.put("status", status + "");
        httpHelper.post(Const.API.ORDER_COMPLEPE, params, new SpotsCallback<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {
                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });
    }

    // 支付结果界面
    private void toPayResultActivity(int status) {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
        this.finish();
    }


    // 对提交的订单封装
    class WareItem {
        private Long ware_id; // 商品id
        private int amount; // 总价(单价*数量)

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
