package com.dozen.commonbase.http;


import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.bean.UpdateEvent;
import com.dozen.commonbase.utils.MyLog;


/**
 * 网络请求。加入签名
 */

public class HttpUtils extends BaseHttpUtils {

    //是否需要签名
    private boolean isUseSign = true;
    //签名秘钥
    private final String DEF_SECRET = "demo.tp-shop.cn";

    public HttpUtils(String url, CallBack callBack, Class cls, Object tag) {
        super(url, callBack, cls, tag);
    }

    public HttpUtils(String url, CallBack callBack, Decoder decoder, Object tag) {
        super(url, callBack, decoder, tag);
    }

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        super.addParam(key, value);
    }

    /**
     * 发起post请求
     */
    public void post() {
        addSign();
        super.post();
    }

    /**
     * 发起put请求
     */
    public void put() {
        addSign();
        super.put();
    }

    /**
     * 发起get请求
     */
    public void get() {
        addSign();
        super.get();
    }

    /**
     * Http请求拦截特殊处理回调
     * @param info
     * @param tag
     */
    @Override
    public void onRequestedBack(ResultInfo info, Object tag) {
        //增加对 code=2,codeEx=102005 进行提示的特殊处理.
        //如extendData 有数据,按extendData的数据往下

        if(info==null || CommonConstant.TOKEN_INVALID_CODE.equals(info.getCode()) ){
            MyLog.d("token已经失效");
//            APP.getApplication().sendBroadcast(new Intent(APPContant.TOKEN_INVALID));
            UpdateEvent event = new UpdateEvent();
            event.isTokenLose = true;
//            EventBus.getDefault().post(event);
        }
        super.onRequestedBack(info, tag);
    }

    /**
     * 添加签名，防止恶意攻击接口使用
     */
    public void addSign() {
        /*HashMap<String, String> params = getParam();
        String sign = Sign.getSignature(params, secret);
        super.addParam("sign", sign);
        super.addParam("timestamp", System.currentTimeMillis()+"");*/
    }

}
