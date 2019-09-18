package patel.jay.jaijalaram.Panel.ShowItem;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ItemAdapter;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.Models.Items.allItems;

/**
 * Created by Jay on 14-Feb-18.
 */

public class CatItems {

    private static ArrayList<Items> items, tempItems;
    private static ItemAdapter adapter;

    public static void diaplayItems(final Activity activity, final int catId) {
        try {
            final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(R.layout.floating_rv_layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.trans)));
//            dialog.setTitle("Select Item:");
            dialog.show();

            final EditText etFind = dialog.findViewById(R.id.etFind);
            etFind.setVisibility(View.VISIBLE);

            final RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));

            FloatingActionButton fab = dialog.findViewById(R.id.fab);
            fab.setImageResource(R.drawable.logout);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            items = allItems(activity, catId);
            tempItems = items;

            adapter = new ItemAdapter(activity, items);
            recyclerView.setAdapter(adapter);

            //region TextWatcher
            etFind.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //region TextChangedArrayList
                    try {
                        String cName = etFind.getText().toString();
                        if (cName.isEmpty()) {
                            items = tempItems;
                        } else {
                            //region Search
                            items = new ArrayList<>();
                            for (Items items : tempItems) {
                                if (items.getName().contains(cName)) {
                                    CatItems.items.add(items);
                                }
                            }
                            //endregion
                        }
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));
                        adapter = new ItemAdapter(activity, items, true);
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //endregion
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
            });
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }
}