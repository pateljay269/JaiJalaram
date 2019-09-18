package patel.jay.jaijalaram.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 17-Feb-18.
 */

public class CartItemHolder extends RecyclerView.ViewHolder {

    public ImageButton btnAdd, btnMin, btnDelete;
    public TextView tvName, tvPrice, tvQty;
    public SimpleDraweeView sdvImage;
    public EditText etDsc;
    public LinearLayout cardLayout;

    public CartItemHolder(View itemView) {
        super(itemView);

        sdvImage = itemView.findViewById(R.id.sdvImage);

        cardLayout = itemView.findViewById(R.id.cardLayout);

        tvName = itemView.findViewById(R.id.tvName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvQty = itemView.findViewById(R.id.tvQty);

        etDsc = itemView.findViewById(R.id.etDsc);

        btnAdd = itemView.findViewById(R.id.btnAdd);
        btnMin = itemView.findViewById(R.id.btnMin);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
