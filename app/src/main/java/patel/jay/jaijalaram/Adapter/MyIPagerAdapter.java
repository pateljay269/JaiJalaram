package patel.jay.jaijalaram.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Panel.ShowItem.ItemDscActivity;
import patel.jay.jaijalaram.R;

public class MyIPagerAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<Items> itemsList;

    public MyIPagerAdapter(Activity activity, ArrayList<Items> itemsList) {
        this.activity = activity;
        this.itemsList = itemsList;
        Fresco.initialize(activity);
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        Fresco.initialize(activity);
        View itemView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.sdv_image_linear, container, false);

        final Items item = itemsList.get(position);
        SimpleDraweeView sdv = itemView.findViewById(R.id.sdvImage);
        sdv.setImageURI(item.getImgSrc());

        container.addView(itemView);

        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDscActivity.ITEMS = item;
                activity.startActivity(new Intent(activity, ItemDscActivity.class));
            }
        });

        return itemView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return itemsList.get(position).getName();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}