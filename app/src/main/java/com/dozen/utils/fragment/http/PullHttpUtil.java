package com.dozen.utils.fragment.http;

import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.HttpUtils;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/12
 */
public class PullHttpUtil {

    public static void pullPicture(String page,CallBack callBack,Object tag){
        String url="https://gank.io/api/v2/data/category/Girl/type/Girl/page/"+page+"/count/30";

        HttpUtils httpUtils = new HttpUtils(url,callBack,HttpPictureResult.class,tag);
        httpUtils.get();

    }

}
