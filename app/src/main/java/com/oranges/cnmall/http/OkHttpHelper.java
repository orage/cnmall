package com.oranges.cnmall.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.oranges.cnmall.app.MyApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 对okHttp作一个简单封装
 * Created by oranges on 2016/9/22.
 */
public class OkHttpHelper {

    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期

    private static OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;

    private OkHttpHelper() {
        // 实例化okHttpClient
        okHttpClient = new OkHttpClient();
        // 设置超时时间
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        // 实例化gson
        gson = new Gson();
        // 实例化handler
        handler = new Handler(Looper.getMainLooper());
    }

    // 使用单例
    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    // get请求
    public void get(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.GET);
        doRequest(request, callback);
    }

    // get请求
    public void get(String url, BaseCallback callback) {
        get(url, null, callback);
    }

    // post请求
    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callback);
    }


    // 执行request和回调
    public void doRequest(Request request, final BaseCallback callback) {
        // 请求之前执行
        callback.onRequestBefore(request);
        // 异步请求enqueue
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // 任何结果都需要调用下callback.onResponse()
                callback.onResponse(response);
                // 成功时
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.type == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = gson.fromJson(resultStr, callback.type);
                            callbackSuccess(callback, response, object);
                        } catch (JsonParseException e) { // json解析错误
                            callbackError(callback, response, e);
                        }
                    }
                } else if (response.code() == TOKEN_MISSING || response.code() == TOKEN_ERROR ||
                        response.code() == TOKEN_EXPIRE) {
                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }

    // 构建request
    private Request buildRequest(String url, Map<String, Object> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        } else {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    // HashMap转String(条件)
    private String buildUrlParams(String url, Map<String, Object> params) {
        if (params == null)
            params = new HashMap<>(1);
        String token = MyApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token))
            params.put("token", token);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        if (url.indexOf("?") > 0) { // 判断url是否已经有参数
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }
        return url;
    }

    // 构建表单数据(post提交的参数)
    private RequestBody buildFormData(Map<String, Object> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            // 循环map给builder添加参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            // 增加token
            String token = MyApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                builder.add("token", token);
        }
        return builder.build();
    }

    // callback回调方法(Success)
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    // callback回调方法(TokenError)
    private void callbackTokenError(final BaseCallback callback, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    // callback回调方法(Error)
    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    // callback回调方法(Failure)
    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    // 请求方式的枚举
    enum HttpMethodType {
        GET,
        POST
    }
}
