package patel.jay.jaijalaram.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.RowItem;
import patel.jay.jaijalaram.Constants.Animations;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Panel.ShowItem.ItemDscActivity;
import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 14-Feb-18.
 */

public class ItemAdapter extends RecyclerView.Adapter<RowItem> {

    private ArrayList<Items> arrayList;
    private Activity activity;
    private boolean isSearch;

    public ItemAdapter(Activity activity, ArrayList<Items> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        isSearch = false;
    }

    public ItemAdapter(Activity activity, ArrayList<Items> arrayList, boolean isSearch) {
        this(activity, arrayList);
        this.isSearch = isSearch;
    }

    @Override
    public RowItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new RowItem(view);
    }

    @Override
    public void onBindViewHolder(RowItem holder, int position) {
        final Items item = arrayList.get(position);
        holder.tvName.setText(item.getName());

        try {
            holder.sdvImage.setImageURI(item.getImgSrc());

            if (!isSearch) {
                Animations.Alpha(holder.sdvImage, 1500);
                Animations.MultipleAnim(holder.tvName, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }

        class ListnerClass implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                ItemDscActivity.ITEMS = item;
                activity.startActivity(new Intent(activity, ItemDscActivity.class));
            }
        }

        ListnerClass listnerClass = new ListnerClass();

        holder.sdvImage.setOnClickListener(listnerClass);
        holder.tvName.setOnClickListener(listnerClass);
        holder.cardLayout.setOnClickListener(listnerClass);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}