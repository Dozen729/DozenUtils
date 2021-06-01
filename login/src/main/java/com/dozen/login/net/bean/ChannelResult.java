package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/25
 */
public class ChannelResult extends HttpResult {

    public Data data;

    //toutiao vivo yingyongbao xiaomi oppo huawei
    public class Data{
        public CSwitch toutiao;
        public CSwitch vivo;
        public CSwitch yingyongbao;
        public CSwitch xiaomi;
        public CSwitch oppo;
        public CSwitch huawei;
        public CSwitch common;
        public CSwitch kuaishou;
        public CSwitch weibo;
        public CSwitch dongfang;
    }

    public class CSwitch{
        public String wenzi;//
        public String zhifu;//根据不同渠道通过开关控制是否需要付费
        public String icon;//开启后，定位/关心/紧急Icon入口隐藏
        public String kaiping;//根据不同渠道开关控制是否需要展示广告
        public String xinxiliu;//开启后，展示信息流广告
        public String fufei;//
        public String loginfufei;//开启后，输入手机号码需要先登录，关闭后先付费再登录
        public String tcwenzi;//首次安装弹窗文字内容由后台返回
        public String shiyong;//开启后，定位/关心页面有免费试用入口
        public String ffwenzi;//免费试用文字由后台返回
        public String changxian1;//开启后，有黑科技定位尝鲜1入口
        public String chaping;//开启后，展示插屏广告
        public String chaxun;//尾号限行/文字/万能/车辆识别使用次数由后台返回
        public String changxian2;//开启后，有黑科技定位尝鲜2入口
        public String hutui;//开启后，展示互推广告
        public String kefu;//开启后，由在线客服H5入口
        public String fenxiang;//开启后，定位/关心/紧急Icon入口隐藏
        public String smwenzi;//添加关心的人提示
    }
}
