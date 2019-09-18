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
import android.widget.Toast;

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
import java.util.List;

import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Models.Customer.custName;
import static patel.jay.jaijalaram.Models.Items.getItem;
import static patel.jay.jaijalaram.Panel.Admin.Reports.ChartData.setBar;

public class CustSaleFragment extends Fragment implements ServerCall.OnAsyncResponse,
        OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {

    HorizontalBarChart barChart;

    ArrayList<BarEntry> entries;
    ArrayList<String> labels, values;
    ArrayList<Integer> totalS;

    TextView tvData;
    public static Spinner spnCal;

    String caltype = "";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        tvData = view.findViewById(R.id.tvData);

        spnCal = view.findViewById(R.id.spnCal);
        spnCal.setOnItemSelectedListener(this);

        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {

            List<String> list = Arrays.asList(getResources().getStringArray(R.array.custSale));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
            spnCal.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tvData.setText("");
        caltype = spnCal.getSelectedItem().toString();
        String call = "";

        HashMap<String, String> hm = new HashMap<>();

        if (caltype.equals(getResources().getString(R.string.custFav))) {
            call = "highCustItem";
        } else if (caltype.equals(getResources().getString(R.string.custSale))) {
            call = "custSale";
        }

        String url = ServerCall.BASE_URL + ServerCall.REORTS + call;
        new ServerCall(CustSaleFragment.this, getActivity(), url, hm, 1001).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tvData.setText("");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void getResponse(String response, int flag) {

        if (response.length() == 0) {
            barChart.clear();
            toast(getActivity(), "No Data Found");
            return;
        }

        //region Report
        try {

            totalS = new ArrayList<>();
            labels = new ArrayList<>();
            values = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.REPORT);

            per = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                try {
                    int iid = jsonObject.getInt("iid");
                    values.add(getItem(getActivity(), iid).getName());
                } catch (Exception e) {
                    values.add("");

                }
                int custid = jsonObject.getInt("custid");
                int total = jsonObject.getInt("total");

                per += total;

                totalS.add(total);
                labels.add(custName(getActivity(), custid));

            }

            entries = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                float total = totalS.get(i);
                total = total * 100 / per;
                String s = String.format("%.1f", total);
                entries.add(new BarEntry(Float.parseFloat(s), i));
            }

            YAxis left = barChart.getAxisLeft();
            left.setAxisMaxValue(100f);

            barChart = setBar(barChart, entries, labels);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //endregion
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String xVal = labels.get(e.getXIndex());
        String yVal = values.get(e.getXIndex());
        int unit = totalS.get(e.getXIndex());
        String rs = "";

        if (!caltype.equals(getString(R.string.custFav)))
            rs = " " + getString(R.string.rs);
        tvData.setText(xVal + " : " + yVal + "- " + unit + rs);

    }

    int per = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        if (!caltype.equals(getString(R.string.custFav)))
            tvData.setText("Total : " + per + " " + getString(R.string.rs));
        else
            tvData.setText("");
    }
}
