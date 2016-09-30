package com.oranges.cnmall.adapter.base;

import android.content.Context;

import java.util.List;

/**
 * 对Adapter的简化 SimpleAdapter
 * Created by oranges on 2016/9/23.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

}
