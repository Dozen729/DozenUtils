package com.dozen.commonbase.http.rawhttp;

import android.text.TextUtils;

import com.dozen.commonbase.utils.MyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/7/7
 */
public class HttpURLBuild {
    private final String TAG = "HttpURLBuild";

    private String requestUrl = "";
    private final String requestMethod;


    final String PREFIX = "--";                            //前缀
    final String BOUNDARY = UUID.randomUUID().toString();  //边界标识
    final String CONTENT_TYPE = "multipart/form-data";     //内容类型
    final String LINE_END = "\r\n";                        //换行
    private static final String CHARSET = "utf-8";                         //编码格式

    //    private HttpClientUtils.OnRequestCallBack callBackListener;
    private HttpClientUtils.OnRequestCallBackBase callBackListenerBase;

    public HttpURLBuild(String requestUrl, String requestMethod) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;

    }


    HashMap<String, String> headMap = new HashMap<>();
    HashMap<String, File> fileMap = new HashMap<>();

    public HttpURLBuild setHead(String key, String value) {
        headMap.put(key, value);
        return this;
    }

    public HttpURLBuild addFile(String key, File file) {
        fileMap.put(key, file);
        return this;
    }


    Map<String, String> bodyMap = new HashMap<>();
    String rawJson = "";

    public HttpURLBuild addParameter(String key, String value) {
        bodyMap.put(key, value);
        return this;
    }

    public HttpURLBuild addParameter(Map<String, String> parameterMap) {
        bodyMap.putAll(parameterMap);
        return this;
    }

    public HttpURLBuild addRawData(String rawJson) {
        this.rawJson = rawJson;
        return this;
    }

    public HttpURLBuild setCallBack(HttpClientUtils.OnRequestCallBack callBackListener) {
        if (callBackListener == null) {
            return this;
        }
        this.callBackListenerBase = new HttpClientUtils.OnRequestCallBackBase(callBackListener);
        return this;
    }

    StringBuilder builder = new StringBuilder();

    //调用这个方法就开始正真的网络请求了 之前都是参数的设置,你别管我,让我看看
    public HttpURLBuild build() {

        if (isCancel) {
//            LogUtil.e(TAG, "请求已取消: " + requestUrl);
            return null;
        }
        if (TextUtils.isEmpty(requestUrl)) {
            throw new NullPointerException("请求地址不能为空");
        }
        if (callBackListenerBase == null) {
            throw new IllegalArgumentException("回调地址不能为空,请检查setCallBack()是否调用");
        }
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {

                // 现将参数写入缓冲区等待发送
                DataOutputStream out = null;

                boolean isSuccess = false;
                String message;
                InputStream inputStream = null;
                ByteArrayOutputStream baos = null;
                try {
                    if ("get".equalsIgnoreCase(requestMethod) && bodyMap.size() > 0) {

                        requestUrl += "?";
                        for (String key : bodyMap.keySet()) {
                            requestUrl += key;
                            requestUrl += bodyMap.get(key);
                        }
                    }
                    URL url = new URL(requestUrl);
                    final HttpURLConnection httpURLConnection;
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(60 * 1000);
                    // 设定请求的方法为"POST"，默认是GET
                    httpURLConnection.setReadTimeout(60 * 1000);
                    if ("get".equalsIgnoreCase(requestMethod)) {
                        httpURLConnection.setRequestMethod("GET");
                    } else if ("post".equalsIgnoreCase(requestMethod)) {
                        httpURLConnection.setRequestMethod("POST");
                        // 设置是否向httpUrlConnection输出，如果是post请求，参数要放在http正文内，因此需要设为true, 默认是false;
                        httpURLConnection.setDoOutput(true);//这里设置成true就代表是post请求
                    }
                    /*
                     * 当我们要获取我们请求的http地址访问的数据时就是使用connection.getInputStream().read()方式时我们就需要setDoInput(true)，
                     * 根据api文档我们可知doInput默认就是为true。我们可以不用手动设置了，如果不需要读取输入流的话那就setDoInput(false)。
                     * 当我们要采用非get请求给一个http网络地址传参 就是使用connection.getOutputStream().write() 方法时我们就需要setDoOutput(true), 默认是false
                     */
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    httpURLConnection.setDoInput(true);
                    //是否使用缓存
                    httpURLConnection.setUseCaches(false);
//                    httpURLConnection.setInstanceFollowRedirects(true);  刚加上的 瞎试的
                    httpURLConnection.setRequestProperty("accept", "*/*");//设置接收数据的格式
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");//设置连接方式为长连接
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");//设置编码字符格式
                    //设置发送数据的格式
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");//关键代码post的表单提交 application/x-www-form-urlencoded
                    builder.append("请求地址为-> \n");
                    builder.append(requestUrl + "\n");
                    //不进行转码的gson
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    builder.append("请求头为->\n" + gson.toJson(headMap) + "\n");
                    addHeads(httpURLConnection);
                    if ("post".equalsIgnoreCase(requestMethod)) {
                        out = new DataOutputStream(httpURLConnection.getOutputStream());
                        //添加请求体 如果不传file的话是这种方式进去
                        if (fileMap.size() == 0) {
                            String params = addBodyParams(out);
                        }
                        addRawData(out);
                        if (fileMap.size() > 0) {
                            addFileData(out);
                        }
//                        out.flush();
                    }
                    MyLog.d(TAG + "请求参数为-->" + builder.toString());


                    // 发送请求params参数
//                    out.flush();
                    //这个时候才是真正的将参数发送给服务器的时候  可以不连接getInputStream的时候会自动连接
                    httpURLConnection.connect();
//                    int contentLength = httpURLConnection.getContentLength();
                    if (httpURLConnection.getResponseCode() == 200) {
                        // 会隐式调用connect()
                        inputStream = httpURLConnection.getInputStream();
                        baos = new ByteArrayOutputStream();
                        int readLen;
                        byte[] bytes = new byte[1024];
                        while ((readLen = inputStream.read(bytes)) != -1) {
                            baos.write(bytes, 0, readLen);
                        }
                        String backStr = baos.toString();
                        message = backStr;
                        MyLog.d(TAG + "请求结果为-->" + message);
                        isSuccess = true;


                    } else {
                        message = "请求失败 code:" + httpURLConnection.getResponseCode();
                    }
                } catch (IOException e) {
                    message = e.getMessage();
                    if (!isCancel) {
                        callBackListenerBase.onError(message);
                    }
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        message = e.getMessage();
                        if (!isCancel) {
                            callBackListenerBase.onError(message);
                        }
                        e.printStackTrace();
                    }
                }

                if (isCancel) {
//                    LogUtil.e(TAG, "请求已取消:数据未回调 " + requestUrl);
                    return;

                } else {
                    if (isSuccess) {
                        callBackListenerBase.onSuccess(message);
                    } else {
                        callBackListenerBase.onError(message);
                    }
                }

            }
        });
        return this;
    }

    private void addFileData(DataOutputStream out) throws IOException {

        //这个是普通的参数
        if (bodyMap.size() > 0) {
            StringBuilder strParams = getStrParams(bodyMap);
            out.write(strParams.toString().getBytes(CHARSET));
            out.flush();
            builder.append(strParams);

        }
        //这个是文件的参数
        if (fileMap != null && fileMap.size() > 0) {
            //文件上传
            getStrParamsFile(out);
            MyLog.d(TAG + "文件请求参数为:" + builder.toString());
        }
    }

    private void getStrParamsFile(DataOutputStream out) throws IOException {
        StringBuilder fileSb = new StringBuilder();
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            fileSb.append(PREFIX)
                    .append(BOUNDARY)
                    .append(LINE_END)
                    /**
                     * 这里重点注意： name里面的值为服务端需要的key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    .append("Content-Disposition: form-data; name=\"image\"; filename=\"" + fileMap.get(fileEntry.getKey()).getName() + "\"" + LINE_END)
                    .append("Content-Type: image/jpg" + LINE_END) //此处的ContentType不同于 请求头 中Content-Type
                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                    .append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
            out.write(fileSb.toString().getBytes(CHARSET));
            out.flush();
            InputStream is = new FileInputStream(fileEntry.getValue());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            is.close();
            out.writeBytes(LINE_END);
        }
        //请求结束标志
        out.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes(CHARSET));
        out.flush();
        builder.append(fileSb);
    }


    /**
     * 当传文件的时候的其他的普通参数设置
     * 对post参数进行编码处理
     */
    private StringBuilder getStrParams(Map<String, String> strParams) {
        StringBuilder strSb = new StringBuilder();
        for (Map.Entry<String, String> entry : strParams.entrySet()) {
            strSb.append(PREFIX)
                    .append(BOUNDARY)
                    .append(LINE_END)
                    .append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END)
                    .append("Content-Type: text/plain; charset=" + CHARSET + LINE_END)
                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                    .append(LINE_END)// 参数头设置完以后需要两个换行，然后才是参数内容
                    .append(entry.getValue())
                    .append(LINE_END);
        }
        return strSb;
    }

    /**
     * raw请求方式的参数设置
     *
     * @param out
     * @throws IOException
     */
    private void addRawData(DataOutputStream out) throws IOException {
        if (!TextUtils.isEmpty(rawJson)) {
            out.write(rawJson.getBytes(CHARSET));
            builder.append("raw参数Wie" + rawJson + "\n");
        }
    }

    /**
     * 这个是普通的表单数据用到的  如果有传递文件的就不用这个了  用的是下面那种拼接方式的
     *
     * @param out
     * @return
     * @throws IOException
     */
    @NotNull
    private String addBodyParams(DataOutputStream out) throws IOException {//别动了
        String params = "";

        if (bodyMap.size() > 0) {
            for (String key : bodyMap.keySet()) {
                params = params + key + "=" + bodyMap.get(key) + "&";

            }
            params = params.substring(0, params.length() - 1);

            //需要进行URL的转码  这是我看着retorfit返回的结果自己写的转码 也不知道些全了没
            //对一些特殊的字符需要进行转码操作,下面是我发现需要转码的字符,有没有其他的还有待检验
            params = params
                    .replace("+", "%2B")
                    .replace("/", "%2F")
                    .replace("\\", "%5C")
                    .replace("\"", "%22")
                    .replace("{", "%7B")
                    .replace("|", "%7C")
                    .replace("}", "%7D")
                    .replace(":", "%3A");


            builder.append("请求参数为 ->\n" + params + "\n");
            //使用这个可以解决中文乱码的问题  使用 out.writeBytes();会有中文乱码的问题
            out.write(params.getBytes(CHARSET));

        }
        return params;
    }

    private void addHeads(HttpURLConnection httpURLConnection) {
        //添加head请求头
        for (String key : headMap.keySet()) {
            //如果是文件类型的后面追加一个边界值
            if (TextUtils.equals(key, "Content-Type") && TextUtils.equals(headMap.get(key), CONTENT_TYPE)) {
                httpURLConnection.setRequestProperty(key, headMap.get(key) + ";boundary=" + BOUNDARY);
            } else {
                httpURLConnection.setRequestProperty(key, headMap.get(key));
            }

        }
    }


    private volatile boolean isCancel = false;

    /**
     * 取消网络请求,其实并不是真正的取消
     * 1.如果还未发起网络请求就不发起网络请求
     * 2.如果已发起网络情趣,则不回调成功的方法,即不处理返回的数据
     */
    public void cancel() {
        isCancel = true;
    }


    private byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 传文件的代码示例
     * <p>
     * <p>
     * public static void postRequest(final Map<String, String> strParams, final Map<String, File> fileParams) {
     * new Thread(new Runnable() {
     *
     * @Override public void run() {
     * HttpURLConnection conn = null;
     * try {
     * URL url = new URL(requestUrl);
     * conn = (HttpURLConnection) url.openConnection();
     * conn.setRequestMethod("POST");
     * conn.setReadTimeout(TIME_OUT);
     * conn.setConnectTimeout(TIME_OUT);
     * conn.setDoOutput(true);
     * conn.setDoInput(true);
     * conn.setUseCaches(false);//Post 请求不能使用缓存
     * //设置请求头参数
     * conn.setRequestProperty("Connection", "Keep-Alive");
     * conn.setRequestProperty("token", token);
     * conn.setRequestProperty("Charset", "UTF-8");
     * conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
     * //上传参数
     * DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
     * //getStrParams()为一个
     * dos.writeBytes(
     *
     * getStrParams(strParams).
     *
     * toString());
     * dos.flush();
     *
     * //文件上传
     * StringBuilder fileSb = new StringBuilder();
     * for(
     * Map.Entry<String, File> fileEntry :fileParams.entrySet())
     *
     * {
     * fileSb.append(PREFIX)
     * .append(BOUNDARY)
     * .append(LINE_END)
     * //
     * //这里重点注意： name里面的值为服务端需要的key 只有这个key 才可以得到对应的文件
     * //filename是文件的名字，包含后缀名的 比如:abc.png
     * //
     * .append("Content-Disposition: form-data; name=\"image\"; filename=\"" + fileParams.get(fileEntry.getKey()).getName() + "\"" + LINE_END)
     * .append("Content-Type: image/jpg" + LINE_END) //此处的ContentType不同于 请求头 中Content-Type
     * .append("Content-Transfer-Encoding: 8bit" + LINE_END)
     * .append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
     * dos.writeBytes(fileSb.toString());
     * dos.flush();
     * InputStream is = new FileInputStream(fileEntry.getValue());
     * byte[] buffer = new byte[1024];
     * int len = 0;
     * while ((len = is.read(buffer)) != -1) {
     * dos.write(buffer, 0, len);
     * }
     * is.close();
     * dos.writeBytes(LINE_END);
     * }
     * //请求结束标志
     * dos.writeBytes(PREFIX +BOUNDARY +PREFIX +LINE_END);
     * dos.flush();
     * dos.close();
     *
     * MyLog.e(TAG+"postResponseCode() = "+conn.getResponseCode());
     * //读取服务器返回信息
     * if(conn.getResponseCode()==200)
     *
     * {
     * InputStream in = conn.getInputStream();
     * BufferedReader reader = new BufferedReader(new InputStreamReader(in));
     * String line = null;
     * StringBuilder response = new StringBuilder();
     * while ((line = reader.readLine()) != null) {
     * response.append(line);
     * }
     * MyLog.e(TAG+ "run: " + response);
     * }
     * } catch(Exception e){
     * e.printStackTrace();
     * }finally{
     * if(conn!=null){
     * conn.disconnect();
     * }
     * }
     * }
     * }).start();
     * }
     *
     *
     * private static StringBuilder getStrParams(Map<String, String> strParams){
     * StringBuilder strSb=new StringBuilder();
     * for(Map.Entry<String, String> entry:strParams.entrySet()){
     * strSb.append(PREFIX)
     * .append(BOUNDARY)
     * .append(LINE_END)
     * .append("Content-Disposition: form-data; name=\""+entry.getKey()+"\""+LINE_END)
     * .append("Content-Type: text/plain; charset="+CHARSET+LINE_END)
     * .append("Content-Transfer-Encoding: 8bit"+LINE_END)
     * .append(LINE_END)// 参数头设置完以后需要两个换行，然后才是参数内容
     * .append(entry.getValue())
     * .append(LINE_END);
     * }
     * return strSb;
     * }
     *
     */

}