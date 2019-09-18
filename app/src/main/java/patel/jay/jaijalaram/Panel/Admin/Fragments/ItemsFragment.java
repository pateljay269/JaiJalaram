package patel.jay.jaijalaram.Panel.Admin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.Models.Categorys;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.Models.Categorys.allCatStr;
import static patel.jay.jaijalaram.Models.Categorys.allCategory;
import static patel.jay.jaijalaram.Models.Items.allItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    ItemAdAdapter adapter;
    Spinner spnCat;
    View view;
    private static int pos = 0;

    ArrayList<String> allCatStr;
    ArrayList<Categorys> allCat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_rv, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        spnCat = view.findViewById(R.id.spnAction);

        spnCat.setVisibility(View.VISIBLE);

        spnCat.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        allCat = allCategory(getActivity());
        allCatStr = allCatStr(getActivity());
        allCatStr.remove(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, allCatStr);
        spnCat.setAdapter(adapter);

        try {
            spnCat.setSelection(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapter != null) {
            recyclerView.setAdapter(null);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));

        int cid = allCat.get(position).getcId();
        adapter = new ItemAdAdapter(getActivity(), allItems(getActivity(), cid));
        recyclerView.setAdapter(adapter);

        pos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}