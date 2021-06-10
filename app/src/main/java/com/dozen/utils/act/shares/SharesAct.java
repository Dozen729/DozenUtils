package com.dozen.utils.act.shares;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.utils.ThreadUtils;
import com.dozen.commonbase.utils.TimeUtil;
import com.dozen.utils.R;
import com.dozen.utils.adapter.SharesAdapter;
import com.dozen.utils.bean.SharesBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Route(path = ARouterLocation.app_shares)
public class SharesAct extends CommonActivity {

    private List<SharesBean> sharesBeanList;
    private SharesAdapter sharesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_shares;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnSetClick(R.id.shares_jsoup);
        btnSetClick(R.id.shares_jsoup_save);
        btnSetClick(R.id.shares_jsoup_show);
        recyclerView = findViewById(R.id.shares_recycler_view);
    }

    @Override
    protected void initData() {
        sharesBeanList = new ArrayList<>();

        sharesAdapter = new SharesAdapter(sharesBeanList);
        recyclerView.setAdapter(sharesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (CommonUtils.isFastClick()) {
                    String code = sharesBeanList.get(position).getCode();
                    code = code.substring(2);
                    AppUtils.copyContent(code);
                }
            }
        });
        sharesAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ARouter.getInstance().build(ARouterLocation.app_shares_line).withObject("sharesBean", sharesBeanList.get(position)).navigation();
            }
        });
        sharesAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                ARouter.getInstance().build(ARouterLocation.app_url_show)
                        .withString("url_show", "https://xueqiu.com/S/" + sharesBeanList.get(position).getCode() + "?from=status_stock_match").navigation();

                return true;
            }
        });
    }

    private void btnSetClick(int id) {
        Button btn = findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.shares_jsoup:

                    loadNetShares();
                    break;
                case R.id.shares_jsoup_save:
                    if (CommonUtils.isContinueTwoClick()) {
                        DataSupport.saveAll(sharesBeanList);
                        StyleToastUtil.success("D");
                    }
                    break;
                case R.id.shares_jsoup_show:
                    showLitePalData();
                    break;
            }
        }
    };

    private void showLitePalData() {
        List<SharesBean> sbl = DataSupport.findAll(SharesBean.class);
        sharesAdapter.setList(sbl);
        sharesAdapter.notifyDataSetChanged();
    }

    private void loadNetShares() {
        ThreadUtils.executeByCached(new ThreadUtils.Task<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                try {
                    //从一个URL加载一个Document对象。
                    Document doc = Jsoup.connect("https://www.taoguba.com.cn/stock/moreHotStock").get();
                    //选择节点
                    Elements elements = doc.select("div.hot_hsnr_l");
                    Elements shares = elements.select("tbody").select("tr");

                    for (Element row : shares) {
                        Elements cols = row.children();
                        MyLog.d(cols.get(0).select("a").text());
                        String code = cols.get(0).select("a").text();
                        if (!EmptyCheckUtil.isEmpty(code)) {
                            SharesBean sharesBean = new SharesBean();
                            sharesBean.setCode(code);
                            sharesBeanList.add(sharesBean);
                        }
                    }

                } catch (Exception e) {
                    MyLog.d(e.toString());
                }

                return null;
            }

            @Override
            public void onSuccess(Object result) {
                MyLog.d("onSuccess");
                loadSharesDetail();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    private void loadSharesDetail() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String url = "http://hq.sinajs.cn/list=";

                    StringBuilder sb = new StringBuilder();

                    for (SharesBean bean : sharesBeanList) {
                        sb.append(bean.getCode()).append(",");
                    }

                    Request request = new Request.Builder()
                            .url(url + sb.toString())
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
//                MyLog.d("" + response.body().string());

                    String[] callList = response.body().string().split(";");

                    int sharesSize = DataSupport.count(SharesBean.class);

                    for (int i = 0; i < callList.length; i++) {

                        String scode = callList[i];

                        String[] scList = scode.split(",");

                        float a = Float.parseFloat(scList[2]);//昨收
                        DecimalFormat df = new DecimalFormat("#,##0.000");
                        a = Float.parseFloat(df.format(a));
                        float b = Float.parseFloat(scList[3]);//今收
                        b = Float.parseFloat(df.format(b));
                        float upup = ((b - a) / a) * 100;
                        DecimalFormat updf = new DecimalFormat("#,##0.00");
                        upup = Float.parseFloat(updf.format(upup));
                        MyLog.d(scList[0]);

                        sharesBeanList.get(i).setSid(sharesSize + i);
                        sharesBeanList.get(i).setTime(TimeUtil.getCurrentData());
                        sharesBeanList.get(i).setRanking(i);
                        sharesBeanList.get(i).setName(scList[0].split("\"")[1]);
                        sharesBeanList.get(i).setPrice(a);
                        sharesBeanList.get(i).setUpup(upup);
                        sharesBeanList.get(i).setDetail(scode);
                    }


                } catch (Exception e) {
                    MyLog.d(e.toString());
                } finally {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            loadComplete();
        }
    };

    private void loadComplete() {
        MyLog.d("no");
        sharesAdapter.setList(sharesBeanList);
        sharesAdapter.notifyDataSetChanged();
    }

}