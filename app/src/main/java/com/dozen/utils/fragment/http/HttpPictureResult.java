package com.dozen.utils.fragment.http;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/12
 */
public class HttpPictureResult extends HttpResult {

    public Data[] data;

    public static class Data{

        public String _id;
        public String author;
        public String desc;
        public String url;

    }

}
