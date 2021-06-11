package com.dozen.utils.act.shares;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.utils.R;
import com.dozen.utils.bean.SharesBean;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterLocation.app_shares_line)
public class SharesLineAct extends CommonActivity implements OnChartValueSelectedListener {

    @Autowired
    SharesBean sharesBean;

    private LineChart chart;
    private TextView tvUPUP, tvDetail;
    private List<LineDataSet> lineDataSetList;

    private Typeface tfRegular;
    private Typeface tfLight;

    @Override
    protected int setLayout() {
        return R.layout.activity_shares_line;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

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

        {   // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(25f);
            yAxis.setAxisMinimum(-25f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(20f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(0f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

        initLineData();
    }

    private void initLineData() {
//        testData("300", 1);
        List<SharesBean> listBean = DataSupport.where("code = ?", sharesBean.getCode()).find(SharesBean.class);
        if (!EmptyCheckUtil.isEmpty(listBean)) {
            addData(listBean);
            addUpUpData(listBean);
        }else {
            StyleToastUtil.success("empty");
            finish();
            return;
        }

        setData();

        // redraw
        chart.invalidate();
    }

    private void testData(String code, int rank) {
        List<SharesBean> listBean = new ArrayList<>();

        for (int i = rank; i < 30 - rank; i++) {
            int val = (int) (Math.random() * 20);
            listBean.add(new SharesBean(1, i + 1, "2", code, "", val, val, val, code, ""));
        }

        addData(listBean);

    }

    private void addUpUpData(List<SharesBean> list) {

        LineDataSet set;
        ArrayList<Entry> values = new ArrayList<>();
        for (SharesBean bean : list) {
            values.add(new Entry(bean.getSid(), bean.getUpup(), bean));
        }

        // create a dataset and give it a type
        set = new LineDataSet(values, list.get(0).getCode());

        int r = (int) (Math.random() * 245) + list.get(0).getRanking();
        int g = (int) (Math.random() * 245) + list.get(0).getRanking();
        int b = (int) (Math.random() * 245) + list.get(0).getRanking();
        set.setColor(Color.rgb(r, g, b));
        set.setFillColor(Color.rgb(r, g, b));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircleHole(false);
        // customize legend entry
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);

        // text size of values
        set.setValueTextSize(9f);

        // set the filled area
        set.setDrawFilled(true);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

//        set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
//                ? LineDataSet.Mode.LINEAR
//                :  LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSetList.add(set);
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
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircleHole(false);
        // customize legend entry
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);

        // text size of values
        set.setValueTextSize(9f);

        // set the filled area
        set.setDrawFilled(true);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

//        set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
//                ? LineDataSet.Mode.LINEAR
//                :  LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSetList.add(set);
    }

    private void setData() {

        // create a data object with the data sets
        LineData data = new LineData();
        for (LineDataSet set : lineDataSetList) {
            data.addDataSet(set);
        }
        data.setValueTextColor(Color.RED);
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
        tvDetail.setText(sharesBean.getName());

    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}