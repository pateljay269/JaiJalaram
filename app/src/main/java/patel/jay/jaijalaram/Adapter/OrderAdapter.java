package patel.jay.jaijalaram.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import patel.jay.jaijalaram.Activity.OrderItemsActivity;
import patel.jay.jaijalaram.Adapter.ViewHolder.RowOrder;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.TimeConvert;
import patel.jay.jaijalaram.ModelClass.Items;
import patel.jay.jaijalaram.ModelClass.Order;
import patel.jay.jaijalaram.R;

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

            TimeConvert tc = TimeConvert.timeMiliesConvert(order.getCurrentTime());
            holder.tvName.setText(tc.getDD_MMM_YY() + "\n" +
                    "" + tc.getHH_Min_AP());
            holder.tvPrice.setText(order.getPrice() + " ₹");
            Items items = Items.getItem(activity, order.getiIdFirst());
            holder.sdvImage.setImageURI(Uri.parse(items.getImgSrc()));

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }

        class ClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {
//                CatItems.OrderItems(activity, order.getoId());
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
