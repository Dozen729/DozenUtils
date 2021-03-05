package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/10/15
 */
public class GoodsListResult extends HttpResult {

    public Data[] data;

    public static class Data {
        public String id;       // 3,
        public String product_id;       // "",
        public String title;       // "月卡",
        public String price;       // "6.00",
        public String dis_price;       // "1.00",
        public String dis_time;       // "2021-03-03 11:05:32",
        public String day;       // 30,
        public String create_time;       // "2020-08-19 10:09:35"
    }
}
