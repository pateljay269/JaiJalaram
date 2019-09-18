package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import patel.jay.jaijalaram.Adapter.OrderAdapter;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Models.Order.jsonToOrd;

public class OrderDisplayActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse {

    Activity activity = OrderDisplayActivity.this;
    Context context = OrderDisplayActivity.this;

    View view;
    RecyclerView recyclerView;
    ArrayList<Order> orderList;

    RecyclerView.Adapter adapter = null;

    private final String url = ServerCall.BASE_URL + ServerCall.ORDER + "newOrd";

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);
        titleSet(this, getString(R.string.orders));

        findViewById(R.id.fab).setVisibility(View.GONE);
        findViewById(R.id.etFind).setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
        view = findViewById(R.id.coordinator);

    }

    private void checkOrd() {
        if (isNetAvail(activity, view)) {
            new ServerCall(context, url, null, 101).execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                checkOrd();

                handler.postDelayed(runnable, 60 * 1000); //1 Min
            }
        };

        handler.postDelayed(runnable, 10);

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                finish();
                break;

            case R.id.action_refresh:
                checkOrd();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void getResponse(String response, int flag) {

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        if (response.trim().equals("")) {
            Snackbar sb = Snackbar
                    .make(view, "No More Order Pending...!!!", Snackbar.LENGTH_LONG)//;
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkOrd();
                        }
                    });

            sb.setActionTextColor(Color.GREEN);
            View sbView = sb.getView();
            TextView tv = sbView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextSize(20);
            tv.setTextColor(Color.YELLOW);
            sb.show();
            return;
        }

        try {

            JSONArray jArr = new JSONObject(response).getJSONArray("NEWORDER");

            orderList = new ArrayList<>();
            for (int i = 0; i < jArr.length(); i++) {
                orderList.add(jsonToOrd(jArr.getJSONObject(i)));
            }

            adapter = new OrderAdapter(activity, orderList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
