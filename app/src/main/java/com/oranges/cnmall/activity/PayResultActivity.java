package com.oranges.cnmall.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_pay_result)
public class PayResultActivity extends BaseActivity {

    @ViewInject(R.id.tb_pr_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.iv_result)
    private ImageView ivResult;
    @ViewInject(R.id.tv_result)
    private TextView tvResult;

    private int status = Const.PAY_RESULT.RESULT_OTHERS;

    @Override
    public void init() {
        // 得到对应的status
        status = getIntent().getIntExtra("status", Const.PAY_RESULT.RESULT_OTHERS);
        initToolbar();
        initData(status);
    }

    // 初始化Toolbar
    private void initToolbar() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 初始化结果显示
    private void initData(int status) {
        switch (status) {
            case Const.PAY_RESULT.RESULT_SUCCESS:
                ivResult.setImageResource(R.drawable.icon_success_128);
                tvResult.setText("支付成功");
                break;
            case Const.PAY_RESULT.RESULT_FAIL:
                ivResult.setImageResource(R.drawable.icon_cancel_128);
                tvResult.setText("支付失败");
                break;
            case Const.PAY_RESULT.RESULT_CANCEL:
                ivResult.setImageResource(R.drawable.icon_cancel_128);
                tvResult.setText("支付取消");
                break;
            case Const.PAY_RESULT.RESULT_OTHERS:
                ivResult.setImageResource(R.drawable.icon_cancel_128);
                tvResult.setText("支付发生未知错误");
                break;
        }
    }

    @Event(value = R.id.btn_back_home,type = View.OnClickListener.class)
    private void onClick(View view){
        // TODO: 2016/9/27  回首页 暂时这么写
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
