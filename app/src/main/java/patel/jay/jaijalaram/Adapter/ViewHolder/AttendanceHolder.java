package patel.jay.jaijalaram.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import patel.jay.jaijalaram.R;

/**
 * Created by Jack on 02-Apr-18.
 */

public class AttendanceHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public RadioButton rdbP, rdbL, rdbA;

    public AttendanceHolder(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        rdbA = itemView.findViewById(R.id.rdbAbsent);
        rdbP = itemView.findViewById(R.id.rdbPresent);
        rdbL = itemView.findViewById(R.id.rdbLeave);

    }
}
