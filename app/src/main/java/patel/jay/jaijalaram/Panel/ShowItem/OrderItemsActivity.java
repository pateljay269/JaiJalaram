package patel.jay.jaijalaram.Panel.ShowItem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.OrderItemAdapter;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Customer;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Login.SignActivity.customer;
import static patel.jay.jaijalaram.Models.OrderItem.allOrderItem;

public class OrderItemsActivity extends Activity implements ServerCall.OnAsyncResponse,
        View.OnClickListener {

    public static Order OITEM;
    private static final int CALL_PERM = 101;

    Activity activity = OrderItemsActivity.this;
    OrderItemAdapter adapter;
    Customer cust;

    TextView tvName, tvMob, tvAdd;
    RecyclerView recyclerView;
    SwitchButton sbtnConfirm, sbtnDeliver;
    FloatingActionButton fab;
    ImageButton btnCall, btnDelete;
    EditText etFind;

    String url = "";
    HashMap<String, String> hm;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);

        hm = new HashMap<>();

        tvName = findViewById(R.id.tvName);
        tvMob = findViewById(R.id.tvMobile);
        tvAdd = findViewById(R.id.tvAddress);
        btnCall = findViewById(R.id.btnCall);
        btnDelete = findViewById(R.id.btnDelete);
        sbtnConfirm = findViewById(R.id.sbtnConfirm);
        sbtnDeliver = findViewById(R.id.sbtnDeliver);

        sbtnConfirm.setText("Yes", "No");
        sbtnDeliver.setText("Yes", "No");

        etFind = findViewById(R.id.etFind);

        btnCall.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        btnDelete.setVisibility(View.VISIBLE);

        sbtnConfirm.setOnClickListener(this);
        sbtnDeliver.setOnClickListener(this);

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

            Order ord = null;
            boolean isNoti = false;
            try {
                ord = (Order) getIntent().getSerializableExtra(MYSQL.Order.OID);
                isNoti = getIntent().getBooleanExtra("isNoti", false);
            } catch (Exception e) {
                e.printStackTrace();
                ord = null;
            }

            if (ord == null && OITEM == null) {
                return;
            } else if (isNoti) {
                OITEM = ord;
                customer = Customer.fromGson(activity);
            }

            if (isNetAvail(activity)) {
                url = ServerCall.BASE_URL + ServerCall.ORDER + "allOid";
                hm = new HashMap<>();
                hm.put(MYSQL.Order.OID, OITEM.getoId() + "");
                new ServerCall(activity, url, hm, MyConst.SELECT).execute();
            } else {
                toast(activity, "There Is No Internet Connection...!!!");
                finish();
            }
            sbtnConfirm.setChecked(OITEM.isConfirm());
            sbtnDeliver.setChecked(OITEM.isDeliver());

            if (OITEM.isConfirm())
                sbtnConfirm.setEnabled(false);

            if (OITEM.isDeliver()) {
                sbtnDeliver.setEnabled(false);
                btnDelete.setVisibility(View.GONE);
            }

            if (!customer.getType().equals("U")) {
                findViewById(R.id.layout).setVisibility(View.VISIBLE);
                cust = Customer.customer(activity, OITEM.getCustId());

            } else {
                etFind.setVisibility(View.VISIBLE);
                cust = Customer.fromGson(activity);
            }

            tvAdd.setText(cust.getAddress());
            tvMob.setText(cust.getMobile());
            tvName.setText(cust.toString());
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.SELECT:
                if (response.trim().equals("")) {
                    refreshAllOrd();
                    finish();
                    return;
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                adapter = new OrderItemAdapter(activity, allOrderItem(activity, response), false);
                recyclerView.setAdapter(adapter);

                break;

            case MyConst.INSERT:
                if (response.trim().equals("1")) {
                    sbtnConfirm.setChecked(true);
                    sbtnConfirm.setEnabled(false);

                    refreshAllOrd();
                } else
                    toast(activity, "Error: " + response);
                break;

            case 1001:
                if (response.trim().equals("1")) {
                    sbtnDeliver.setChecked(true);
                    sbtnDeliver.setEnabled(false);

                    sbtnConfirm.setChecked(true);
                    sbtnConfirm.setEnabled(false);
                    btnDelete.setVisibility(View.GONE);
                    refreshAllOrd();
                } else
                    toast(activity, "Error: " + response);
                break;

            case MyConst.UPDATE:
                MyConst.putIntoPref(activity, PrefConst.AORDER_S, response);
                break;

            case MyConst.DELETE:
                if (response.trim().equals("1")) {
                    toast(activity, "Deleted");

                    url = ServerCall.BASE_URL + ServerCall.ORDER + "allOid";
                    hm = new HashMap<>();
                    hm.put(MYSQL.Order.OID, OITEM.getoId() + "");
                    new ServerCall(activity, url, hm, MyConst.SELECT).execute();
                } else
                    toast(activity, "Error: " + response);
                break;
        }
    }

    private void refreshAllOrd() {
        url = ServerCall.BASE_URL + ServerCall.AALL + "allOrd";
        hm = new HashMap<>();
        new ServerCall(activity, url, hm, MyConst.UPDATE).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCall:
                requestCall();
                break;

            case R.id.sbtnConfirm:
                alertView("CONFIRM");
                break;

            case R.id.sbtnDeliver:
                alertView("DELIVER");
                break;

            case R.id.btnDelete:
                alertView("DELETE");
                break;
        }
    }

    //region Request Permissions
    private void requestCall() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            checkTest();
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PERM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkTest();
                } else {
                    finish();
                    MyConst.toast(this, "Give Permission From Settings");
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void checkTest() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + cust.getMobile()));
        startActivity(callIntent);
    }
    //endregion

    private void alertView(final String isConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm ???");

        builder.setTitle("Are You Sure??")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        switch (isConfirm) {
                            case "CONFIRM":
                                sbtnConfirm.setChecked(false);
                                break;

                            case "DELIVER":
                                sbtnDeliver.setChecked(false);
                                break;
                        }
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        url = ServerCall.BASE_URL + ServerCall.ORDER;

                        hm = new HashMap<>();
                        hm.put(MYSQL.Order.OID, OITEM.getoId() + "");

                        switch (isConfirm) {
                            case "CONFIRM":
                                url += "confirmOrd";
                                new ServerCall(activity, url, hm, MyConst.INSERT).execute();
                                break;

                            case "DELIVER":
                                url += "deliverOrd";
                                new ServerCall(activity, url, hm, 1001).execute();
                                break;

                            case "DELETE":
                                url += "delOrder";
                                new ServerCall(activity, url, hm, MyConst.DELETE).execute();
                                break;
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
