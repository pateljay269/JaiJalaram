package patel.jay.jaijalaram.Panel.Admin.Reports.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Worker;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.monthName;
import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendFragment extends Fragment implements OnChartValueSelectedListener,
        ServerCall.OnAsyncResponse, View.OnClickListener {

    HorizontalBarChart barChart;

    ArrayList<String> labels, values;
    ArrayList<Integer> totalS, monthAr;
    ArrayAdapter<String> adapter;

    TextView tvData;
    Button btnFind;
    public static Spinner spnCal, spnYear, spnMon, spnDate;

    String caltype = "";
    ArrayList<String> list;
    View view;
    String url;
    HashMap<String, String> hm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        tvData = view.findViewById(R.id.tvData);
        btnFind = view.findViewById(R.id.btnFind);
        btnFind.setOnClickListener(this);
        btnFind.setVisibility(View.VISIBLE);

        spnCal = view.findViewById(R.id.spnCal);

        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.someCal)));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        spnCal.setAdapter(adapter);

        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.asd)));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);

        spnYear = view.findViewById(R.id.spnYear);
        spnMon = view.findViewById(R.id.spnMon);
        spnDate = view.findViewById(R.id.spnDate);

        spnCal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                caltype = spnCal.getSelectedItem().toString();

                switch (spnCal.getSelectedItem().toString().toUpperCase()) {
                    case "MONTH":
                        spnMon.setVisibility(View.VISIBLE);
                        spnDate.setVisibility(View.GONE);

                        spnDate.setAdapter(adapter);
                        break;

                    case "DATE":
                        spnMon.setVisibility(View.VISIBLE);
                        spnDate.setVisibility(View.VISIBLE);
                        break;

                    default:
                        spnMon.setVisibility(View.GONE);
                        spnDate.setVisibility(View.GONE);

                        spnMon.setAdapter(adapter);
                        spnDate.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tvData.setText("");
            }
        });

        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    int year = Integer.parseInt(spnYear.getSelectedItem().toString());
                    url = ServerCall.BASE_URL + ServerCall.LIST + "attendmonth";
                    hm = new HashMap<>();
                    hm.put("year", year + "");
                    new ServerCall(AttendFragment.this, getActivity(), url, hm, 102).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tvData.setText("");
            }
        });

        spnMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (!spnCal.getSelectedItem().toString().equalsIgnoreCase("Month")
                        && !spnMon.getSelectedItem().toString().equals(getString(R.string.select))) {
                    url = ServerCall.BASE_URL + ServerCall.LIST + "attenddate";
                    hm = new HashMap<>();
                    hm.put("year", spnYear.getSelectedItem().toString().trim());
//                    hm.put("month", spnMon.getSelectedItem().toString().trim());
                    hm.put("month", monthAr.get(pos) + "");
                    new ServerCall(AttendFragment.this, getActivity(), url, hm, 103).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tvData.setText("");
            }
        });

        spnYear.setVisibility(View.VISIBLE);
        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        url = ServerCall.BASE_URL + ServerCall.LIST + "attendlist";
        hm = new HashMap<>();
        hm.put("listof", "year");
        new ServerCall(AttendFragment.this, getActivity(), url, hm, 101).execute();

        return view;
    }

    private ArrayList<String> setData(String response, String caltype) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("LIST");

            ArrayList<String> arrayList = new ArrayList<>();

            if (caltype.equals("month"))
                monthAr = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                int list = jsonObject.getInt(caltype);

                if (caltype.equals("month")) {
                    monthAr.add(list);
                    arrayList.add(monthName(list));
                } else
                    arrayList.add(numberOf(list));
            }

            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }
        return new ArrayList<>();
    }

    private void setSpn(String response, int flag) {
        try {
            String key = "";
            switch (flag) {
                case 101:
                    key = "year";
                    break;

                case 102:
                    key = "month";
                    break;

                case 103:
                    key = "date";
                    break;
            }

            if (key.equals("")) {
                return;
            }
            ArrayList<String> arrayList = setData(response, key);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

            switch (flag) {
                case 101:
                    spnYear.setAdapter(adapter);
                    break;

                case 102:
                    spnMon.setAdapter(adapter);
                    break;

                case 103:
                    spnDate.setAdapter(adapter);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
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
            case 101:
            case 102:
            case 103:
                setSpn(response, flag);
                break;

            case 2001:
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
                    left.setDrawGridLines(false);
                    left.setAxisMinValue(0);
//                    left.setAxisMaxValue(100f);

                    if (caltype.equals(getString(R.string.date))) {

                        left.setAxisMaxValue(1.1f);
                    } else if (caltype.equals(getString(R.string.month))) {
                        left.setAxisMaxValue(31f);

                    } else if (caltype.equals(getString(R.string.year))) {
                        left.setAxisMaxValue(365f);

                    }


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

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        tvData.setText("");
    }

    @Override
    public void onClick(View view) {
        url = ServerCall.BASE_URL + ServerCall.REORTS + "attendspecificRepo";
        hm = new HashMap<>();
        url += "";
        hm.put("year", spnYear.getSelectedItem().toString());
        hm.put("date", spnDate.getSelectedItem().toString());
        hm.put("type", caltype);

        if (monthAr != null && monthAr.size() != 0)
            hm.put("month", monthAr.get(spnMon.getSelectedItemPosition()) + "");
        else
            hm.put("month", "");
        new ServerCall(AttendFragment.this, getActivity(), url, hm, 2001).execute();
    }
}