package com.oranges.cnmall.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oranges.cnmall.R;
import com.oranges.cnmall.bean.Wares;

import java.util.List;


/**
 * Created by oranges on 16/9/30.
 */
public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder> {

    private List<Wares> mDatas;
    private LayoutInflater mInflater;

    public HotWaresAdapter(List<Wares> wares,LayoutInflater inflater) {
        mDatas = wares;
        mInflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.template_hot_wares, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wares wares = getData(position);
        // 绑定数据
        holder.sdvImg.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.tvTitle.setText(wares.getName());
        holder.tvPrice.setText("￥" + wares.getPrice());
    }

    // 获得数据对象
    public Wares getData(int position) {
        return mDatas.get(position);
    }

    // 获得数据列表
    public List<Wares> getDatas() {
        return mDatas;
    }

    // 清除数据
    public void clearData() {
        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }

    // 添加数据
    public void addData(List<Wares> datas) {
        addData(0, datas);
    }

    // 添加数据(指定位置)
    public void addData(int position, List<Wares> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0)
            return mDatas.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvImg;
        TextView tvTitle;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            sdvImg = (SimpleDraweeView) itemView.findViewById(R.id.sdv_img);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
