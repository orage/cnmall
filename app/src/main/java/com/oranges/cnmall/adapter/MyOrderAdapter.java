package com.oranges.cnmall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oranges.cnmall.R;
import com.oranges.cnmall.adapter.base.BaseViewHolder;
import com.oranges.cnmall.adapter.base.SimpleAdapter;
import com.oranges.cnmall.bean.Order;
import com.oranges.cnmall.bean.OrderItem;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;


/**
 * 我的订单adapter(实现类似微信朋友圈的图片显示效果) MyOrderAdapter
 * Created by oranges on 2016/9/27.
 */
public class MyOrderAdapter extends SimpleAdapter<Order> {

    private Context context;

    public MyOrderAdapter(Context context, List<Order> datas) {
        super(context, R.layout.template_my_orders, datas);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Order item) {

        viewHolder.getTextView(R.id.tv_order_num).setText("订单号：" + item.getOrderNum());
        viewHolder.getTextView(R.id.tv_order_money).setText("实付金额： ￥：" + item.getAmount());
        TextView txtStatus = viewHolder.getTextView(R.id.tv_status);
        switch (item.getStatus()) {
            case Order.STATUS_SUCCESS:
                txtStatus.setText("成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;
            case Order.STATUS_PAY_FAIL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;
            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("等待支付");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;
        }
        NineGridlayout nineGridlayout = (NineGridlayout) viewHolder.getView(R.id.ngl_layout);
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultWidth(50);
        nineGridlayout.setDefaultHeight(50);
        nineGridlayout.setAdapter(new OrderItemAdapter(context, item.getItems()));
    }


    class OrderItemAdapter extends NineGridAdapter {
        private List<OrderItem> items;
        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int position) {

            OrderItem item = getItem(position);

            return item.getWares().getImgUrl();

        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {

            OrderItem item = getItem(position);
            return item == null ? 0 : item.getId();
        }

        @Override
        public View getView(int i, View view) {

            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }

    }
}
