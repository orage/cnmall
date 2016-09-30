package com.oranges.cnmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.provider.CartProvider;

import java.util.List;

/**
 * 热卖商品adapter HWAdapter
 * Created by oranges on 2016/9/23.
 */
public class HWAdapter extends SimpleAdapter<Wares> {

    CartProvider provider;
    ImagePipeline imagePipeline;
    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        provider = new CartProvider(context);
        imagePipeline = Fresco.getImagePipeline();
    }

    @Override
    protected void convert(BaseViewHolder holder, final Wares wares) {
        SimpleDraweeView sdvImg = (SimpleDraweeView) holder.getView(R.id.sdv_img);
        // 绑定数据
        sdvImg.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.tv_title).setText(wares.getName());
        holder.getTextView(R.id.tv_price).setText("￥" + wares.getPrice());
        Button button = holder.getButton(R.id.btn_add);
        if (null != button)
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击加入购物车
                    provider.put(wares);
                }
            });
    }


    // 更改布局
    public void resetLayout(int layoutId) {
        this.mLayoutResId = layoutId;
        notifyItemRangeChanged(0, getDatas().size());
    }
}
