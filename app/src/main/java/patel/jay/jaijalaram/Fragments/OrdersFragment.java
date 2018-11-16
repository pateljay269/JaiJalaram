package patel.jay.jaijalaram.Fragments;

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
import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.ModelClass.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ModelClass.Order.allOrder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements ServerCall.OnAsyncResponse {

    private final int ORD = 1003;
    RecyclerView recyclerView;
    OrderAdapter adapter;
    View view;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        view = inflater.inflate(R.layout.content_rv, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String url2 = ServerCall.BASE_URL + ServerCall.ORDER + "allCustOrd";
        HashMap<String, String> hm = new HashMap<>();
        hm.put(MYSQL.Customer.CUSTID, SignActivity.customer.getCustId() + "");
        new ServerCall(OrdersFragment.this, getActivity(), url2, hm, ORD).execute();

        if (adapter != null) {
            recyclerView.setAdapter(null);
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case ORD:
                if (response.length() > 0) {
                    MyConst.putIntoPref(getActivity(), PrefConst.ORDER_S, response);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    ArrayList<Order> allOrder = allOrder(getActivity(), false);
                    adapter = new OrderAdapter(getActivity(), allOrder);
                    recyclerView.setAdapter(adapter);
                }
                break;
        }
    }
}
