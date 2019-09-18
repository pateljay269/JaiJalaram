package patel.jay.jaijalaram.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.RowOrder;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.Panel.ShowItem.OrderItemsActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Constants.TimeConvert.timeMiliesConvert;
import static patel.jay.jaijalaram.Login.SignActivity.customer;
import static patel.jay.jaijalaram.Models.Customer.custName;
import static patel.jay.jaijalaram.Models.Items.getItem;

/**
 * Created by Jay on 24-Feb-18.
 */

public class OrderAdapter extends RecyclerView.Adapter<RowOrder> {

    private ArrayList<Order> arrayList;
    private Activity activity;

    public OrderAdapter(Activity activity, ArrayList<Order> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public RowOrder onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(activity);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new RowOrder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RowOrder holder, int position) {
        final Order order = arrayList.get(position);
        try {

            TimeConvert tc = timeMiliesConvert(order.getCurrentTime());
            holder.tvName.setText(tc.getDD_MMM_YY() + "\n" +
                    "" + tc.getHH_Min_AP());
            holder.tvPrice.setText(order.getPrice() + " ₹");

            Items items = getItem(activity, order.getiIdFirst());
            holder.sdvImage.setImageURI(items.getImgSrc());

            if (!customer.getType().equals("U")) {
                holder.tvCust.setVisibility(View.VISIBLE);
                holder.tvCust.setText(custName(activity, order.getCustId()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }

        class ClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                OrderItemsActivity.OITEM = order;
                activity.startActivity(new Intent(activity, OrderItemsActivity.class));
            }
        }

        ClickListner clickListner = new ClickListner();

        holder.sdvImage.setOnClickListener(clickListner);
        holder.cardLayout.setOnClickListener(clickListner);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
