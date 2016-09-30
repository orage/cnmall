package com.oranges.cnmall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oranges.cnmall.R;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.widget.ClearEditText;
import com.oranges.cnmall.widget.MyToolBar;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

/**
 * 注册(1) RegisterActivity
 * Created by oranges on 2016/9/25.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.tb_register_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.tv_country)
    private TextView tvCountry;
    @ViewInject(R.id.tv_country_code)
    private TextView tvCountryCode;
    @ViewInject(R.id.cet_phone)
    private ClearEditText cetPhone;
    @ViewInject(R.id.cet_pwd)
    private ClearEditText cetPwd;

    private SpotsDialog dialog;
    private SMSEvenHandler evenHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    // 初始化
    private void init() {
        initToolBar();
        SMSSDK.initSDK(this, Const.SMS_APP_KEY, Const.SMS_APP_SECRET);
        evenHandler = new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);
    }

    // 初始化ToolBar
    private void initToolBar() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
    }

    // 获取输入信息
    private void getCode() {
        String phone = cetPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = tvCountryCode.getText().toString().trim();
        // 验证
        checkPhoneNum(phone, code);
        //not 86   +86
        SMSSDK.getVerificationCode(code, phone);
    }

    // 验证手机号格式
    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+"))
            code = code.substring(1);
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        if (code == "86") {
            if (phone.length() != 11) {
                Toast.makeText(this, "手机号码长度不对", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            Toast.makeText(this, "您输入的手机号码格式不正确", Toast.LENGTH_LONG).show();
            return;
        }
    }

    // 国家列表
    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
            Log.d(TAG, "code=" + code + "rule=" + rule);
        }
    }

    // 请求验证码后跳转到验证码填写页面
    private void afterVerificationCodeRequested(boolean smart) {
        String phone = cetPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = tvCountryCode.getText().toString().trim();
        String pwd = cetPwd.getText().toString().trim();
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        Intent intent = new Intent(this, Register2Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        intent.putExtra("countryCode", code);
        startActivity(intent);
    }

    // 获取国家区号
    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc))
            country = SMSSDK.getCountryByMCC(mcc);
        if (country == null)
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        return country;
    }

    // 获取MCC
    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }
        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHandler);
    }

    class SMSEvenHandler extends EventHandler {

        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            // 拿到国家代码(这里固定中国 所以不需要做任何操作)
                        }
                    } else {
                        // 根据服务器返回的网络错误，给Toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }
}
