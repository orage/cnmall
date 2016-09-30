package com.oranges.cnmall.provider;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.oranges.cnmall.bean.ShoppingCart;
import com.oranges.cnmall.bean.Wares;
import com.oranges.cnmall.utils.JSONUtil;
import com.oranges.cnmall.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车本地数据操作 CartProvider
 * Created by oranges on 2016/9/23.
 */
public class CartProvider {

    public static final String CART_JSON = "cart_json";
    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;

    public CartProvider(Context context) {
        datas = new SparseArray<>(10);
        mContext = context;
        list2Sparse();
    }

    // 添加
    public void put(ShoppingCart cart) {
        // 查询数据是否已存在
        ShoppingCart temp = datas.get(cart.getId().intValue());
        // 已存在的数据数量+=1
        if (null != temp)
            temp.setCount(temp.getCount() + 1);
        else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(), temp);
        commit();
    }

    // 添加
    public void put(Wares wares) {
        // 查询数据是否已存在
        ShoppingCart temp = convertData(wares);
        put(temp);
        commit();
    }

    // 提交数据到SharedPreferences
    public void commit() {
        List<ShoppingCart> carts = sparse2List();
        PreferencesUtil.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    // SparseArray转化为ArrayList<>
    private List<ShoppingCart> sparse2List() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(datas.valueAt(i));
        return list;
    }

    // ArrayList<>转化为SparseArray
    private void list2Sparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (null != carts && carts.size() > 0) {
            for (ShoppingCart cart : carts)
                datas.put(cart.getId().intValue(), cart);
        }
    }

    // 更新
    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    // 删除
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    // 删除多个
    public void delete(List<ShoppingCart> carts) {
        for (ShoppingCart cart : carts) {
            datas.delete(cart.getId().intValue());
        }
        commit();
    }


    // 获得所有购物车商品
    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    // 使用SharedPreferences读取本地数据
    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtil.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (null != json) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }

    // 转换数据格式
    public ShoppingCart convertData(Wares wares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setDescription(wares.getDescription());
        cart.setImgUrl(wares.getImgUrl());
        cart.setName(wares.getName());
        cart.setPrice(wares.getPrice());
        return cart;
    }
}
