package patel.jay.jaijalaram.Panel.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Worker;
import patel.jay.jaijalaram.Panel.Admin.Activity.WorkerProfileActivity;
import patel.jay.jaijalaram.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkersListFragment extends Fragment implements ServerCall.OnAsyncResponse, AdapterView.OnItemClickListener {

    ArrayList<String> listNames;
    ArrayList<Worker> workers;

    ListView listView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workers_list, container, false);

        listView = view.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String url = ServerCall.BASE_URL + ServerCall.AALL + "allWorker";
        new ServerCall(WorkersListFragment.this, getActivity(), url, null, MyConst.SELECT)
                .execute();

    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.SELECT:
                MyConst.putIntoPref(getActivity(), PrefConst.WORKER_S, response);

                workers = Worker.allWorker(getActivity());
                listNames = Worker.allWorkerStr(getActivity());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listNames);

                listView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        WorkerProfileActivity.isUpdate = true;
        WorkerProfileActivity.worker = workers.get(pos);
        Intent intent = new Intent(getContext(), WorkerProfileActivity.class);
        startActivity(intent);
    }
}
