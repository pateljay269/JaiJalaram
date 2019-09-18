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
public class CustomRepoFragment extends Fragment implements OnChartValueSelectedListener,
        ServerCall.OnAsyncResponse, AdapterView.OnItemSelectedListener {

    HorizontalBarChart barChart;

    ArrayList<BarEntry> entries;
    ArrayList<String> labels, values;
    ArrayList<Integer> totalS, month;

    TextView tvData;
    public static Spinner spnCal, spnYear;

    String caltype = "";
    View view;
    boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mis_reports, container, false);

        tvData = view.findViewById(R.id.tvData);

        spnCal = view.findViewById(R.id.spnCal);
        spnCal.setOnItemSelectedListener(this);

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.someCal));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        spnCal.setAdapter(adapter);

        spnYear = view.findViewById(R.id.spnYear);
        spnYear.setOnItemSelectedListener(this);

        spnYear.setVisibility(View.VISIBLE);
        barChart = view.findViewById(R.id.barchart1);
        barChart.setOnChartValueSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        caltype = spnCal.getSelectedItem().toString();

        HashMap<String, String> hm = new HashMap<>();
        String url = "";

        switch (adapterView.getId()) {
            case R.id.spnCal:
                isFirst = true;
                url = ServerCall.BASE_URL + ServerCall.LIST + "list";
                hm.put("listof", caltype);
                new ServerCall(CustomRepoFragment.this, getActivity(), url, hm, 101).execute();
                break;

            case R.id.spnYear:
                if (!isFirst) {
                    String num = spnYear.getSelectedItem().toString();
                    url = ServerCall.BASE_URL + ServerCall.REORTS + "otherRepo";
                    hm.put("type", caltype);
                    if (caltype.equals(getString(R.string.month))) {
                        num = month.get(spnYear.getSelectedItemPosition()) + "";
                    }
                    hm.put("num", num);
                    new ServerCall(CustomRepoFragment.this, getActivity(), url, hm, 1001).execute();
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tvData.setText("");
    }

    private void setSpn(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("LIST");

            ArrayList<String> arrayList = new ArrayList<>();

            if (caltype.equals(getString(R.string.month)))
                month = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                int list = jsonObject.getInt(caltype);
                if (caltype.equals(getString(R.string.month))) {
                    arrayList.add(MyConst.monthName(list));
                    month.add(list);
                } else {
                    arrayList.add(MyConst.numberOf(list));
                    spnYear.setPadding(10, 0, 10, 0);
                }
            }

            isFirst = false;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            spnYear.setAdapter(adapter);
        } catch (JSONException e) {
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

        if (flag == 101) {
            setSpn(response);
        } else if (flag == 1001) {
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
                    int type = jsonObject.getInt("type");
                    int total = jsonObject.getInt("total");

                    per += total;

                    if (caltype.equals(getResources().getString(R.string.year))) {
                        labels.add(MyConst.monthName(type));

                    } else {
                        labels.add(type + "");
                    }

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
                if (caltype.equals(getResources().getString(R.string.year))) {
                    yAx += " Month";
                } else if (caltype.equals(getResources().getString(R.string.month))) {
                    yAx += " Date";
                } else if (caltype.equals(getResources().getString(R.string.date))) {
                    yAx += " Time";
                }

                barChart.setDescription(yAx);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //endregion
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

}