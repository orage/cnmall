package com.oranges.cnmall.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.Wares;

import java.util.List;

/**
 * 分类下的商品 WaresAdapter
 * Created by oranges on 2016/9/23.
 */
public class WaresAdapter extends SimpleAdapter<Wares> {


    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Wares wares) {
        SimpleDraweeView sdvImg = (SimpleDraweeView) holder.getView(R.id.sdv_img);
        // 绑定数据
        sdvImg.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.tv_title).setText(wares.getName());
        holder.getTextView(R.id.tv_price).setText("￥"+wares.getPrice());
    }
}
