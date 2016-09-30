package com.oranges.cnmall.consts;

/**
 * 存放常量类 Const
 * Created by oranges on 2016/9/22.
 */
public class Const {

    // 对称加密key
    public static final String DES_KEY = "Cniao5_123456";
    // 短信 App Key
    public static final String SMS_APP_KEY = "176ff5f8dc32a";
    // 短信 App Secret
    public static final String SMS_APP_SECRET = "c78e0a02a810951e3a05a5884a2ea3f4";

    public static final String USER_JSON = "user_json";

    public static final String TOKEN = "token";

    public static final String CAMPAIGN_ID = "campaign_id";

    public static final String WARE = "ware";

    public static final int REQUEST_DO_LOGIN = 0;

    public static final int REQUEST_DO_PAYMENT = 1;

    public static final int REQUEST_DO_ADDRESS_ADD = 2;

    public static final int REQUEST_DO_ADDRESS_MODIFY = 3;

    // API
    public static class API {
        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";
        // 轮播广告
        public static final String BANNER = BASE_URL + "banner/query";
        // 热门活动(首页)
        public static final String CAMPAIGN_HOME = BASE_URL + "campaign/recommend";
        // 热卖商品
        public static final String WARES_HOT = BASE_URL + "wares/hot";
        // 分类列表
        public static final String CATEGORY_LIST = BASE_URL + "category/list";
        // 分类下的商品
        public static final String WARES_LIST = BASE_URL + "wares/list";
        // 热门活动下的商品列表
        public static final String WARES_CAMPAIGN_LIST = BASE_URL + "wares/campaign/list";
        // 商品详情
        public static final String WARES_DETAIL = BASE_URL + "wares/detail.html";
        // 登录
        public static final String LOGIN = BASE_URL + "auth/login";
        // 注册
        public static final String REGISTER = BASE_URL + "auth/reg";
        // 结算
        public static final String USER_DETAIL = BASE_URL + "user/get?id=1";
        // 下单
        public static final String ORDER_CREATE = BASE_URL + "/order/create";
        // 下单完成
        public static final String ORDER_COMPLEPE = BASE_URL + "/order/complete";
        // 订单列表
        public static final String ORDER_LIST = BASE_URL + "order/list";
        // 获取地址
        public static final String ADDRESS_LIST = BASE_URL + "addr/list";
        // 新建地址
        public static final String ADDRESS_CREATE = BASE_URL + "addr/create";
        // 更新地址
        public static final String ADDRESS_UPDATE = BASE_URL + "addr/update";
        // 删除地址
        public static final String ADDRESS_DELETE = BASE_URL + "addr/del";
        // 收藏列表
        public static final String FAVORITE_LIST=BASE_URL +"favorite/list";
        // 添加收藏
        public static final String FAVORITE_CREATE=BASE_URL +"favorite/create";
    }

    // 支付
    public static class PAY {
        // 银联支付渠道
        public static final String CHANNEL_UPACP = "upacp";
        // 微信支付渠道
        public static final String CHANNEL_WECHAT = "wx";
        // 支付宝支付渠道
        public static final String CHANNEL_ALIPAY = "alipay";
        // 百度钱包支付渠道
        public static final String CHANNEL_BFB = "bfb";
        // 京东支付渠道
        public static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    }

    // 支付结果
    public static class PAY_RESULT {
        // 成功
        public static final String SUCCESS = "success";
        // 失败
        public static final String FAIL = "fail";
        // 取消
        public static final String CANCEL = "cancel";
        // 成功状态码
        public static final int RESULT_SUCCESS = 1;
        // 失败状态码
        public static final int RESULT_FAIL = -1;
        // 取消状态码
        public static final int RESULT_CANCEL = -2;
        // 其它状态码
        public static final int RESULT_OTHERS = 0;
    }
}
