package patel.jay.jaijalaram.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ViewHolder.AttendanceHolder;
import patel.jay.jaijalaram.R;

/**
 * Created by Jack on 02-Apr-18.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {

    private Activity activity;
    private ArrayList<String> arrayList, idList;
    private boolean isPresent;

    public static String[] attendList;
    public static String[] workerIds;

    public AttendanceAdapter(Activity activity, ArrayList<String> arrayList, ArrayList<String> idList) {
        if (arrayList.size() != idList.size()) {
            throw new IllegalArgumentException("List Are Different..");
        }

        attendList = new String[arrayList.size()];
        workerIds = new String[idList.size()];

        for (int i = 0; i < arrayList.size(); i++) {
            attendList[i] = "A";
            workerIds[i] = idList.get(i);
        }

        this.activity = activity;
        this.arrayList = arrayList;
        this.idList = idList;
        isPresent = false;
    }

    public AttendanceAdapter(Activity activity, ArrayList<String> arrayList, ArrayList<String> idList, boolean isPresent) {
        this(activity, arrayList, idList);
        this.isPresent = isPresent;
    }

    @Override
    public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.row_attaendance, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(final AttendanceHolder holder, int position) {
        String name = arrayList.get(position);

        holder.tvName.setText(name);

        if (isPresent) {
            attendList[position] = "P";
            holder.rdbP.setChecked(true);
        } else {
            attendList[position] = "A";
            holder.rdbA.setChecked(true);
        }

        class Listner implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                attendList[holder.getAdapterPosition()] = btn.getText().toString().charAt(0) + "";
            }
        }

        Listner listner = new Listner();

        holder.rdbP.setOnClickListener(listner);
        holder.rdbA.setOnClickListener(listner);
        holder.rdbL.setOnClickListener(listner);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
