package com.oranges.cnmall.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * okHttp自定义回调
 * Created by oranges on 2016/9/22.
 */
public abstract class BaseCallback<T> {

    public Type type;

    // 将T转成Type
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback() {
        type = getSuperclassTypeParameter(getClass());
    }

    // 请求之前执行
    public abstract void onRequestBefore(Request request);

    // 请求失败 发生不可逆转错误
    public abstract void onFailure(Request request, IOException e);

    // 请求成功(状态码大于200小于300调用此方法)
    public abstract void onSuccess(Response response,T t);

    // 请求错误(状态码400 403 404 500)
    public abstract void onError(Response response, int code, Exception e);

    // 请求结果返回(请求成功时调用)
    public abstract void onResponse(Response response);

    // 验证token失败(状态码401 402 403)
    public abstract void onTokenError(Response response, int code);

}
