package patel.jay.jaijalaram.Panel.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.Other.BackgroundService;
import patel.jay.jaijalaram.Panel.Admin.Activity.AddItemActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.AdminCustAddActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.AttendanceActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.MenuActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.TempRVActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.WorkersActivity;
import patel.jay.jaijalaram.Panel.Admin.Reports.ReportsNavActivity;
import patel.jay.jaijalaram.Panel.Customer.CustDashActivity;
import patel.jay.jaijalaram.Panel.Admin.Activity.OrderDisplayActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Login.SignActivity.customer;

public class AdminDashActivity extends AppCompatActivity implements View.OnClickListener,
        ServerCall.OnAsyncResponse {

    Activity activity = AdminDashActivity.this;
    //    HashMap<String, String> hm;
    String url = "";

    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
        titleSet(this, "Admin Dashboard");

        findViewById(R.id.btnAddItem).setOnClickListener(this);
        findViewById(R.id.btnEditMenu).setOnClickListener(this);
        findViewById(R.id.btnOrd).setOnClickListener(this);
        findViewById(R.id.btnCust).setOnClickListener(this);
        findViewById(R.id.btnReport).setOnClickListener(this);
        findViewById(R.id.btnWorker).setOnClickListener(this);
        findViewById(R.id.btnAttend).setOnClickListener(this);
        findViewById(R.id.btnAddCust).setOnClickListener(this);
        findViewById(R.id.btnOrdDisplay).setOnClickListener(this);

        refreshData();

        service = new Intent(activity, BackgroundService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (customer != null && customer.getType().equals("M")) {
            titleSet(this, "Manager Dashboard");

            startService(service);

//            findViewById(R.id.btnOrdDisplay).setVisibility(View.GONE);
//            findViewById(R.id.btnOrd).setVisibility(View.GONE);
//            findViewById(R.id.btnWorker).setVisibility(View.GONE);
//            findViewById(R.id.btnAttend).setVisibility(View.GONE);
//            findViewById(R.id.btnAddCust).setVisibility(View.GONE);
//            findViewById(R.id.btnAddItem).setVisibility(View.GONE);
//            findViewById(R.id.btnEditMenu).setVisibility(View.GONE);
//            findViewById(R.id.btnCust).setVisibility(View.GONE);
//            findViewById(R.id.btnReport).setVisibility(View.GONE);

            findViewById(R.id.btnCust).setVisibility(View.GONE);
            findViewById(R.id.btnReport).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customer == null || !customer.getType().equals("M")) {
            stopService(service);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnOrdDisplay:
                startActivity(new Intent(activity, OrderDisplayActivity.class));
                break;

            case R.id.btnAddItem:
                startActivity(new Intent(activity, AddItemActivity.class));
                break;

            case R.id.btnEditMenu:
                startActivity(new Intent(activity, MenuActivity.class));
                break;

            case R.id.btnCust:
                TempRVActivity.type = PrefConst.AUSER_S;
                startActivity(new Intent(activity, TempRVActivity.class));
                break;

            case R.id.btnOrd:
                TempRVActivity.type = PrefConst.AORDER_S;
                startActivity(new Intent(activity, TempRVActivity.class));
                break;

            case R.id.btnReport:
                startActivity(new Intent(activity, ReportsNavActivity.class));
                break;

            case R.id.btnWorker:
                startActivity(new Intent(activity, WorkersActivity.class));
                break;

            case R.id.btnAttend:
                startActivity(new Intent(activity, AttendanceActivity.class));
                break;

            case R.id.btnAddCust:
                startActivity(new Intent(activity, AdminCustAddActivity.class));
                break;

        }
    }

    private void refreshData() {
        if (isNetAvail(activity, findViewById(R.id.grid))) {

            url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
            new ServerCall(activity, url, null, 1101).execute();

            url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
            new ServerCall(activity, url, null, 1102).execute();

            url = ServerCall.BASE_URL + ServerCall.AALL + "allUser";
            new ServerCall(activity, url, null, 1103).execute();

            url = ServerCall.BASE_URL + ServerCall.AALL + "allOrd";
            new ServerCall(activity, url, null, 1104).execute();

            url = ServerCall.BASE_URL + ServerCall.AALL + "allWorker";
            new ServerCall(activity, url, null, 1105).execute();

            url = ServerCall.BASE_URL + ServerCall.LIST + "maxMin";
            new ServerCall(activity, url, null, 1106).execute();

            url = ServerCall.BASE_URL + ServerCall.LIST + "maxMinAttend";
            new ServerCall(activity, url, null, 1107).execute();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        if (response.length() == 0) {
            return;
        }

        switch (flag) {
            case 1101:
                MyConst.putIntoPref(activity, PrefConst.CATEGORY_S, response);
                break;

            case 1102:
                MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);
                break;

            case 1103:
                MyConst.putIntoPref(activity, PrefConst.AUSER_S, response);
                break;

            case 1104:
                MyConst.putIntoPref(activity, PrefConst.AORDER_S, response);
                break;

            case 1105:
                MyConst.putIntoPref(activity, PrefConst.WORKER_S, response);
                break;

            case 1106:
                MyConst.putIntoPref(activity, PrefConst.MAXMIN, response);
                break;

            case 1107:
                MyConst.putIntoPref(activity, PrefConst.MAXMINATTEND, response);
                break;

            case MyConst.INSERT:
                startActivity(new Intent(activity, SignActivity.class));
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.getItem(0).setIcon(R.drawable.refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
//                logOut();
                refreshData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void logOut() {
        CustDashActivity.logOut(activity);
    }
}