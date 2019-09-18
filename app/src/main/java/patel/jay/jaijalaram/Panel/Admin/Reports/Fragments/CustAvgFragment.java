package patel.jay.jaijalaram.Panel.Admin.Reports.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Panel.Admin.Reports.ChartData;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Panel.Admin.Reports.ChartData.setBar;

public class CustAvgFragment extends Fragment implements OnChartValueSelectedListener,
        AdapterView.OnItemSelectedListener, ServerCall.OnAsyncResponse {

    HorizontalBarChart barChart;

    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    ArrayList<Integer> values;

    TextView tvData;
    public static Spinner spnYear, spnCal;

    boolean isMonth = false;

    String caltype = "";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        tvData = view.findViewById(R.id.tvData);

        spnYear = view.findViewById(R.id.spnYear);
        spnCal = view.findViewById(R.id.spnCal);

        spnCal.setOnItemSelectedListener(this);
        spnYear.setOnItemSelectedListener(this);

        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        String url = ServerCall.BASE_URL + ServerCall.LIST + "list";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("listof", "Year");
        new ServerCall(CustAvgFragment.this, getActivity(), url, hm, 101).execute();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.custAvg));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        spnCal.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        tvData.setText("");
        caltype = spnCal.getSelectedItem().toString();
        String call = "";

        spnYear.setVisibility(View.GONE);
        HashMap<String, String> hm = new HashMap<>();

        if (caltype.equals(getResources().getString(R.string.year))) {
            call = "avgCustSaleYear";

        } else if (caltype.equals(getResources().getString(R.string.month))) {

            isMonth = true;
            call = "avgCustSaleMonth";
            spnYear.setVisibility(View.VISIBLE);
            hm.put("year", spnYear.getSelectedItem().toString());
        }

        String url = ServerCall.BASE_URL + ServerCall.REORTS + call;
        new ServerCall(CustAvgFragment.this, getActivity(), url, hm, 1001).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tvData.setText("");
    }

    private void setYear(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("LIST");

            ArrayList<String> arrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String list = jsonObject.getString("Year");
                arrayList.add(list);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            spnYear.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        if (response.length() == 0) {
            barChart.clear();
            toast(getActivity(), "No Data Found");
            return;
        }

        if (flag == 101) {
            setYear(response);
        } else {
            //region Report
            try {

                values = new ArrayList<>();
                labels = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);

                JSONObject jObj = jsonArray.getJSONObject(1);
                jsonArray = jObj.getJSONArray("TOTAL");

                HashMap<String, Integer> hm = new HashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jObj = jsonArray.getJSONObject(i);
                    int year = jObj.getInt("year");
                    int total = jObj.getInt("total");

                    hm.put(year + "", total);
                }

                jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);
                jObj = jsonArray.getJSONObject(0);
                jsonArray = jObj.getJSONArray("DATA");

                for (int i = 0; i < jsonArray.length(); i++) {
                    jObj = jsonArray.getJSONObject(i);
                    int year = jObj.getInt("year");
                    int temp = hm.get(year + "");

                    int total = jObj.getInt("total") / temp;

                    values.add(total);

                    if (isMonth) {
                        labels.add(MyConst.monthName(year) + "");
                    } else {
                        labels.add(year + "");
                    }
                }

                entries = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    int total = values.get(i);
                    entries.add(new BarEntry(total, i));
                }

                YAxis left = barChart.getAxisLeft();
                float max = Collections.max(values) + 1000;
                left.setAxisMaxValue(max);

                barChart = setBar(barChart, entries, labels);

                BarData data = barChart.getData();
                data.setValueFormatter(new ChartData.SimpleValFormat());
                barChart.setData(data);

            } catch (JSONException e) {
                e.printStackTrace();
                toast(getActivity(), e.getMessage());
            }
            //endregion
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String xVal = labels.get(e.getXIndex());
        int unit = values.get(e.getXIndex());

        tvData.setText(xVal + " :- " + unit + " " + getString(R.string.rs));
    }

    @Override
    public void onNothingSelected() {
        tvData.setText("");
    }
}