package com.dozen.commonbase.http;

import android.text.TextUtils;

import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.GsonUtils;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.VersionInfoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http 连接类
 */

public class HttpClient implements Callback {

    public static final int POST = 1;
    public static final int GET = 2;
    public static final int PUT = 3;
    public static final int DEL = 4;
    public final static int REQUEST_TIMEOUT = 30;

    // public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
    // public static final MediaType STREAM = MediaType.parse("application/octet-stream");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");//Content-Type charset=utf-8   application/json
    private Object tag;
    public static OkHttpClient okHttpClient;
    private Call call;
    private String mUrl;
    public boolean needCompress = true;//需要压缩

    public HttpClient(String url, Object tag) {
        this.tag = tag;
        initOkHttpClient();
        mUrl = url;
    }

    /**
     * OkHttpClient初始化
     */
    private synchronized static void initOkHttpClient(){
        if (okHttpClient != null)
            return;
        if (HttpConstant.NEED_PROXY){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS).build();
        }else {
            okHttpClient = new OkHttpClient.Builder()
                    .proxySelector(new ProxySelector() {//设置跳过代理
                        @Override
                        public List<Proxy> select(URI uri) {
                            return Collections.singletonList(Proxy.NO_PROXY);
                        }

                        @Override
                        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                        }
                    })
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS).build();
        }

    }

    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, File> fileParams = new HashMap<>();

    /**
     * 添加请求参数
     * @param name
     * @param value
     */
    public void addParam(String name, String value) {
        params.put(name, value);
    }

    public HashMap<String, String> getParam() {
        return params;
    }

    /**
     * 添加一个上传文件
     * @param name
     * @param path
     */
    public void addFileParam(String name, String path) {
        addParam(name, new File(path));
    }

    /**
     * 添加多个上传文件
     * @param name
     * @param paths
     */
    public void addFileParam(String name, List<String> paths) {
        for(String path : paths)
            addParam(name, new File(path));
    }

    /**
     * 添加一个上传文件
     * @param name
     * @param file
     */
    public void addParam(String name, File file) {
        fileParams.put(name, file);
    }

    public void post() {
        enqueue(POST);
    }

    public void get() {
        enqueue(GET);
    }

    public void put() {
        enqueue(PUT);
    }

    public void del(){
        enqueue(DEL);
    }

    //json数据 request
    public Request buildRequest(int method) {
        Request request = null;
        Request.Builder builder2 = new Request.Builder();
        addRequestHeader(builder2);
        if(method == POST) {
            String content = params.get("json");
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            RequestBody requestBody = FormBody.create(JSON, content == null ? "" : content);
            request = builder2.url(mUrl).post(requestBody).build();
            MyLog.e("post 请求URL：" + request.url().url().toString());
            MyLog.e("post 请求参数：" + content);
        } else if(method == PUT){
            String content = params.get("json");
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            RequestBody requestBody = FormBody.create(JSON, content == null ? "" : content);
            request = builder2.url(mUrl).put(requestBody).build();
            MyLog.e("put 请求URL：" + request.url().url().toString());
            MyLog.e("put 请求参数：" + content);
        }else if(method == GET){
            request = builder2.url(handleGetRequestParams()).build();;
        }else if (method == DEL){
            String content = params.get("json");
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            RequestBody requestBody = FormBody.create(JSON, content == null ? "" : content);
            request = builder2.url(mUrl).delete(requestBody).build();
        }
        return request;
    }

    /**
     * get请求 参数处理、拼接
     * @return
     */
    private String handleGetRequestParams(){
        if(mUrl.contains("?")) {
            for(String key:params.keySet()){
                mUrl +=  "&" +key + "=" + encodeString(params.get(key));
            }
        } else {
            mUrl  += "?";
            for(String key:params.keySet()){
                mUrl += (key + "=" + encodeString(params.get(key))+ "&");
            }
            mUrl = mUrl.substring(0, mUrl.length() - 1);
        }
        MyLog.e("get 请求URL："+mUrl);
        return mUrl;
    }

    /**
     * utf-8编码
     * @param encodeStr
     * @return
     */
    private String encodeString(String encodeStr){
        try {
            return  URLEncoder.encode(encodeStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 上传文件、表单数据
     * @param method
     * @return
     */
    public Request buildUploadFileRequest(int method) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Request.Builder builder2 = new Request.Builder();
        addRequestHeader(builder2);
        for(String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        for (String key : fileParams.keySet()) {
            File file = fileParams.get(key);
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), file);
            builder.addFormDataPart(key, file.getName(), fileBody);
        }
        RequestBody requestBody = builder.build();
        MyLog.d("post 请求URL：" + mUrl);
        if(method == PUT) {
            return builder2.url(mUrl).put(requestBody).build();
        } else {
            return builder2.url(mUrl).post(requestBody).build();
        }
    }

    /**
     * 添加请求header
     * @param builder
     */
    private void addRequestHeader(Request.Builder builder){
        String token = params.remove("token");
        if(!TextUtils.isEmpty(token)) {
            builder.addHeader("authorization", token);
        }else {
            builder.addHeader("authorization", SPUtils.getString(APPBase.getApplication(), CommonConstant.USER_TOKEN,""));
        }
        builder.addHeader(HttpConstant.HTTP_TYPE, AppUtils.getPhoneBrand());
        builder.addHeader(HttpConstant.HTTP_Version, VersionInfoUtils.getVersionName(APPBase.getApplication()));
        builder.addHeader(HttpConstant.HTTP_Channel,HttpConstant.HTTP_Channel_Value);
    }

    /**
     * 获取文件MimeType
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    /**
     * 请求 - 回调
     * @param method
     */
    public void enqueue(int method) {
        Request request = null;
        if(fileParams.size() > 0) {
            request = buildUploadFileRequest(method);
        } else {
            request = buildRequest(method);
        }
        Headers headers = request.headers();
        MyLog.d("--请求头header--"+ GsonUtils.GsonString2(headers.toMultimap()));
        call = okHttpClient.newCall(request);
        call.enqueue(this);
    }

    //源头失败返回
    @Override
    public void onFailure(Call call, IOException e) {
        MyLog.e("onFailure " + e.getMessage());
        if(null!= e.getMessage() && e.getMessage().contains("timeout")){
            httpCallback.onFailed(HttpCode.REQUEST_TIMEOUT, "网络异常", tag);
        }else if(e instanceof SocketException){
            httpCallback.onFailed(HttpCode.REQUEST_TIMEOUT, "请求超时", tag);
        }else if(e instanceof UnknownHostException){
            httpCallback.onFailed(HttpCode.REQUEST_TIMEOUT, "网络异常", tag);
        }else if(e instanceof InterruptedIOException){
            httpCallback.onFailed(HttpCode.REQUEST_TIMEOUT, "网络异常", tag);
        }else{
//            httpCallback.onFailed(HttpCode.NET_ERROR, e.toString(), tag);
            httpCallback.onFailed(HttpCode.NET_ERROR, "系统异常:"+e.getClass().getSimpleName(), tag);
        }
    }

    //源头成功返回
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()) {
            String result = response.body().string();
            MyLog.d("http Successful result:" + result);
            httpCallback.onSuccess(result, tag);
        } else {
            MyLog.d("onResponse code:" + response.code() + "  message:" + response.message());
            httpCallback.onFailed(response.code(), HttpCode.getCauseByHttpCode(response.code()), tag);
        }
        deleteLocalUploadImgs();
    }

    /**
     * 图片上传成功，删除本地压缩的文件
     */
    private void deleteLocalUploadImgs(){
        MyLog.d("是否需要压缩: " + needCompress);
        if(fileParams.size() > 0 && needCompress){
//            XutilsBaseHttp.deleteImageFile();
//            LubanUtil.getInstance().deleteImageFile();
        }
    }

    /**
     * 取消请求
     */
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    private HttpCallback httpCallback;
    public void setHttpCallback(HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
    }

    public interface HttpCallback {
        void onSuccess(String value, Object tag);
        void onFailed(int code, String msg, Object tag);
    }
}
