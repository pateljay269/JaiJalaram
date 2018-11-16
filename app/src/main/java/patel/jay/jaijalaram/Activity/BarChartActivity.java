package patel.jay.jaijalaram.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import patel.jay.jaijalaram.R;

public class BarChartActivity extends Activity {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        graph = findViewById(R.id.graph);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //region Graph
        /*
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 10),
                new DataPoint(2, 5),
                new DataPoint(3, 20),
                new DataPoint(4, 30),
                new DataPoint(4, 0)
        });
        series1.setAnimated(true);
//        graph.addSeries(series1);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 5),
                new DataPoint(2, 4),
                new DataPoint(3, 3),
                new DataPoint(4, 2),
                new DataPoint(5, 1)
        });
        series2.setSpacing(5);
        series2.setAnimated(true);
//        graph.addSeries(series2);

        graph.getViewport().setMinX(0.4); //where to start from x-Axis
        graph.getViewport().setMaxX(10); //Upto Display in x-Axis
        graph.getViewport().setXAxisBoundsManual(true);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(5, -1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
//        graph.addSeries(series3);

        // styling
        series3.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        series3.setSpacing(50);
        series3.setAnimated(true);

        // draw values on top
        series3.setDrawValuesOnTop(true);
        series3.setValuesOnTopColor(Color.RED);
        series3.setValuesOnTopSize(30);

        // legend
        series3.setTitle("foo");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
//        graph.addSeries(series4);

        // custom label formatter to show currency "EUR"
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " ₹";
                }
            }
        });

        //endregion*/
        //endregion

        setGraph(true);
        setGraph(false);
    }

    private void setGraph(boolean is) {
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        series5.setTitle("" + is);

        if (is) {
            graph.addSeries(series5);
        } else {
            graph.getSecondScale().addSeries(series5);
            series5.setColor(Color.RED);
        }
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

}
