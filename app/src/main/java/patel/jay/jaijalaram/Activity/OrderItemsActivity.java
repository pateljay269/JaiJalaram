package patel.jay.jaijalaram.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.OrderItemAdapter;
import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.ModelClass.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ModelClass.OrderItem.allOrderItem;

public class OrderItemsActivity extends Activity implements ServerCall.OnAsyncResponse {

    public static Order OITEM;

    Activity activity = OrderItemsActivity.this;
    OrderItemAdapter adapter;

    EditText etFind;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);

        String url1 = ServerCall.BASE_URL + ServerCall.ORDER + "allOid";
        HashMap<String, String> hm = new HashMap<>();
        hm.put(MYSQL.Order.OID, OITEM.getoId() + "");
        new ServerCall(activity, url1, hm, MyConst.SELECT).execute();

        etFind = findViewById(R.id.etFind);
        etFind.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);

        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.logout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OITEM = null;
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            if (adapter != null) {
                recyclerView.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.SELECT:
                if (response.length() > 0) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    adapter = new OrderItemAdapter(activity, allOrderItem(activity, response));
                    recyclerView.setAdapter(adapter);
                }
                break;
        }
    }
}
