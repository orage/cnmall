package com.oranges.cnmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.provider.CartProvider;
import com.oranges.cnmall.widget.CartCalculationView;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {

    public static final String TAG = "CartAdapter";
    private CheckBox cbAll;
    private TextView tvTotal;
    private List<ShoppingCart> datas;
    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox cbAll, TextView tvTotal) {
        super(context, R.layout.template_cart, datas);
        this.datas = datas;
        this.cbAll = cbAll;
        this.tvTotal = tvTotal;
        cartProvider = new CartProvider(context);
        setOnItemClickListener(this);
        showTotalPrice();
        cbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllORNone(cbAll.isChecked());
                showTotalPrice();
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, final ShoppingCart cart) {
        SimpleDraweeView sdvImg = (SimpleDraweeView) holder.getView(R.id.sdv_img);
        CheckBox cbSelect = (CheckBox) holder.getView(R.id.cb_select);
        CartCalculationView ccvControl = (CartCalculationView) holder.getView(R.id.ccv_control);
        // 绑定数据
        sdvImg.setImageURI(Uri.parse(cart.getImgUrl()));
        cbSelect.setChecked(cart.isChecked());
        holder.getTextView(R.id.tv_title).setText(cart.getName());
        holder.getTextView(R.id.tv_price).setText("￥" + cart.getPrice());
        ccvControl.setValue(cart.getCount());
        ccvControl.setOnButtonClickListener(new CartCalculationView.OnButtonClickListener() {
            @Override
            public void onAddClick(View view, int value) { // 增加数量
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }

            @Override
            public void onSubClick(View view, int value) { // 减少数量
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }
        });
    }

    // 计算总价
    private float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;
        for (ShoppingCart cart :
                datas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    // 显示总价
    public void showTotalPrice(){
        float total = getTotalPrice();
        tvTotal.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"),
                TextView.BufferType.SPANNABLE);
    }

    // 判断数据不为null
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }

    // 全选按钮的点击监听(点击全选或不选)
    public void checkAllORNone(boolean isChecked){
        if(!isNull())
            return;
        int i = 0;
        for (ShoppingCart cart:datas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    // 全选按钮状态监听
    private void checkListener(){
        int count = 0;
        int checkNum = 0;
        if(datas!=null){
            count = datas.size();
            for (ShoppingCart cart:datas){
                if(!cart.isChecked()){
                    cbAll.setChecked(false);
                    break;
                }else {
                    checkNum +=1;
                }
            }
            if (count==checkNum)
                cbAll.setChecked(true);
        }
    }

    // 删除选中商品
    public void delCart(){
        if (!isNull())
            return;
        /* 循环中改变datas的长度会报错
        for (ShoppingCart cart:datas){
            if (cart.isChecked()){
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                datas.remove(cart);
                notifyItemRemoved(position);
            }
        }*/
        for (Iterator iterator = datas.iterator();iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()){
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void OnClick(View view, int position) {
       ShoppingCart cart = getData(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);
        // 实时计算
        checkListener();
        showTotalPrice();
    }

}
