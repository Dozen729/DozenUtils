package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/10/15
 */
public class PullGoodsResult extends HttpResult {

    public Data data;

    public static class Data {
        public String url;//支付链接
        public Order order;
        public String http;

        public static class Order {
            public String goods_id;
            public String pay_type;
            public String channel;
            public String device;
            public String version;
            public String uuid;
            public String user_id;
            public String ip;
            public String order_sn;
            public String order_price;
            public String id;//商品id
            public String product_id;
        }
    }
}
