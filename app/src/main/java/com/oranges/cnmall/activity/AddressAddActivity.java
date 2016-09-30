package com.oranges.cnmall.activity;

import android.content.res.AssetManager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.oranges.cnmall.R;
import com.oranges.cnmall.activity.base.BaseActivity;
import com.oranges.cnmall.app.MyApplication;
import com.oranges.cnmall.bean.Address;
import com.oranges.cnmall.bean.city.CityModel;
import com.oranges.cnmall.bean.city.DistrictModel;
import com.oranges.cnmall.bean.city.ProvinceModel;
import com.oranges.cnmall.consts.Const;
import com.oranges.cnmall.http.OkHttpHelper;
import com.oranges.cnmall.http.SpotsCallback;
import com.oranges.cnmall.msg.base.BaseRespMsg;
import com.oranges.cnmall.utils.XmlParserHandlerUtil;
import com.oranges.cnmall.widget.ClearEditText;
import com.oranges.cnmall.widget.MyToolBar;
import com.squareup.okhttp.Response;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


@ContentView(R.layout.activity_address_add)
public class AddressAddActivity extends BaseActivity {

    private static final int REQUEST_ADD = 0;
    private static final int REQUEST_MODIFY = 1;

    private int defaultStats = REQUEST_ADD;

    private OptionsPickerView mCityPikerView;

    @ViewInject(R.id.tb_aa_toolbar)
    private MyToolBar toolBar;
    @ViewInject(R.id.cet_consignee)
    private ClearEditText cetConsignee;
    @ViewInject(R.id.cet_phone)
    private ClearEditText cetPhone;
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    @ViewInject(R.id.cet_address)
    private ClearEditText cetAddress;

    private Address address;

    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public void init() {
        address = (Address) getIntent().getSerializableExtra("address");
        if (null != address)
            defaultStats = REQUEST_MODIFY;
        initToolbar(defaultStats);
        initData(defaultStats, address);
        initProvinceDatas();
        initOptionsPickerView();
    }

    // 初始化数据
    private void initData(int stats, Address address) {
        // 修改时才初始化相应数据
        if (stats == REQUEST_MODIFY) {
            cetConsignee.setText(address.getConsignee().trim());
            cetPhone.setText(address.getPhone().trim());
            tvAddress.setText(address.getAddr().substring(0, address.getAddr().indexOf(":")));
            cetAddress.setText(address.getAddr().substring(address.getAddr().indexOf(":") + 1, address.getAddr().length()));
        }
    }

    // 初始化Toolbar
    private void initToolbar(final int stats) {
        if (stats == REQUEST_MODIFY)
            toolBar.setTitle("修改收货地址");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stats == REQUEST_ADD)
                    createAddress();
                else
                    modifyAddress();
            }
        });

    }

    // 初始化城市选择控件
    private void initOptionsPickerView() {
        mCityPikerView = new OptionsPickerView(this);
        mCityPikerView.setPicker((ArrayList) mProvinces, mCities, mDistricts, true);
        mCityPikerView.setTitle("选择城市");
        mCityPikerView.setCyclic(false, false, false);
        mCityPikerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String address = mProvinces.get(options1).getName() + "  "
                        + mCities.get(options1).get(option2) + "  "
                        + mDistricts.get(options1).get(option2).get(options3);
                tvAddress.setText(address);
            }
        });
    }

    // 初始化城市数据
    protected void initProvinceDatas() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandlerUtil handler = new XmlParserHandlerUtil();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (mProvinces != null) {
            for (ProvinceModel p : mProvinces) {
                List<CityModel> cities = p.getCityList();
                ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List
                for (CityModel c : cities) {
                    cityStrs.add(c.getName()); // 把城市名称放入 cityStrs
                    ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List
                    List<DistrictModel> districts = c.getDistrictList();
                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());
                    for (DistrictModel d : districts)
                        districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                    dts.add(districtStrs);
                    mDistricts.add(dts);
                }
                mCities.add(cityStrs); // 组装城市数据
            }
        }
    }

    @Event(value = R.id.ll_city_picker, type = View.OnClickListener.class)
    private void showCityPickerView(View view) {
        mCityPikerView.show();
    }

    // 新建收货地址
    public void createAddress() {
        String consignee = cetConsignee.getText().toString();
        String phone = cetPhone.getText().toString();
        String address = tvAddress.getText().toString() + ":" + cetAddress.getText().toString();
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MyApplication.getInstance().getUser().getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", address);
        params.put("zip_code", "000000");
        httpHelper.post(Const.API.ADDRESS_CREATE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 修改收货地址
    public void modifyAddress() {
        String consignee = cetConsignee.getText().toString();
        String phone = cetPhone.getText().toString();
        String addr = tvAddress.getText().toString() + cetAddress.getText().toString();
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", address.getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", addr);
        params.put("zip_code", "000000");
        params.put("is_default", address.getIsDefault());
        httpHelper.post(Const.API.ADDRESS_UPDATE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
