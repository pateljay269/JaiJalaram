package patel.jay.jaijalaram.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.RowItem;
import patel.jay.jaijalaram.Constants.Animations;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Models.Categorys;
import patel.jay.jaijalaram.Panel.ShowItem.CatItems;
import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 13-Feb-18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RowItem> {

    private ArrayList<Categorys> arrayList;
    private Activity activity;

    public CategoryAdapter(Activity activity, ArrayList<Categorys> arrayList) {
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
        final Categorys category = arrayList.get(position);
        holder.tvName.setText(category.getName());

        try {
            holder.sdvImage.setImageURI(Uri.parse(category.getImgSrc()));
            Animations.Alpha(holder.sdvImage, 1000);
            Animations.Scale(holder.tvName, 1000);

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        class ListnerClass implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                CatItems.diaplayItems(activity, category.getcId());
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