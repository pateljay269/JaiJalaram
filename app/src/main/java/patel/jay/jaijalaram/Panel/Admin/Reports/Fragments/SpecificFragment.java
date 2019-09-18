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
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Panel.Admin.Reports.ChartData.setBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificFragment extends Fragment implements OnChartValueSelectedListener,
        ServerCall.OnAsyncResponse, View.OnClickListener {

    HorizontalBarChart barChart;

    ArrayList<BarEntry> entries;
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
        list.remove(getString(R.string.year));

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
                    url = ServerCall.BASE_URL + ServerCall.LIST + "month";
                    hm = new HashMap<>();
                    hm.put("year", year + "");
                    new ServerCall(SpecificFragment.this, getActivity(), url, hm, 102).execute();
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
                    url = ServerCall.BASE_URL + ServerCall.LIST + "date";
                    hm = new HashMap<>();
                    hm.put("year", spnYear.getSelectedItem().toString().trim());
//                    hm.put("month", spnMon.getSelectedItem().toString().trim());
                    hm.put("month", monthAr.get(pos) + "");
                    new ServerCall(SpecificFragment.this, getActivity(), url, hm, 103).execute();
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

        url = ServerCall.BASE_URL + ServerCall.LIST + "list";
        hm = new HashMap<>();
        hm.put("listof", "year");
        new ServerCall(SpecificFragment.this, getActivity(), url, hm, 101).execute();

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
                    arrayList.add(MyConst.monthName(list));
                } else
                    arrayList.add(MyConst.numberOf(list));
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

                    totalS = new ArrayList<>();
                    labels = new ArrayList<>();
                    values = new ArrayList<>();
                    per = 0;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        int type = jsonObject.getInt("type");
                        int total = jsonObject.getInt("total");

                        per += total;
                        labels.add(type + "");

                        values.add(total + "");
                        totalS.add(total);
                    }

                    entries = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        float total = totalS.get(i);
                        total = total * 100 / per;
                        String s = String.format("%.1f", total);
                        entries.add(new BarEntry(Float.parseFloat(s), i));
                    }

                    barChart = setBar(barChart, entries, labels);

                    YAxis left = barChart.getAxisLeft();
                    left.setAxisMaxValue(100f);

                    String yAx = "Y : ";
                    if (caltype.equals(getResources().getString(R.string.month))) {
                        yAx += " Date";
                    } else if (caltype.equals(getResources().getString(R.string.date))) {
                        yAx += " Time";
                    }

                    barChart.setDescription(yAx);
                } catch (JSONException e) {
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
        String yVal = values.get(e.getXIndex());

        if (caltype.equals(getResources().getString(R.string.date))) {
            int a = Integer.parseInt(xVal);
            xVal = a + " - " + (a + 1) + " AM";

            if (a > 12) {
                a -= 12;
                xVal = numberOf(a) + " - " + numberOf(a + 1) + " PM";
            } else if (a == 12) {
                xVal = "12 - 01" + " PM";
            }
        }
        tvData.setText(xVal + " : " + yVal + " " + getString(R.string.rs));

    }

    int per = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        tvData.setText("Total : " + per + " " + getString(R.string.rs));
    }

    @Override
    public void onClick(View view) {
        url = ServerCall.BASE_URL + ServerCall.REORTS + "specificRepo";
        hm = new HashMap<>();
        url += "";
        hm.put("year", spnYear.getSelectedItem().toString());
        hm.put("date", spnDate.getSelectedItem().toString());
        hm.put("type", caltype);

        if (monthAr != null && monthAr.size() != 0)
            hm.put("month", monthAr.get(spnMon.getSelectedItemPosition()) + "");
        else
            hm.put("month", "");
        new ServerCall(SpecificFragment.this, getActivity(), url, hm, 2001).execute();
    }
}