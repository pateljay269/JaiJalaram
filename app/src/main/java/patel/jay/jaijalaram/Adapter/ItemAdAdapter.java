package patel.jay.jaijalaram.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.RowItem;
import patel.jay.jaijalaram.Admin.Activity.UpdateItemActivity;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ModelClass.Items;
import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 26-Jan-18.
 */

public class ItemAdAdapter extends RecyclerView.Adapter<RowItem> {

    public static Items ITEM_ADMIN;

    private ArrayList<Items> arrayList;
    private Activity activity;

    public ItemAdAdapter(Activity activity, ArrayList<Items> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public RowItem onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(activity);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new RowItem(view);
    }

    @Override
    public void onBindViewHolder(RowItem holder, int position) {
        final Items item = arrayList.get(position);
        holder.tvName.setText(item.getName());

        try {
            Uri imageUri = Uri.parse(item.getImgSrc());
            holder.sdvImage.setImageURI(imageUri);

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        class ListnerClass implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                ITEM_ADMIN = item;
                CatAdAdapter.CATEGORY_ADMIN = null;
                activity.startActivity(new Intent(activity, UpdateItemActivity.class));
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