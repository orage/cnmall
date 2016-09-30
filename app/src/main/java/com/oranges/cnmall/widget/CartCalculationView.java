package com.oranges.cnmall.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oranges.cnmall.R;

/**
 * 购物车加减控件 CartCalculationView
 * Created by oranges on 2016/9/23.
 */
public class CartCalculationView extends LinearLayout implements View.OnClickListener {


    public static final String TAG = "CardCalculationView";
    public static final int DEFAULT_MAX = 1000;
    private TextView mTvNum;
    private Button mBtnAdd;
    private Button mBtnSub;

    private LayoutInflater mInflater;
    private OnButtonClickListener onButtonClickListener;
    private int value;
    private int minValue;
    private int maxValue = DEFAULT_MAX;


    public CartCalculationView(Context context) {
        this(context, null);
    }

    public CartCalculationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartCalculationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();
        if (null != attrs) {
            // 加载自定义属性
            TintTypedArray array = TintTypedArray.obtainStyledAttributes(context, attrs,
                    R.styleable.cardCalculationView, defStyleAttr, 0);
            // 当前值(默认值) 最小值 最大值
            int val = array.getInt(R.styleable.cardCalculationView_value,0);
            int minVal = array.getInt(R.styleable.cardCalculationView_minValue,1);
            int maxVal = array.getInt(R.styleable.cardCalculationView_maxValue,DEFAULT_MAX);
            // 控件图片
            Drawable drawableBtnAdd = array.getDrawable(R.styleable.cardCalculationView_buttonAddBackground);
            Drawable drawableBtnSub = array.getDrawable(R.styleable.cardCalculationView_buttonSubBackground);
            Drawable drawableTvNum = array.getDrawable(R.styleable.cardCalculationView_textViewBackground);
            // 赋值
            setValue(val);
            setMinValue(minVal);
            setMaxValue(maxVal);
            setButtonAddBackground(drawableBtnAdd);
            setButtonSubBackground(drawableBtnSub);
            setTextViewBackground(drawableTvNum);
            array.recycle();
        }
    }

    // 初始化控件
    private void initView() {
        View view = mInflater.inflate(R.layout.widget_num_add_sub, this, true);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);
        mTvNum = (TextView) view.findViewById(R.id.tv_num);
        // 设置监听
        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    // -- start Getter and Setter
    public int getValue() {
        String val = mTvNum.getText().toString();
        if (null != val || !"".equals(val))
            this.value = Integer.parseInt(val);
        return value;
    }

    public void setValue(int value) {
        mTvNum.setText(value+"");
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setTextViewBackground(Drawable drawable){
        mTvNum.setBackgroundDrawable(drawable);
    }

    public void setTextViewBackground(int drawableId){
        setTextViewBackground(getResources().getDrawable(drawableId));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackground(Drawable background){
        this.mBtnAdd.setBackground(background);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackground(Drawable background){
        this.mBtnSub.setBackground(background);
    }
    // end --

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add: // 增加
                numAdd();
                if (null != onButtonClickListener) {
                    onButtonClickListener.onAddClick(v, value);
                }
                break;
            case R.id.btn_sub: // 减少
                numSub();
                if (null != onButtonClickListener) {
                    onButtonClickListener.onSubClick(v, value);
                }
                break;
        }
    }

    // 点击增加
    private void numAdd() {
        if (value < maxValue) {
            value ++;
        }
        mTvNum.setText(value + "");
    }

    // 点击减少
    private void numSub() {
        if (value > minValue) {
            value --;
        }
        mTvNum.setText(value + "");
    }

    public void setOnButtonClickListener(OnButtonClickListener mButtonClickListener) {
        this.onButtonClickListener = mButtonClickListener;
    }

    // 点击事件接口
    public interface OnButtonClickListener {
        void onAddClick(View view, int value);

        void onSubClick(View view, int value);
    }
}
