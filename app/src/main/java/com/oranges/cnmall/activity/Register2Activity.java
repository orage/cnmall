package com.oranges.cnmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oranges.cnmall.MainActivity;
import com.oranges.cnmall.R;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.User;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.msg.LoginRespMsg;
import com.oranges.cnmall.utils.CountTimeViewUtil;
import com.oranges.cnmall.utils.DESUtil;
import com.oranges.cnmall.widget.ClearEditText;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

/**
 * 注册(2) Register2Activity
 * Created by oranges on 2016/9/25.
 */
@ContentView(R.layout.activity_register2)
public class Register2Activity extends AppCompatActivity {

    @ViewInject(R.id.tb_register_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.tv_tip)
    private TextView tvTip;
    @ViewInject(R.id.btn_send_again)
    private Button btnResend;
    @ViewInject(R.id.cet_code)
    private ClearEditText cetCode;

    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimeViewUtil countTimerViewUtil;
    private SpotsDialog dialog;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
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
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");
        String formatPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.send_mobile_detail) + formatPhone;
        tvTip.setText(Html.fromHtml(text));
        // 显示短信倒计时
        countTimerViewUtil = new CountTimeViewUtil(btnResend);
        countTimerViewUtil.start();
        // 初始化并注册SmsSDK
        SMSSDK.initSDK(this, Const.SMS_APP_KEY, Const.SMS_APP_SECRET);
        evenHandler = new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);
        // 友好提示
        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this, "正在校验验证码");
    }

    // 初始化Toolbar
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
                submitCode();
            }
        });
    }

    @Event(value = R.id.btn_send_again ,type = View.OnClickListener.class)
    public void sendCodeAgain(View view){
        SMSSDK.getVerificationCode("+"+countryCode, phone);
        countTimerViewUtil = new CountTimeViewUtil(btnResend,R.string.resend_identify_code);
        countTimerViewUtil.start();
        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    // 注册
    private void doRegister() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Const.DES_KEY, pwd));

        httpHelper.post(Const.API.REGISTER, params, new SpotsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    Toast.makeText(Register2Activity.this, "注册失败:" + userLoginRespMsg.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                startActivity(new Intent(Register2Activity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }

    // 检验验证码
    private void submitCode() {
        String vCode = cetCode.getText().toString().trim();
        if (TextUtils.isEmpty(vCode)) {
            Toast.makeText(this, R.string.write_identify_code, Toast.LENGTH_LONG).show();
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }

    // 分割电话号码
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
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
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            doRegister();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des))
                                return;
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }


}
