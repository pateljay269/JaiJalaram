package patel.jay.jaijalaram.Admin.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ConstClass.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.ModelClass.Items.allItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment implements ServerCall.OnAsyncResponse {

    RecyclerView recyclerView;
    ItemAdAdapter adapter;
    View view;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_rv, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            recyclerView.setAdapter(null);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));
        String url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
        new ServerCall(ItemsFragment.this, getActivity(), url, new HashMap<String, String>(), MyConst.SELECT)
                .execute();
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case MyConst.SELECT:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(getActivity(), PrefConst.ITEM_S, response);
                        adapter = new ItemAdAdapter(getActivity(), allItems(getActivity()));
                        recyclerView.setAdapter(adapter);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(getActivity(), e.getMessage());
        }
    }
}