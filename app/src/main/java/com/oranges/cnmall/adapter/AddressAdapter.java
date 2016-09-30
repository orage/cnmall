package com.oranges.cnmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.AddressAddActivity;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.Address;
import com.oranges.cnmall.consts.Const;

import java.util.List;

import static com.oranges.cnmall.consts.Const.REQUEST_DO_ADDRESS_MODIFY;

/**
 * 收货地址adapter AddressAdapter
 * Created by oranges on 2016/9/27.
 */
public class AddressAdapter extends SimpleAdapter<Address> {


    private AddressListener listener;
    private Context context;

    public AddressAdapter(Context context, List<Address> datas, AddressListener listener) {
        super(context, R.layout.template_address, datas);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Address item) {
        viewHolder.getTextView(R.id.tv_name).setText(item.getConsignee());
        viewHolder.getTextView(R.id.tv_phone).setText(replacePhoneNum(item.getPhone()));
        viewHolder.getTextView(R.id.tv_address).setText(item.getAddr());
        final CheckBox checkBox = viewHolder.getCheckBox(R.id.cb_is_default);
        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);
        // 默认地址不能点击
        if (isDefault) {
            checkBox.setText("默认地址");
        } else {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && listener != null) {
                        item.setIsDefault(true);
                        listener.setDefault(item);
                    }
                }
            });
        }
        // 编辑
        viewHolder.getTextView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.modifyAddress(item);
            }
        });
        // 删除
        viewHolder.getTextView(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteAddress(item);
            }
        });
    }

    // 加密手机号码
    public String replacePhoneNum(String phone) {
        return phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
    }

    // 收货地址监听接口
    public interface AddressListener {
        public void setDefault(Address address);
        public void modifyAddress(Address address);
        public void deleteAddress(Address address);
    }
}
