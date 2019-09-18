package patel.jay.jaijalaram.Panel.Admin.Reports;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class ChartData {

    public static HorizontalBarChart setBar(HorizontalBarChart barChart, ArrayList<BarEntry> entries, ArrayList<String> labels) {
        BarDataSet bardataset = new BarDataSet(entries, "");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(labels, bardataset);
        data.setValueFormatter(new ValFormatter());

        YAxis left = barChart.getAxisLeft();
        left.setDrawGridLines(false);
        left.setAxisMinValue(0);

        barChart.setData(data);
        barChart.setDescription("");
        barChart.animateY(1000);
        barChart.setDescriptionTextSize(15);
        barChart.setDescriptionPosition(1000, 80);
        return barChart;
    }

    public static class ValFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + " %";
        }
    }

    public static class SimpleValFormat implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + "";
        }
    }
}
