package patel.jay.jaijalaram.Panel.Admin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import patel.jay.jaijalaram.Adapter.CatAdAdapter;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.Models.Categorys.allCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    CatAdAdapter adapter = null;
    View view;

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
        adapter = new CatAdAdapter(getActivity(), allCategory(getActivity()));
        recyclerView.setAdapter(adapter);

    }

}