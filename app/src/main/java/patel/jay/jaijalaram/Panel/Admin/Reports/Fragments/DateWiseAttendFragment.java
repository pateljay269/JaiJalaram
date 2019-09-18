package patel.jay.jaijalaram.Panel.Admin.Reports.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.Models.Worker;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.toast;

public class DateWiseAttendFragment extends Fragment implements ServerCall.OnAsyncResponse,
        OnChartValueSelectedListener, View.OnClickListener {

    HorizontalBarChart barChart;
//    BarChart barChart;

    String MIN = "0";// ,caltype = "", MAX = "0",
    long startMili = 0, endMili = 0;

    TextView tvData;
    public static Button btnDtStart, btnDtEnd;
    Button btnFind;

    ArrayList<String> labels, values;
    ArrayList<Integer> totalS;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        btnFind = view.findViewById(R.id.btnFind);
        btnDtStart = view.findViewById(R.id.btnDtStart);
        btnDtEnd = view.findViewById(R.id.btnDtEnd);

        tvData = view.findViewById(R.id.tvData);
        view.findViewById(R.id.spnCal).setVisibility(View.INVISIBLE);

        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        btnFind.setVisibility(View.VISIBLE);
        btnDtStart.setVisibility(View.VISIBLE);
        btnDtEnd.setVisibility(View.VISIBLE);

        btnDtStart.setOnClickListener(this);
        btnDtEnd.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        try {
            String date = MyConst.getPrefData(getActivity(), PrefConst.MAXMINATTEND);
            JSONObject jsonObject = new JSONObject(date);
            JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.MAXMINATTEND);

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
            barChart.clear();
            tvData.setText("");

            String url = ServerCall.BASE_URL + ServerCall.REORTS + "attendRepo";
            HashMap<String, String> hm = new HashMap<>();
            hm.put("start", startMili + "");
            hm.put("end", endMili + "");
            new ServerCall(DateWiseAttendFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();
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
                //region Report
                try {
                    barChart.clear();
                    totalS = new ArrayList<>();
                    labels = new ArrayList<>();
                    values = new ArrayList<>();

                    ArrayList<BarEntry> dataset1 = new ArrayList<>();
                    ArrayList<BarEntry> dataset2 = new ArrayList<>();
                    ArrayList<BarEntry> dataset3 = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);

                    int wwi = 0, pos = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        int wid = jsonObject.getInt("wid");
                        String worker = Worker.workerName(getActivity(), wid);
                        int total = jsonObject.getInt("total");
                        String status = jsonObject.getString("status");

                        if (wwi != wid) {
                            if (wwi != 0)
                                pos++;

                            wwi = wid;
                            labels.add(worker);
                            values.add(total + "");
                            totalS.add(total);
                        }
                        switch (status.toUpperCase()) {
                            case "P":
                                dataset1.add(new BarEntry(total, pos));
                                break;

                            case "L":
                                dataset2.add(new BarEntry(total, pos));
                                break;

                            case "A":
                                dataset3.add(new BarEntry(total, pos));
                                break;
                        }

                    }

                    YAxis left = barChart.getAxisLeft();
                    left.setAxisMaxValue(30f);
                    left.setDrawGridLines(false);
                    left.setAxisMinValue(0);

                    BarDataSet set1 = new BarDataSet(dataset1, "P");
                    BarDataSet set2 = new BarDataSet(dataset2, "L");
                    BarDataSet set3 = new BarDataSet(dataset3, "A");

                    set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                    set2.setColor(ColorTemplate.COLORFUL_COLORS[1]);
                    set3.setColor(ColorTemplate.COLORFUL_COLORS[2]);

                    BarData dsd = new BarData(labels);
                    dsd.addDataSet(set1);
                    dsd.addDataSet(set2);
                    dsd.addDataSet(set3);

                    barChart.setData(dsd);

                } catch (Exception e) {
                    e.printStackTrace();
                    toast(getActivity(), e.getMessage());
                }
                //endregion
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String xVal = labels.get(e.getXIndex());
        tvData.setText(xVal);
    }

    int per = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        tvData.setText("");
    }
}