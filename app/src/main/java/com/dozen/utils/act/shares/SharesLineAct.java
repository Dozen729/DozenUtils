package com.dozen.utils.act.shares;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.utils.R;
import com.dozen.utils.bean.SharesBean;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class SharesLineAct extends CommonActivity implements OnChartValueSelectedListener {

    private LineChart chart;
    private TextView tvUPUP, tvDetail;
    private List<LineDataSet> lineDataSetList;

    private Typeface tfRegular;
    private Typeface tfLight;

    @Override
    protected int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_shares_line;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        tvUPUP = findViewById(R.id.tv_show_upup);
        tvDetail = findViewById(R.id.tv_detail);

        chart = findViewById(R.id.line_chart);
        chart.setOnChartValueSelectedListener(this);

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    @Override
    protected void initData() {
        initChart();
    }

    private void initChart() {

        lineDataSetList = new ArrayList<>();

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);

        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(15);
        leftAxis.setAxisMinimum(-5);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(15);
        rightAxis.setAxisMinimum(-5);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        initLineData();
    }

    private void initLineData() {
        //        setData(seekBarX.getProgress(), seekBarY.getProgress());
        testData("300", 1);
        testData("900", 5);
        testData("dozen", 10);

        setData();

        // redraw
        chart.invalidate();
    }

    private void testData(String code, int rank) {
        List<SharesBean> listBean = new ArrayList<>();

        for (int i = rank; i < 30 - rank; i++) {
            int val = (int) (Math.random() * 10);
            listBean.add(new SharesBean(1, i + 1, "2", code, "", val, val, val, code, ""));
        }

        addData(listBean);

    }

    private void addData(List<SharesBean> list) {

        LineDataSet set;
        ArrayList<Entry> values = new ArrayList<>();
        for (SharesBean bean : list) {
            values.add(new Entry(bean.getSid(), bean.getRanking(), bean));
        }

        // create a dataset and give it a type
        set = new LineDataSet(values, list.get(0).getCode());

        int r = (int) (Math.random() * 245) + list.get(0).getRanking();
        int g = (int) (Math.random() * 245) + list.get(0).getRanking();
        int b = (int) (Math.random() * 245) + list.get(0).getRanking();
        set.setColor(Color.rgb(r, g, b));
        set.setFillColor(Color.rgb(r, g, b));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircleHole(false);

        lineDataSetList.add(set);
    }

    private void setData() {

        // create a data object with the data sets
        LineData data = new LineData();
        for (LineDataSet set : lineDataSetList) {
            data.addDataSet(set);
        }
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);

        SharesBean sharesBean = (SharesBean) e.getData();
        tvUPUP.setText(sharesBean.getUpup() + "");
        tvDetail.setText(sharesBean.getDetail());

    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}