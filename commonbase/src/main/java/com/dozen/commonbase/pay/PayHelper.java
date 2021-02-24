package com.dozen.commonbase.pay;

import android.app.Activity;

import com.dozen.commonbase.R;
import com.dozen.commonbase.pay.alipay.AliPayModel;
import com.dozen.commonbase.pay.alipay.AliPayTools;
import com.dozen.commonbase.pay.interfaces.OnRequestListener;
import com.dozen.commonbase.pay.utils.PayUtil;
import com.dozen.commonbase.pay.wechat.WechatModel;
import com.dozen.commonbase.pay.wechat.WechatPayTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PayHelper {

    // 支付宝参数
    private static final String Alipay_appid = "2019092067649106";
    private static final String Alipay_rsa_private = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQChUYUPbSdDnd7h+3EBxoC54XKAxm5r70KedxdFUJjQZ1Bzb3M7JFd1L6kryrjIHdyspzJMgIK7pxQoakhZFfclepoJqa7fea5biEnBuSzfPGz68Gf7j6pzhrVooQny826BBg1oOj+CuC9ai7RGzz++xArMR+i8UWOUeOZhHZTBokBLxOpZzKbt/rmdIiYhYr5DwQOpZN+YfZ7z/jwYKFKBdJJR64+gctGTWaHWU8C9D7m1+0GQ+nsdHMtJ6nFEOIFw191NMrwVxJ+/M9obgLGnvAES7Hsebn4yiKjuI9u5y22ph1KsDSXyqBXYM8M7Afm29QMdVzKaz9cC5EEX81R1AgMBAAECggEBAI00Aulm3OiFxnM+GUO5kjGiUgzAJCVcD48PpRHqI2jAFh6E3nNVMyyDJGwNjUK1SNTjoNZv4n8JF07tARjhbiyjLTPqEATSyLicChuEz+8zQB1C03HR5hwrPJ0x4LiFNiO/vXqPCcqNF/4ErS8R97UuelknVGOKRkTOsrEtKGl4Ca0qvkbpqrmMkNq1x5BTuKb9REWz/iH2X2rLPWD1M4kfUM4fd73PlEEG7tyFaSPqqXAky2fpcZryi3FR1W3UDDQOYJ86l1VaDluCxB+XdbBjPd4mnMauPmsGfcMLdEQM+xtzmOjbt2qKPw6SSz9BY4zE6cnkAXdyb6VVrLgMy10CgYEA6oqVEs+XoxE87/l7cqLDc+30fPibfqqWNgjuED5J7S5qnPfiXQUZaocNlmFA/+CfMYgI1Qgj2zwxOsa2rxurj6qXgeMk+HP3gFCNjrCJokmnguVyAwbiRfP7PUeWo8fS+yuVOjsUxQlZEy/c11TYC88dAHiqj/Vyz+6elYIdn4sCgYEAsBPp5HKIcQbY8CaVIWZ7g21ZRKAoWVs8lRIpeCOiPviM5br/y6SvBKlmPte8nwmFrKYtvlhce+GP54RX6DMVsqHEQbPFd3tKktxSuob/MbZ2kme0dirSY9a6r6Zr10p0cPkFwHiyXgS3HMmG1niwCY42vbI4LANld1MnaKWRW/8CgYEAnv5g/kaB6gZGg/tg5x5VVyJpScqSRnTHrUuybop7PPDpTw1vg1VwnFl/KYE2Of+Ai2hHbHq/K+CLEuggcWZVj+e6G0/rAQ5EKtTwkVk1hDFgAAkgl9Kz024I08rS4KYWbTRnTfk9JDEpFCuvDr4yUxcM9wPVxA9EzJZbJMj24e8CgYB+KhaqoUKQslZZZVvwLc2Ms5qGKLggptY2/meGdQn5UwEqTx6gXWUm0va3SsAVuK32srAONqQ2A76oJlUSxR/j+jN03Io+fbJGnuYMT1Sl21cEupg9H5vW+/KSj42wGuJqXJS5gAJWicxnLSOXo6yHfEZPEFddeiWHJacUPaZ1IwKBgD2Sp1FQB1vEp4gTbhpets6pH6pUnZnz3NnDONvtx7GzDs9fXlMJemdymbEapuCHiTozTXBxp58dorSGHu6WseAjRKtizaNbctSIyMuYPGzDwLLH7QmksMYdS51/yXl2hLacS0KTMdhV9J20TrErNvlUs6Vn/ugQtwjYaC4Tj3oz";
    // 微信参数
    public static String WeChat_appid = "wx40a1a16c5202db1b";
    public static String WeChat_mchId = "1502074771";
    public static String WeChat_PrivateKey = "8801a4460c729d9f1f81ef010389fe48";

    // 通用参数
    public static final String HOST = "http://api.appmans.com:8080/qmjm";
    private static final String WxNotify_Url = HOST+"/wxPayAppNotify";
    private static final String AliNotify_Url = HOST+"/aliPayAppNotify";
    //private static final String KeFu = "QQ客服:2435181686";

    public static interface IPayCallBack{
        public void onSuccess();
        public void onFailed(String err);
    }

    public static void startAliPay(Activity activity, String orderId, String name, double price, final IPayCallBack cb) {
        // 支付宝支付
        AliPayTools.aliPay(activity,
                Alipay_appid,
                true,
                Alipay_rsa_private,
                new AliPayModel(orderId, price+"", name+"-"+activity.getString(R.string.app_name), "", AliNotify_Url, PayUtil.getCurrentTime()),
                new OnRequestListener() {

                    @Override
                    public void onSuccess(String s) {
                        cb.onSuccess();
                    }

                    @Override
                    public void onError(String s) {
                        cb.onFailed(s);
                    }
                }
        );
    }

    // 微信单位是分
    public static void startWeChatPay(Activity activity, String orderId, String name, double price, final IPayCallBack cb) {
        // 微信支付
        WechatPayTools.wechatPayUnifyOrder(activity,
                WeChat_appid,
                WeChat_mchId,
                WeChat_PrivateKey,
                new WechatModel(orderId, (int)(price*100)+"", name, name+"-"+activity.getString(R.string.app_name), WxNotify_Url),
                new OnRequestListener() {

                    @Override
                    public void onSuccess(String s) {
                        cb.onSuccess();
                    }

                    @Override
                    public void onError(String s) {
                        cb.onFailed(s);
                    }
                });
    }

    /**
     * 生成订单唯一值
     *
     * @return
     */
    public static String getOrderIdByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

}
