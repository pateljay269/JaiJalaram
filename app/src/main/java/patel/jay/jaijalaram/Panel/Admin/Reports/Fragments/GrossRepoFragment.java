package patel.jay.jaijalaram.Panel.Admin.Reports.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Collections;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Categorys;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.numberOf;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Panel.Admin.Reports.ChartData.setBar;

public class GrossRepoFragment extends Fragment implements ServerCall.OnAsyncResponse,
        AdapterView.OnItemSelectedListener, OnChartValueSelectedListener, CompoundButton.OnCheckedChangeListener {

    HorizontalBarChart barChart;
//    BarChart barChart;

    String caltype = "";
    public static Spinner spnCal;
    TextView tvData;
    CheckBox chkDisName;

    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    ArrayList<Integer> values;
    ArrayList<Float> totalS;

    public static boolean isCal = true;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        chkDisName = view.findViewById(R.id.chkDisName);

        tvData = view.findViewById(R.id.tvData);
        spnCal = view.findViewById(R.id.spnCal);

        spnCal.setOnItemSelectedListener(this);

        chkDisName.setOnCheckedChangeListener(this);

        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            String arr[] = null;
            if (isCal)
                arr = getResources().getStringArray(R.array.calType);
            else
                arr = getResources().getStringArray(R.array.itemSale);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr);
            spnCal.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        caltype = spnCal.getSelectedItem().toString();

        String type = caltype;
        chkDisName.setVisibility(View.GONE);
        if (caltype.equals(getResources().getString(R.string.itemwise))
                || caltype.equals(getResources().getString(R.string.itemQty))) {
            type = "iid";
            chkDisName.setVisibility(View.VISIBLE);
        } else if (caltype.equals(getResources().getString(R.string.catwise))) {
            type = "cId";
        }

        barChart.clear();
        tvData.setText("");

        String url = ServerCall.BASE_URL + ServerCall.REORTS + "gross";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("type", type);
        new ServerCall(GrossRepoFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.SELECT:
                if (response.length() == 0) {
                    barChart.clear();
                    toast(getActivity(), "No Data Found");
                    return;
                }

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
                        int price = jsonObject.getInt("price");
                        int total = jsonObject.getInt("total");

                        switch (caltype.toUpperCase()) {
                            case "MONTH":
                                type = MyConst.monthName(Integer.parseInt(type));
                                break;

                            case "DAY":
                                type = MyConst.dayName(Integer.parseInt(type) + 1);
                                break;

                            case "WEEK":
                                type = MyConst.weekdaySuper(Integer.parseInt(type));
                                break;

                            case "ITEM WISE":
                            case "ITEM QTY":
                                if (isName) {
                                    Items items = Items.getItem(getActivity(), Integer.parseInt(type));
                                    type = items.getName();
                                }
                                break;
                        }

                        if (caltype.equals(getResources().getString(R.string.itemQty))) {
                            values.add(total);
                            per += total;
                        } else {
                            per += price;
                            values.add(price);
                        }

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

                    if (isCal)
                        left.setAxisMaxValue(100f);
                    else {
                        float max = Collections.max(totalS) + 5;
                        left.setAxisMaxValue(max);
                    }

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
        String rs = "₹";

        //region Set X Value
        if (caltype.equals(getString(R.string.itemwise)) ||
                caltype.equals(getString(R.string.itemQty))) {
            try {
                Items items = Items.getItem(getActivity(), Integer.parseInt(xVal));
                xVal = items.getName();
            } catch (Exception ex) {
                xVal = labels.get(e.getXIndex());
            }

            if (caltype.equals(getResources().getString(R.string.itemQty)))
                rs = "Unit";
        } else if (caltype.equals(getString(R.string.itemtime))) {
            int a = Integer.parseInt(xVal);
            xVal = a + " - " + (a + 1) + " AM";

            if (a > 12) {
                a -= 12;
                xVal = numberOf(a) + " - " + numberOf(a + 1) + " PM";
            } else if (a == 12) {
                xVal = "12 - 01" + " PM";
            }
        } else if (caltype.equals(getResources().getString(R.string.catwise))) {
            try {
                xVal = Categorys.getCatName(getActivity(), Integer.parseInt(xVal));
            } catch (Exception ex) {
                xVal = labels.get(e.getXIndex());
            }
        }

        //endregion
        tvData.setText(xVal + " :- " + yVal + " " + rs);
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
        new ServerCall(GrossRepoFragment.this, getActivity(), url, hm, MyConst.SELECT).execute();
    }
}