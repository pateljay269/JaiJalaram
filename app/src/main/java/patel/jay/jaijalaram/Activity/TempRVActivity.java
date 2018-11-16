package patel.jay.jaijalaram.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;

import patel.jay.jaijalaram.Adapter.CustAdapter;
import patel.jay.jaijalaram.Adapter.OrderAdapter;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ModelClass.Customer;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ConstClass.MyConst.ROWCOUNT;
import static patel.jay.jaijalaram.ModelClass.Order.allOrder;

public class TempRVActivity extends Activity {

    public static String type = "";
    Activity activity = TempRVActivity.this;
    RecyclerView recyclerView;
    EditText etFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);

        recyclerView = findViewById(R.id.recyclerView);
        etFind = findViewById(R.id.etFind);
        etFind.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView.Adapter adapter = null;

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        switch (type) {
            case PrefConst.AORDER_S:
                adapter = new OrderAdapter(activity, allOrder(activity, true));
                break;

            case PrefConst.AUSER_S:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ROWCOUNT, StaggeredGridLayoutManager.VERTICAL));
                adapter = new CustAdapter(activity, Customer.allCust(activity));
                break;
        }

        recyclerView.setAdapter(adapter);

    }

}
