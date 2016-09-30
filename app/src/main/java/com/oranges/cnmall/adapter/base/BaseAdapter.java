package com.oranges.cnmall.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * 对Adapter的简单封装 BaseAdapter
 * Created by oranges on 2016/9/23.
 */
public abstract class BaseAdapter<T, Holder extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected int mLayoutResId;
    protected OnItemClickListener mListener;


    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        mDatas = (datas == null ? Collections.synchronizedList(new ArrayList<T>()) : datas);
        mLayoutResId = layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, null, false);
        BaseViewHolder baseViewHolder = new BaseViewHolder(view, mListener);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getData(position);
        convert((Holder) holder, t);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // item点击事件接口
    public interface OnItemClickListener {
        void OnClick(View view, int position);
    }

    // 获得ListIterator
    private ListIterator getListIterator() {
        return mDatas.listIterator();
    }

    // 获得数据对象
    public T getData(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    // 获得数据列表
    public List<T> getDatas() {
        return mDatas;
    }

    // 清除数据
    public void clearData() {
        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }

    // 添加数据
    public void addData(List<T> datas) {
        addData(0, datas);
    }

    // 添加数据(指定位置)
    public void addData(int position, List<T> datas) {
        if (datas == null && datas.size() <= 0) {
            return;
        }
        for (T t : datas) {
            mDatas.add(position, t);
            notifyItemInserted(position);
        }
    }

    // 清除数据
    public void clear() {
        if (mDatas == null || mDatas.size() <= 0)
            return;
        for (ListIterator it = mDatas.listIterator(); it.hasNext(); ) {
            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }

    // 删除
    public void removeItem(T t) {
        int position = mDatas.indexOf(t);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    // 刷新
    public void refreshData(List<T> list) {
        clear();
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    // 加载更多
    public void loadMoreData(List<T> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            int begin = mDatas.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(list.get(i));
                notifyItemInserted(i + begin);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() <= 0)
            return 0;
        return mDatas.size();
    }

    // 抽象绑定数据方法
    protected abstract void convert(Holder holder, T t);

    // 抽象绑定数据方法(旧)
    // public abstract void bindData(BaseViewHolder holder, T t);
}
