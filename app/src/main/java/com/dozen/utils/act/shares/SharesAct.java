package com.dozen.utils.act.shares;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.ThreadUtils;
import com.dozen.utils.R;
import com.dozen.utils.bean.SharesBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Route(path = ARouterLocation.app_shares)
public class SharesAct extends CommonActivity {

    private List<SharesBean> sharesBeanList;

    @Override
    protected int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_shares;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnSetClick(R.id.shares_jsoup);
    }

    @Override
    protected void initData() {
        sharesBeanList = new ArrayList<>();
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
            }
        }
    };

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
                loadSharesDetail(0);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    private void loadSharesDetail(int index) {
        ThreadUtils.executeByCached(new ThreadUtils.Task<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = "http://hq.sinajs.cn/list=";

                for (int i = 0; i < sharesBeanList.size(); i++) {
                    Request request = new Request.Builder()
                            .url(url+sharesBeanList.get(i).getCode())
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    MyLog.d("" + response.body().string());
                }


                return null;
            }

            @Override
            public void onSuccess(Object result) {
                loadSharesDetail(index+1);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

}