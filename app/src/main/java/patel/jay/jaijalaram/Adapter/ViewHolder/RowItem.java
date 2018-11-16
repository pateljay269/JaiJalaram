package patel.jay.jaijalaram.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 17-Feb-18.
 */

public class RowItem extends RecyclerView.ViewHolder {

    public TextView tvName;
    public LinearLayout cardLayout;
    public SimpleDraweeView sdvImage;

    public RowItem(View itemView) {
        super(itemView);

        sdvImage = itemView.findViewById(R.id.sdvImage);
        cardLayout = itemView.findViewById(R.id.cardLayout);
        tvName = itemView.findViewById(R.id.tvName);
    }
}
