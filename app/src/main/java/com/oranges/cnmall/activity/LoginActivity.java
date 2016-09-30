package com.oranges.cnmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oranges.cnmall.R;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.msg.LoginRespMsg;
import com.oranges.cnmall.utils.DESUtil;
import com.oranges.cnmall.widget.ClearEditText;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录 LoginActivity
 * Created by oranges on 2016/9/25.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @ViewInject(R.id.tb_login_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.cet_phone)
    private ClearEditText cetPhone;
    @ViewInject(R.id.cet_pwd)
    private ClearEditText cetPwd;
    @ViewInject(R.id.btn_login)
    private Button btnLogin;
    @ViewInject(R.id.tv_toReg)
    private TextView tvToReg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    // 初始化
    private void init() {
        initToolBar();
    }

    // 初始化ToolBar
    private void initToolBar() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 登录
    @Event(value = R.id.btn_login, type = View.OnClickListener.class)
    private void login(View view) {
        String phone = cetPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        String pwd = cetPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Const.DES_KEY, pwd));
        httpHelper.post(Const.API.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) { // 登录成功
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                if (null == application.getIntent()) {
                    setResult(RESULT_OK);
                } else {
                    application.doTargetActivity(LoginActivity.this);
                }
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    // 去注册
    @Event(value = R.id.tv_toReg, type = View.OnClickListener.class)
    private void doRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
