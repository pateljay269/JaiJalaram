package patel.jay.jaijalaram.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import patel.jay.jaijalaram.Admin.Activity.AddItemActivity;
import patel.jay.jaijalaram.Admin.Activity.CatItemActivity;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.R;

public class AdminDashActivity extends AppCompatActivity implements View.OnClickListener, ServerCall.OnAsyncResponse {

    Activity activity = AdminDashActivity.this;
    Button btnAddItem, btnEditMenu, btnPie, btnOrd, btnCust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnEditMenu = findViewById(R.id.btnEditMenu);
        btnPie = findViewById(R.id.btnPie);
        btnOrd = findViewById(R.id.btnOrd);
        btnCust = findViewById(R.id.btnCust);

        btnAddItem.setOnClickListener(this);
        btnEditMenu.setOnClickListener(this);
        btnPie.setOnClickListener(this);
        btnOrd.setOnClickListener(this);
        btnCust.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnAddItem:
                    startActivity(new Intent(activity, AddItemActivity.class));
                    break;

                case R.id.btnEditMenu:
                    startActivity(new Intent(activity, CatItemActivity.class));
                    break;

                case R.id.btnCust:
                    TempRVActivity.type = PrefConst.AUSER_S;
                    startActivity(new Intent(activity, TempRVActivity.class));
                    break;

                case R.id.btnOrd:
                    TempRVActivity.type = PrefConst.AORDER_S;
                    startActivity(new Intent(activity, TempRVActivity.class));
                    break;

                case R.id.btnPie:
//                    startActivity(new Intent(activity, PieChartActivity.class));
                    startActivity(new Intent(activity, BarChartActivity.class));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String url = "";

        url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
        new ServerCall(activity, url, new HashMap<String, String>(), 1101).execute();

        url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
        new ServerCall(activity, url, new HashMap<String, String>(), 1102).execute();

        url = ServerCall.BASE_URL + ServerCall.AALL + "allUser";
        new ServerCall(activity, url, new HashMap<String, String>(), 1103).execute();

        url = ServerCall.BASE_URL + ServerCall.AALL + "allOrd";
        new ServerCall(activity, url, new HashMap<String, String>(), 1104).execute();
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case 1101:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.CATEGORY_S, response);
                    }
                    break;

                case 1102:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);
                    }
                    break;

                case 1103:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.AUSER_S, response);
                    }
                    break;

                case 1104:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.AORDER_S, response);
                    }
                    break;

                case MyConst.INSERT:
                    if (DashActivity.logClear(activity, response)) {
                        startActivity(new Intent(activity, SignActivity.class));
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                break;

            case R.id.action_cart:

                break;

            case R.id.action_notify:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void logOut() {
        DashActivity.logOut(activity);
    }
}