package patel.jay.jaijalaram.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.CartItemHolder;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.Fragments.CartFragment;
import patel.jay.jaijalaram.ModelClass.Items;
import patel.jay.jaijalaram.ModelClass.OrderItem;
import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 24-Feb-18.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<CartItemHolder> {

    private ArrayList<OrderItem> arrayList;
    private Activity activity;
    private boolean isDisplay;

    public OrderItemAdapter(Activity activity, ArrayList<OrderItem> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        isDisplay = false;
    }

    public OrderItemAdapter(Activity activity, ArrayList<OrderItem> arrayList, boolean isDisplay) {
        this(activity, arrayList);
        this.isDisplay = isDisplay;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_cart, parent, false);
        return new CartItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CartItemHolder holder, int position) {
        final OrderItem ordItem = arrayList.get(position);
        final Items items = Items.getItem(activity, ordItem.getiId());
        try {
            if (isDisplay) {
                setHide(holder, View.VISIBLE);
                holder.tvDsc.setVisibility(View.GONE);
            } else {
                holder.etDsc.setVisibility(View.GONE);
                setHide(holder, View.GONE);
            }

            holder.tvName.setText(items.getName());
            holder.tvQty.setText(ordItem.getQty() + "");
            holder.etDsc.setText(ordItem.getDsc());
            holder.tvDsc.setText(ordItem.getDsc());
            holder.tvPrice.setText(ordItem.getPrice() + " ₹");

            holder.sdvImage.setImageURI(Uri.parse(items.getImgSrc()));

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }

        class ClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                try {
                    int qty = ordItem.getQty();
                    switch (view.getId()) {
                        case R.id.btnAdd:
                            if (qty < 9) {
                                qty++;

                            }
                            break;

                        case R.id.btnMin:
                            if (qty > 1) {
                                qty--;
                            }
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
                    MyConst.toast(activity, e.getMessage());
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