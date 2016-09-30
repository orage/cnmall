package com.oranges.cnmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.WareDetailActivity;
import com.oranges.cnmall.adapter.HWAdapter;
import com.oranges.cnmall.adapter.base.BaseAdapter;
import com.oranges.cnmall.adapter.decortion.DividerItemDecoration;
import com.oranges.cnmall.bean.Page;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.utils.PagerUtil;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Oranges on 16/9/22.
 */
@ContentView(R.layout.fragment_hot)
public class HotFragment extends Fragment implements PagerUtil.OnPageListener{

    private static final String TAG = "HotFragment";

    @ViewInject(R.id.mrl_refresh)
    private MaterialRefreshLayout mrlRefresh;
    @ViewInject(R.id.rv_content)

    private RecyclerView rvContent;
    private HWAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        init();
        return view;

    }

    // 使用PagerUtil请求数据并显示
    private void init(){
        PagerUtil pager = PagerUtil.newBuilder()
                .setUrl(Const.API.WARES_HOT)
                .setPageSize(6)
                .setLoadMore(true)
                .setRefreshLayout(mrlRefresh)
                .setOnPageListener(this)
                .build(getActivity(),new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) { // 正常(默认)
        mAdapter = new HWAdapter(getActivity(),datas);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                // 跳转商品详情
                Wares wares = mAdapter.getData(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Const.WARE,wares);
                startActivity(intent);
            }
        });
        rvContent.setAdapter(mAdapter);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContent.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) { // 下拉刷新
        mAdapter.refreshData(datas);
        // 滚动到初始位置
        rvContent.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) { // 上拉加载更多
        mAdapter.addData(mAdapter.getDatas().size(), datas);
        // 滚动到最后
        rvContent.scrollToPosition(mAdapter.getDatas().size());
    }
}
