package patel.jay.jaijalaram.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.CustHolder;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Models.Customer;
import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 25-Feb-18.
 */

public class CustAdapter extends RecyclerView.Adapter<CustHolder> {

    private Activity activity;
    private ArrayList<Customer> arrayList;

    public CustAdapter(Activity activity, ArrayList<Customer> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public CustHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.row_customer, parent, false);
        return new CustHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustHolder holder, int position) {
        final Customer customer = arrayList.get(position);
        try {

            switch (customer.getType()) {
                case "A":
                    holder.tvName.setTextColor(Color.BLACK);
                    break;

                case "M":
                    holder.tvName.setTextColor(Color.BLUE);
                    break;
            }
            holder.tvName.setText(customer.getFname() + " " + customer.getLname());
            holder.tvMobile.setText(customer.getMobile() + "");
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }

        class ClickListner implements View.OnClickListener {
            @Override
            public void onClick(View view) {

            }
        }

        ClickListner listner = new ClickListner();
        holder.tvName.setOnClickListener(listner);
        holder.tvMobile.setOnClickListener(listner);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
