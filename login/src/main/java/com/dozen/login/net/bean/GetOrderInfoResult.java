package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/15
 */
public class GetOrderInfoResult extends HttpResult {

    public Data data;

    public class Data{
        public int id;
        public int pay_type;
        public int goods_id;
        public String user_id;
        public String order_sn;
        public String order_price;
        public String create_time;
        public int order_status;//1 代表成功
        public String trade_order_sn;
    }
}
