package patel.jay.jaijalaram.Panel.Customer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.OrderAdapter;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Models.Order.allOrder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements ServerCall.OnAsyncResponse {

    RecyclerView recyclerView;
    OrderAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
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

        if (isNetAvail(getActivity())) {
            String url = ServerCall.BASE_URL + ServerCall.ORDER + "allCustOrd";
            HashMap<String, String> hm = new HashMap<>();
            hm.put(MYSQL.Customer.CUSTID, SignActivity.customer.getCustId() + "");
            new ServerCall(OrdersFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();
        } else {
            setRv();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.SELECT:
                if (response.length() > 0) {
                    MyConst.putIntoPref(getActivity(), PrefConst.ORDER_S, response);
                    setRv();
                }
                break;
        }
    }

    private void setRv() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<Order> allOrder = allOrder(getActivity(), false);
        adapter = new OrderAdapter(getActivity(), allOrder);
        recyclerView.setAdapter(adapter);
    }
}
