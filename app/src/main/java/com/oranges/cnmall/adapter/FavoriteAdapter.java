package com.oranges.cnmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.Favorites;
import com.oranges.cnmall.bean.Wares;

import java.util.List;

/**
 * 我的收藏adapter FavoriteAdapter
 * Created by oranges on 2016/9/28.
 */
public class FavoriteAdapter extends SimpleAdapter<Favorites> {

    public FavoriteAdapter(Context context, List<Favorites> datas) {
        super(context, R.layout.template_favorite, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Favorites favorites) {
        Wares wares = favorites.getWares();
        SimpleDraweeView sdvImg = (SimpleDraweeView) viewHolder.getView(R.id.sdv_img);
        sdvImg.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.tv_title).setText(wares.getName());
        viewHolder.getTextView(R.id.tv_price).setText("￥ " + wares.getPrice());

        Button buttonRemove = viewHolder.getButton(R.id.btn_remove);
        Button buttonLike = viewHolder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


}
