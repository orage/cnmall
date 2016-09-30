package com.oranges.cnmall.adapter;

import android.content.Context;

import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.Category;

import java.util.List;

/**
 * 一级分类adapter CategoryAdapter
 * Created by oranges on 2016/9/23.
 */
public class CategoryAdapter extends SimpleAdapter<Category>{

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category category) {
        // 绑定数据
        holder.getTextView(R.id.tv_category).setText(category.getName());
    }

}
