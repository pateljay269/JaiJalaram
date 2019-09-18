package patel.jay.jaijalaram.Panel.Admin.Reports.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Panel.Admin.Reports.ChartData.setBar;

public class DateWiseFragment extends Fragment implements ServerCall.OnAsyncResponse,
        OnChartValueSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    HorizontalBarChart barChart;
//    BarChart barChart;

    String caltype = "", MAX = "0", MIN = "0";
    long startMili = 0, endMili = 0;

    public static Spinner spnCal;
    TextView tvData;
    CheckBox chkDisName;
    Button btnFind, btnDtStart, btnDtEnd;

    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    ArrayList<Integer> values;
    ArrayList<Float> totalS;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        chkDisName = view.findViewById(R.id.chkDisName);

        btnFind = view.findViewById(R.id.btnFind);
        btnDtStart = view.findViewById(R.id.btnDtStart);
        btnDtEnd = view.findViewById(R.id.btnDtEnd);

        tvData = view.findViewById(R.id.tvData);
        spnCal = view.findViewById(R.id.spnCal);

//        spnCal.setOnItemSelectedListener(this);

        chkDisName.setOnCheckedChangeListener(this);

        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        btnFind.setVisibility(View.VISIBLE);
        btnDtStart.setVisibility(View.VISIBLE);
        btnDtEnd.setVisibility(View.VISIBLE);

        btnDtStart.setOnClickListener(this);
        btnDtEnd.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        try {
            String date = MyConst.getPrefData(getActivity(), PrefConst.MAXMIN);
            JSONObject jsonObject = new JSONObject(date);
            JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.MAXMIN);

            jsonObject = jsonArray.getJSONObject(0);
//            MAX = jsonObject.getString("maxtime");
            MIN = jsonObject.getString("mintime");
        } catch (JSONException e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }

//        selectDate(true);
        return view;
    }

    private void selectDate(final boolean isStart) {
        TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
        int date = tc.getDd();
        int mon = tc.getMm();
        int year = tc.getYy();

        if (isStart) {
            mon = tc.getMm() - 1;

            if (tc.getMm() == 0) {
                mon = 11;
                year -= 1;
            }
        }
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String strDate = numberOf(date) + "-" + numberOf(month + 1) + "-" + year;

                if (isStart) {
                    btnDtStart.setText(strDate);
                    startMili = TimeConvert.date2Millies(strDate);

                    if (endMili == 0) {
                        selectDate(false);
                    }
                } else {
                    btnDtEnd.setText(strDate);
                    endMili = TimeConvert.date2Millies(strDate);

                    findDetail();
                }
            }
        }, year, mon, date);

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.getDatePicker().setMinDate(Long.parseLong(MIN));
        dpd.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {

            String arr[] = getResources().getStringArray(R.array.someCal);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr);
            spnCal.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnDtEnd:
                selectDate(false);
                break;

            case R.id.btnDtStart:
                selectDate(true);
                break;

            case R.id.btnFind:
                findDetail();
                break;
        }
    }

    private void findDetail() {
        if (startMili == 0 || endMili == 0) {
            toast(getActivity(), "Set Dates First");
            selectDate(true);
        } else {
            caltype = spnCal.getSelectedItem().toString();

            barChart.clear();
            tvData.setText("");

            String url = ServerCall.BASE_URL + ServerCall.REORTS + "dateWise";
            HashMap<String, String> hm = new HashMap<>();
            hm.put("type", caltype);
            hm.put("start", startMili + "");
            hm.put("end", endMili + "");
            new ServerCall(DateWiseFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void getResponse(String response, int flag) {
        if (response.length() == 0) {
            barChart.clear();
            toast(getActivity(), "No Data Found");
            return;
        }

        switch (flag) {
            case MyConst.SELECT:

                try {
                    entries = new ArrayList<>();
                    labels = new ArrayList<>();
                    values = new ArrayList<>();
                    totalS = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);

                    per = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String type = jsonObject.getString("type");
                        int total = jsonObject.getInt("total");

                        switch (caltype.toUpperCase()) {
                            case "MONTH":
                                type = MyConst.monthName(Integer.parseInt(type));
                                break;

                            case "DAY":
                                type = MyConst.dayName(Integer.parseInt(type) + 1);
                                break;
                        }

                        per += total;
                        values.add(total);
                        labels.add(type);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        float val = values.get(i);
                        float temp = val * 100 / per;
                        totalS.add(temp);

                        String s = String.format("%.1f", temp);
                        entries.add(new BarEntry(Float.parseFloat(s), i));
                    }

                    YAxis left = barChart.getAxisLeft();

                    left.setAxisMaxValue(110f);

                    barChart = setBar(barChart, entries, labels);
                } catch (JSONException e) {
                    e.printStackTrace();
                    toast(getActivity(), e.getMessage());
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String xVal = labels.get(e.getXIndex());
        int yVal = values.get(e.getXIndex());
        String rs = " ₹";
        tvData.setText(xVal + " :- " + yVal + rs);
    }

    int per = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        tvData.setText("Total : " + per + " " + getString(R.string.rs));
    }

    boolean isName = false;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isName = b;

        barChart.clear();
        tvData.setText("");

        String url = ServerCall.BASE_URL + ServerCall.REORTS + "gross";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("type", "iid");
        new ServerCall(DateWiseFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();
    }

}