package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.CustAdapter;
import patel.jay.jaijalaram.Adapter.OrderAdapter;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Customer;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Models.Customer.adminAllCust;
import static patel.jay.jaijalaram.Models.Order.allOrder;

public class TempRVActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse {

    public static String type = "";
    Activity activity = TempRVActivity.this;
    RecyclerView recyclerView;
    EditText etFind;
    FloatingActionButton fab;
    ArrayList<Order> orderList, tempOList;
    ArrayList<Customer> custList, tempCList;

    RecyclerView.Adapter adapter = null;
    String tempName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        etFind = findViewById(R.id.etFind);

        fab.setVisibility(View.GONE);

        etFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempName = etFind.getText().toString();
                switch (type) {
                    case PrefConst.AUSER_S:
                        //region TextChangedArrayList
                        if (tempName.isEmpty()) {
                            custList = tempCList;
                        } else {
                            //region Search
                            custList = new ArrayList<>();
                            for (Customer customer : tempCList) {
                                if (customer.getFname().contains(tempName)
                                        || customer.getLname().contains(tempName)) {
                                    custList.add(customer);
                                }
                            }
                            //endregion
                        }
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));
                        adapter = new CustAdapter(activity, custList);
                        recyclerView.setAdapter(adapter);
                        //endregion

                        break;
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        switch (type) {
            case PrefConst.AORDER_S:
                titleSet(this, getString(R.string.orders));
                orderList = allOrder(activity, true);
                adapter = new OrderAdapter(activity, orderList);
                break;

            case PrefConst.AUSER_S:
                etFind.setVisibility(View.VISIBLE);
                titleSet(this, getString(R.string.user));
                fab.setVisibility(View.VISIBLE);
                custList = adminAllCust(activity);
                tempCList = custList;
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));
                adapter = new CustAdapter(activity, custList);
                break;
        }

        recyclerView.setAdapter(adapter);

    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (type) {
            case PrefConst.AORDER_S:
                getMenuInflater().inflate(R.menu.order_status, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = ServerCall.BASE_URL + ServerCall.AALL;

        switch (item.getItemId()) {
            case R.id.action_not_confirm:
                url += "allNotConfirm";
                new ServerCall(activity, url, null, 1101).execute();
                break;

            case R.id.action_not_deliver:
                url += "allNotDeliver";
                new ServerCall(activity, url, null, 1102).execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void onBackPressed() {
        type = "";
        super.onBackPressed();
    }

    @Override
    public void getResponse(String response, int flag) {
        if (response.length() == 0) {
            View view = findViewById(R.id.coordinator);
            Snackbar.make(view, "No Data Found", Snackbar.LENGTH_LONG).show();
            return;
        }

        RecyclerView.Adapter adapter = null;

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        try {
            ArrayList<Order> arrayList = Order.allOrder(activity, response);

            switch (flag) {
                case 1101:
                    titleSet(this, getString(R.string.not_confirm));
                    adapter = new OrderAdapter(activity, arrayList);
                    break;

                case 1102:
                    titleSet(this, getString(R.string.not_deliver));
                    adapter = new OrderAdapter(activity, arrayList);
                    break;
            }

            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
