package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.AttendanceAdapter;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.Models.Worker;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Constants.TimeConvert.date2Millies;
import static patel.jay.jaijalaram.Constants.TimeConvert.timeMiliesConvert;

public class AttendanceActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse {

    ArrayList<String> arrayList, idList;
    AttendanceAdapter adapter;

    Activity activity = AttendanceActivity.this;

    Button btnDate;
    RecyclerView rv;

    String date = "", timeMillies = "";
    boolean isDateAvail = false;
    TimeConvert tc;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance);
        titleSet(this, "Take Attendance");

        rv = findViewById(R.id.recyclerView);

        btnDate = findViewById(R.id.btnDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tc = timeMiliesConvert(System.currentTimeMillis());

                DatePickerDialog datePD = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        date = numberOf(dd) + "-" + numberOf(mm) + "-" + yy;
                        timeMillies = date2Millies(date) + "";
                        btnDate.setText(date);

                    }
                }, tc.getYy(), tc.getMm(), tc.getDd());

                int m = tc.getMm(), y = tc.getYy();
                if (tc.getMm() == 0) {
                    m = 12;
                    y -= 1;
                }

                datePD.getDatePicker().setMaxDate(System.currentTimeMillis());
                long millies = date2Millies(tc.getDd() + "-" + m + "-" + y);
                datePD.getDatePicker().setMinDate(millies);
                datePD.show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        setList(false);

        tc = timeMiliesConvert(System.currentTimeMillis());
        date = tc.getDD_MM_YY();
        timeMillies = date2Millies(date) + "";
        btnDate.setText(date);

    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_save:
                    checkDt();
                    break;

                case R.id.action_home:
                    finish();
                    break;

                case R.id.action_present:
                    setList(true);
                    break;

                case R.id.action_absent:
                    setList(false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void setList(boolean isPresent) {
        if (adapter != null) {
            rv.setAdapter(null);
        }

        arrayList = Worker.allWorkerStr(activity);
        idList = Worker.allWorkerIds(activity);

        adapter = new AttendanceAdapter(activity, arrayList, idList, isPresent);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.setAdapter(adapter);
    }

    private void checkDt() {
        //region Check For Existing Date
        url = ServerCall.BASE_URL + ServerCall.WORKER + "checkDate";
        new ServerCall(activity, url, null, MyConst.SELECT).execute();
        //endregion
    }

    private void saveAttendance() {
        try {
            //region Convert Into Json
            JSONObject obj = new JSONObject();

            JSONObject detail = new JSONObject();

            detail.put(MYSQL.Attend.CURTIME, timeMillies);

            JSONObject ordObj = new JSONObject();
            ordObj.put("detail", new JSONArray().put(0, detail));

            JSONArray itemArray = new JSONArray();
            JSONObject attendObj = new JSONObject();

            for (int j = 0; j < AttendanceAdapter.attendList.length; j++) {
                JSONObject attendLog = new JSONObject();

                String status = AttendanceAdapter.attendList[j];
                String wid = AttendanceAdapter.workerIds[j];
                attendLog.put(MYSQL.Attend.WID, wid);
                attendLog.put(MYSQL.Attend.STATUS, status);

                itemArray.put(j, attendLog);
                attendObj.put("attend", itemArray);
            }

            obj.put("detail", ordObj);
            obj.put("attend", attendObj);
            //endregion

            String json = obj.toString();
//            MyConst.toast(activity, json);

            url = ServerCall.BASE_URL + ServerCall.WORKER + "addAttend";
            HashMap<String, String> hm = new HashMap<>();
            hm.put("attend", json);

            new ServerCall(activity, url, hm, MyConst.INSERT).execute();
        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.equals("1")) {
                    toast(activity, "Added...");
//                    finish();
                }
                break;

            case MyConst.SELECT:
                isDateAvail = false;
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.DATES);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String dt = jsonObject.getString("date");

                            if (date.equalsIgnoreCase(dt)) {
                                isDateAvail = true;
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast(activity, e.getMessage());
                    }

                    if (isDateAvail) {
                        toast(activity, "Attendance Already Taken On This Date");
                    } else {
                        saveAttendance();
                    }
                }
                break;
        }
    }
}
