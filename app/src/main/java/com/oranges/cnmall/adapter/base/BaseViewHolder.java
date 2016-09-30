package com.oranges.cnmall.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 对ViewHolder的简单封装 BaseViewHolder
 * Created by oranges on 2016/9/23.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> views;
    private BaseAdapter.OnItemClickListener listener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        views = new SparseArray<>();
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    // 获得TextView
    public TextView getTextView(int id) {
        return findView(id);
    }

    // 获得ImageView
    public ImageView getImageView(int id){
        return findView(id);
    }

    // 获得Button
    public Button getButton(int id){
        return findView(id);
    }

    // 获得CheckBox
    public CheckBox getCheckBox(int viewId) {
        return findView(viewId);
    }

    // 获得View
    public View getView(int id) {
        return findView(id);
    }

    // 查找View
    private <T extends View> T findView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        // 点击事件回调
        if (listener!=null){
            listener.OnClick(v,getLayoutPosition());
        }
    }
}
