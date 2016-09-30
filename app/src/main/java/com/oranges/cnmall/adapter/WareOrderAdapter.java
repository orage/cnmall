package com.oranges.cnmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.ShoppingCart;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 商品订单 WaresAdapter
 * Created by oranges on 2016/9/26.
 */
public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {

    private List<ShoppingCart> datas;
    private Context context;

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, R.layout.template_order_wares, datas);
        this.datas = datas;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final ShoppingCart item) {
        ImageView simpleDraweeView = (ImageView) viewHolder.getView(R.id.sdv_view);
        // TODO resize() 自适应分辨率的问题
        Picasso.with(context).load(Uri.parse(item.getImgUrl())).resize(150,150).centerCrop().into(simpleDraweeView);
    }

    // 获得总价
    public float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;
        for (ShoppingCart cart : datas) {
            sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    // 是否为空
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }
}
