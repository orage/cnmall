package com.oranges.cnmall.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Favorites;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.provider.CartProvider;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * Created by oranges on 2016/9/24.
 */
@ContentView(R.layout.activity_ware_detail)
public class WareDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tb_detail_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.wv_content)
    private WebView wvContent;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private WebAppInterface webAppInterface;
    private Wares wares;
    private CartProvider cartProvider;
    private SpotsDialog dialog;

    // 初始化
    @Override
    public void init() {
        Serializable serializable = getIntent().getSerializableExtra(Const.WARE);
        if (null == serializable)
            finish();
        wares = (Wares) serializable;
        cartProvider = new CartProvider(this);
        dialog = new SpotsDialog(this, "loading...");
        dialog.show();
        initToolBar();
        initWebView();
    }

    // 初始化ToolBar
    private void initToolBar() {
        toolBar.setRightButtonText("分享");
        toolBar.setRightButtonOnClickListener(this);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 初始化WebView
    private void initWebView() {
        WebSettings settings = wvContent.getSettings();
        // Native与Web交互所必须
        settings.setJavaScriptEnabled(true);
        // 默认true 加载不了html中的图片
        settings.setBlockNetworkImage(false);
        // 允许缓存
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath("/data/data/" + getPackageName() + "/cache");
        wvContent.loadUrl(Const.API.WARES_DETAIL);
        webAppInterface = new WebAppInterface(this);
        // Web添加接口
        wvContent.addJavascriptInterface(webAppInterface, "appInterface");
        wvContent.setWebViewClient(new WebC());
    }

    // 分享
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(wares.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(wares.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(wares.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    // 添加收藏(废弃 暂时没有使用)
    private void addToFavorite() {
        User user = MyApplication.getInstance().getUser();
        if (null == user) {
            startActivity(new Intent(this, LoginActivity.class), true);
        }
        Long userId = MyApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("ware_id", wares.getId());
        httpHelper.post(Const.API.FAVORITE_CREATE, params, new SpotsCallback<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                Toast.makeText(WareDetailActivity.this, "已添加到收藏夹", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        showShare();
    }

    // 监听Web界面状态
    class WebC extends WebViewClient {
        // 页面加载完成时
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (null != dialog && dialog.isShowing())
                dialog.dismiss();
            webAppInterface.showDetail();
        }
    }

    // Native与Web交互接口
    class WebAppInterface {

        Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        // 显示商品详情
        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wvContent.loadUrl("javascript:showDetail(" + wares.getId() + ")");
                }
            });
        }

        // 立即购买
        @JavascriptInterface
        public void buy(long id) {
            ShoppingCart cart = cartProvider.convertData(wares);
            Intent intent = new Intent(WareDetailActivity.this, CreateOrderActivity.class);
            intent.putExtra("cart",cart);
            startActivity(intent, true);
            //  添加收藏
            //  addToFavorite();
            //  Toast.makeText(context, "添加收藏成功", Toast.LENGTH_LONG).show();

        }

        // 加入购物车
        @JavascriptInterface
        public void addToCart(long id) {
            cartProvider.put(wares);
            Toast.makeText(context, "加入购物车成功", Toast.LENGTH_LONG).show();
        }

    }
}
