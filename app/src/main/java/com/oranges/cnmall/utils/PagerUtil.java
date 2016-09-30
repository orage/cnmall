package com.oranges.cnmall.utils;

import android.content.Context;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.oranges.cnmall.bean.Page;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对分页功能的封装 PagerUtil
 * Created by oranges on 2016/9/24.
 */
public class PagerUtil {

    private OkHttpHelper httpHelper;
    private static Builder builder;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int curState = STATE_NORMAL;

    private PagerUtil() {
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        builder = new Builder();
        builder.params = new HashMap<>(5);
        return builder;
    }

    // 对外开放网络请求方法
    public void request() {
        requestData();
    }

    // 对外开放设置请求地址的参数(已build的情况下)
    public void putParam(String key, Object value) {
        builder.params.put(key, value);
    }

    // 设置mrlRefresh属性
    private void initRefreshLayout() {
        // 启用加载更多
        builder.mrlRefresh.setLoadMore(builder.canLoadMore);
        // 手势监听
        builder.mrlRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            // 下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            // 上拉加载更多
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.curPage < builder.totalPage)
                    loadMoreData();
                else {
                    builder.mrlRefresh.finishRefreshLoadMore();
                }
            }
        });
    }

    // 加载数据(下拉刷新)
    private void refreshData() {
        builder.curPage = 1;
        curState = STATE_REFRESH;
        requestData();
    }

    // 加载数据(上拉加载更多)
    private void loadMoreData() {
        builder.curPage += 1;
        curState = STATE_MORE;
        requestData();
    }

    // 加载数据
    private void requestData() {
        httpHelper.get(buildUrl(), new RequestCallBack(builder.context));
    }

    // 展示数据
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        switch (curState) {
            case STATE_NORMAL: // 正常(默认)
                if (null != builder.onPageListener)
                    builder.onPageListener.load(datas, totalPage, totalCount);
                break;
            case STATE_REFRESH: // 下拉刷新
                if (null != builder.onPageListener)
                    builder.onPageListener.refresh(datas, totalPage, totalCount);
                builder.mrlRefresh.finishRefresh();
                break;
            case STATE_MORE: // 上拉加载更多
                if (null != builder.onPageListener)
                    builder.onPageListener.loadMore(datas, totalPage, totalCount);
                builder.mrlRefresh.finishRefreshLoadMore();
                break;
        }
    }

    // URL拼接
    private String buildUrl() {
        return builder.url + "?" + buildUrlParams();
    }

    // HashMap转String(条件)
    private String buildUrlParams() {
        HashMap<String, Object> map = builder.params;

        map.put("curPage", builder.curPage);
        map.put("pageSize", builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) { // 如果最后一位是&
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }


    // 需要用户传入部分
    public static class Builder {
        private String url;
        private Context context;
        private Type type;
        private MaterialRefreshLayout mrlRefresh;
        private boolean canLoadMore;

        private OnPageListener onPageListener;

        private int curPage = 1;
        private int pageSize = 8;
        private int totalPage = 1;

        private HashMap<String, Object> params;

        // 设置请求地址
        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        // 设置请求地址的参数
        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        // 设置每页加载条目数
        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }

        // 设置MaterialRefreshLayout控件(加载更多实现的必须)
        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mrlRefresh = refreshLayout;
            return builder;
        }

        // 设置是否需要加载更多
        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;
            return builder;
        }

        // 设置监听器
        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        // 需要上下文对象
        public PagerUtil build(Context context, Type type) {
            this.context = context;
            this.type = type;
            validateParam();
            return new PagerUtil();
        }

        // 验证主要参数不为NULL
        private void validateParam() {
            if (this.context == null)
                throw new RuntimeException("context can't be null");
            if (this.url == null || "".equals(this.url))
                throw new RuntimeException("url can't be null");
            if (this.mrlRefresh == null)
                throw new RuntimeException("MaterialRefreshLayout can't be null");
        }
    }

    // 数据刷新或加载更多的接口
    public interface OnPageListener<T> {
        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }


    // 请求回调
    class RequestCallBack<T> extends SpotsCallback<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);
            super.type = builder.type;
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            builder.curPage = page.getCurrentPage();
            builder.totalPage = page.getTotalPage();
            showData(page.getList(), builder.totalPage, page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {

        }
    }
}
