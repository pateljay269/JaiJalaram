package patel.jay.jaijalaram.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import patel.jay.jaijalaram.R;

/**
 * Created by Jay on 25-Feb-18.
 */

public class CustHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvMobile;

    public CustHolder(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        tvMobile = itemView.findViewById(R.id.tvMobile);
    }
}
