package patel.jay.jaijalaram.Panel.Customer.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import patel.jay.jaijalaram.Adapter.OrderItemAdapter;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.Models.OrderItem;
import patel.jay.jaijalaram.Panel.Customer.CustDashActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.sDetail;
import static patel.jay.jaijalaram.Constants.MyConst.sItems;
import static patel.jay.jaijalaram.Constants.MyConst.sOrder;
import static patel.jay.jaijalaram.Models.Categorys.getCid;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    public static ArrayList<Items> cartItems = new ArrayList<>();
    public static ArrayList<String> cartDsc = new ArrayList<>();
    public static ArrayList<OrderItem> cartOrder = new ArrayList<>();

    private static LinearLayout layoutEmpty, layoutBuy, layoutHeader;
    private static RecyclerView recyclerView;
    private static OrderItemAdapter adapter;
    private static TextView tvPrice;
    private static int price = 0;
    TextView tvBuy, tvName;
    Button btnAddNew;
    View view;

    @SuppressLint("SetTextI18n")
    public static void setCartPrice() {
        price = 0;
        for (OrderItem items : cartOrder) {
            price += items.getPrice();
        }
        tvPrice.setText(price + " ₹");
        CustDashActivity.setupBadge();
    }

    public static void setOrderItem(Activity activity) {
        if (cartOrder.size() > 0) {
            layoutEmpty.setVisibility(View.GONE);
            layoutBuy.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            layoutBuy.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            cartItems = new ArrayList<>();
            cartOrder = new ArrayList<>();
            cartDsc = new ArrayList<>();
        }

        if (adapter != null) {
            recyclerView.setAdapter(null);
        }

        adapter = new OrderItemAdapter(activity, cartOrder, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        setCartPrice();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getContext());

        view = inflater.inflate(R.layout.fragment_cart, container, false);

        layoutEmpty = view.findViewById(R.id.showEmpty);
        layoutBuy = view.findViewById(R.id.showBottom);
        layoutHeader = view.findViewById(R.id.layoutHeader);

        recyclerView = view.findViewById(R.id.recyclerView);

        tvBuy = view.findViewById(R.id.tvAddCart);
        tvName = view.findViewById(R.id.tvName);
        tvPrice = view.findViewById(R.id.tvPrice);


        tvPrice.setText("");
        tvName.setText(SignActivity.customer.getFname() + " " + SignActivity.customer.getLname());
        tvBuy.setText("Order Now");
        tvBuy.setBackgroundColor(getResources().getColor(R.color.orange));

        btnAddNew = view.findViewById(R.id.btnAddNew);

        tvBuy.setOnClickListener(this);
        btnAddNew.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            boolean auto = false;
            if (auto) {
                ArrayList<Items> items = Items.allItems(getActivity());
                cartItems = new ArrayList<>();
                cartDsc = new ArrayList<>();

                for (int i = 0; i < 10; i++) {
                    cartItems.add(items.get(new Random().nextInt(items.size() - 1)));
                    cartDsc.add("");
                }
            }

            CartFragment.cartOrder = new ArrayList<>();

            for (int i = 0; i < cartItems.size(); i++) {
                //Use static ArrayList For Dsc ,qty
                Items item = cartItems.get(i);
                String dsc = cartDsc.get(i);
                OrderItem cartItem = new OrderItem(item.getiId(), 1, item.getPrice(), dsc);
                CartFragment.cartOrder.add(cartItem);
            }

            setOrderItem(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddCart:

                if (isNetAvail(getActivity(), view)) {
                    placeOrder();
                }
                break;

            case R.id.btnAddNew:
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.framelayout, new MenuFragment(), getResources().getString(R.string.menu)).commit();
                break;
        }
    }

    private void placeOrder() {
        try {
            //region Convert Into Json
            JSONObject obj = new JSONObject();

            JSONObject detail = new JSONObject();
            detail.put(MYSQL.Order.CUSTID, SignActivity.customer.getCustId());
            detail.put(MYSQL.Order.CURTIME, System.currentTimeMillis());
            detail.put(MYSQL.Order.PRICE, price);
            detail.put(MYSQL.Order.IID, cartOrder.get(0).getiId() + "");

            JSONObject ordObj = new JSONObject();
            ordObj.put(sDetail, new JSONArray().put(0, detail));

            JSONArray itemArray = new JSONArray();
            JSONObject itemObj = new JSONObject();

            for (int j = 0; j < cartOrder.size(); j++) {
                JSONObject items = new JSONObject();

                OrderItem cartItem = cartOrder.get(j);
                String dsc = cartItem.getDsc();
                items.put(MYSQL.OrderItem.IID, cartItem.getiId() + "");
                items.put(MYSQL.OrderItem.QTY, cartItem.getQty() + "");
                items.put(MYSQL.OrderItem.PRICE, cartItem.getPrice() + "");
                items.put(MYSQL.OrderItem.DSC, dsc);
                items.put(MYSQL.OrderItem.CID, getCid(getActivity(), cartItem.getiId()));

                itemArray.put(j, items);
                itemObj.put(sOrder, itemArray);
            }

            obj.put(sDetail, ordObj);
            obj.put(sOrder, itemObj);
            //endregion

            String json = obj.toString();
//            MyConst.toast(getActivity(), json);

            String url = ServerCall.BASE_URL + ServerCall.ORDER + "insertOrd";
            HashMap<String, String> hm = new HashMap<>();
            hm.put(sItems, json);

            new ServerCall(CartFragment.this, getActivity(), url, hm, MyConst.INSERT).execute();
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.equals("1")) {
                    MyConst.toast(getActivity(), "Ordered");
                    cartItems = new ArrayList<>();
                    cartOrder = new ArrayList<>();
                    cartDsc = new ArrayList<>();
                    setOrderItem(getActivity());
                }
                break;
        }
    }
}