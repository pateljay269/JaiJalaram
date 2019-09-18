package patel.jay.jaijalaram.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.ViewHolder.CartItemHolder;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Models.OrderItem;
import patel.jay.jaijalaram.Panel.Customer.Fragments.CartFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Panel.ShowItem.OrderItemsActivity.OITEM;

/**
 * Created by Jay on 24-Feb-18.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<CartItemHolder> {

    private ArrayList<OrderItem> arrayList;
    private Activity activity;
    private boolean isDispAdDe;

    public OrderItemAdapter(Activity activity, ArrayList<OrderItem> arrayList, boolean isDispAdDe) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.isDispAdDe = isDispAdDe;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(activity);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_cart, parent, false);
        return new CartItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CartItemHolder holder, int position) {
        final OrderItem ordItem = arrayList.get(position);
        final Items items = Items.getItem(activity, ordItem.getiId());
        try {
            if (isDispAdDe) {
                setHide(holder, View.VISIBLE);
            } else {
                holder.etDsc.setEnabled(false);
                setHide(holder, View.GONE);
            }

            holder.tvName.setText(items.getName());
            holder.tvQty.setText(ordItem.getQty() + "");
            holder.tvPrice.setText(ordItem.getPrice() + " ₹");

            if (!ordItem.getDsc().equals("")) {
                holder.etDsc.setText(ordItem.getDsc());
            } else {
                holder.etDsc.setVisibility(View.GONE);
            }
            holder.sdvImage.setImageURI(items.getImgSrc());

        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }

        class ClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                try {
                    int qty = ordItem.getQty();
                    switch (view.getId()) {
                        case R.id.btnAdd:
//                            if (qty < 9)
                            qty++;
                            break;

                        case R.id.btnMin:
                            if (qty > 1)
                                qty--;
                            break;
                    }
                    int price = qty * items.getPrice();
                    CartFragment.cartOrder.get(holder.getAdapterPosition()).setQty(qty);
                    CartFragment.cartOrder.get(holder.getAdapterPosition()).setPrice(price);

                    holder.tvQty.setText(qty + "");
                    holder.tvPrice.setText(price + " ₹");

                    CartFragment.setCartPrice();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(activity, e.getMessage());
                }
            }
        }

        ClickListner clickListner = new ClickListner();

        holder.btnAdd.setOnClickListener(clickListner);
        holder.btnMin.setOnClickListener(clickListner);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment.cartOrder.remove(holder.getAdapterPosition());
                CartFragment.cartItems.remove(holder.getAdapterPosition());
                CartFragment.setOrderItem(activity);
            }
        });

        class ItemClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                if (!isDispAdDe) {
                    if (!OITEM.isDeliver()) {
                        alertView();
                    }
                }
            }

            private void alertView() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Order Item Delete..???");

                builder.setTitle("Are You Sure??")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url1 = ServerCall.BASE_URL + ServerCall.ORDER + "delOitem";
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put(MYSQL.Order.OID, OITEM.getoId() + "");
                                hm.put(MYSQL.Order.IID, items.getiId() + "");
                                new ServerCall(activity, url1, hm, MyConst.DELETE).execute();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        ItemClickListner click = new ItemClickListner();

        holder.cardLayout.setOnClickListener(click);
        holder.etDsc.setOnClickListener(click);
        holder.sdvImage.setOnClickListener(click);
        holder.tvName.setOnClickListener(click);
        holder.tvPrice.setOnClickListener(click);
        holder.tvQty.setOnClickListener(click);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void setHide(CartItemHolder holder, int a) {
        holder.btnAdd.setVisibility(a);
        holder.btnDelete.setVisibility(a);
        holder.btnMin.setVisibility(a);
    }
}