package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.Models.Worker;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.clearEdittext;
import static patel.jay.jaijalaram.Constants.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;

public class WorkerProfileActivity extends AppCompatActivity implements View.OnClickListener,
        ServerCall.OnAsyncResponse {

    View view;
    EditText etName, etMob, etJoinDt, etAddress;
    RadioButton rdbM, rdbF;
    Button btnAdd, btnClear;
    TextView tvAddedOn;
    ViewGroup layout;

    String timeMillies = "";

    public static boolean isUpdate = false;
    public static Worker worker = null;

    Activity activity = WorkerProfileActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_worker_profile);

        layout = findViewById(R.id.layout);

        tvAddedOn = findViewById(R.id.tvAddedOn);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etMob = findViewById(R.id.etMobile);
        etJoinDt = findViewById(R.id.etJoinDate);
        etJoinDt.setTextColor(Color.BLACK);

        rdbM = findViewById(R.id.rdbMale);
        rdbF = findViewById(R.id.rdbFemale);

        btnClear = findViewById(R.id.btnClear);
        btnAdd = findViewById(R.id.btnAdd);

        btnClear.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        findViewById(R.id.btnDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

                new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        String dt = numberOf(dd) + "-" + numberOf(mm + 1) + "-" + yy;
                        timeMillies = TimeConvert.date2Millies(dt) + "";
                        etJoinDt.setText(dt);
                    }
                }, tc.getYy(), tc.getMm(), tc.getDd()).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        tvAddedOn.setText("Added On " + TimeConvert.toDay());

        titleSet(this, "Add Worker");

        if (isUpdate && worker != null) {
            titleSet(this, "Worker Profile");

            btnAdd.setText("Update");
            tvAddedOn.setText("Added On " + worker.getAddedOn());

            timeMillies = TimeConvert.date2Millies(worker.getJoinDt()) + "";

            etName.setText(worker.getName());
            etAddress.setText(worker.getAddress());
            etJoinDt.setText(worker.getJoinDt());
            etMob.setText(worker.getMobile());

            boolean gender = worker.getGender().equalsIgnoreCase("M");
            rdbM.setChecked(gender);
            rdbF.setChecked(!gender);
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.getItem(0).setIcon(R.drawable.leftarrow);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_home:
                    isUpdate = false;
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion


    @Override
    public void onBackPressed() {
        isUpdate = false;
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                clearEdittext(layout);
                break;

            case R.id.btnAdd:
                if (!isNetAvail(activity, view)) {
                    return;
                }

                String name = etName.getText().toString().trim();
                String mobile = etMob.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String joinDt = etJoinDt.getText().toString().trim();

                String gender = rdbM.isChecked() ? "M" : "F";

                if (name.isEmpty() || address.isEmpty()
                        || mobile.isEmpty() || joinDt.isEmpty()) {

                    etBlankCheck(etName);
                    etBlankCheck(etMob);
                    etBlankCheck(etAddress);
                    etBlankCheck(etJoinDt);
                } else {
                    String url = "";

                    if (isUpdate) {
                        url = "updateWork";
                    } else {
                        url = "insertWork";
                    }

                    url = ServerCall.BASE_URL + ServerCall.WORKER + url;
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(MYSQL.Worker.NAME, name);
                    hm.put(MYSQL.Worker.MOBILE, mobile);
                    hm.put(MYSQL.Worker.ADDRESS, address);
                    hm.put(MYSQL.Worker.JOINDT, joinDt);
                    hm.put(MYSQL.Worker.GENDER, gender);
                    hm.put(MYSQL.Worker.CURTIME, timeMillies);
                    if (isUpdate) {
                        hm.put(MYSQL.Worker.WID, worker.getWid() + "");
                    }

                    new ServerCall(activity, url, hm, MyConst.INSERT)
                            .execute();

                }
                break;
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                String msg = "";
                if (response.trim().equals("1")) {

                    if (isUpdate) {
                        msg = "Updated...";
                    } else {
                        msg = "Added...";
                        clearEdittext(layout);
                    }
                } else {
                    msg = "Fail:" + response;
                }
                toast(activity, msg);
                break;
        }
    }
}
