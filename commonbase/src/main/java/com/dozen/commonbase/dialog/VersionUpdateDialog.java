package com.dozen.commonbase.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dozen.commonbase.R;
import com.dozen.commonbase.http.DownloadUtil;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.utils.VersionInfoUtils;

import java.io.File;

/**
 * 文件描述：下载文件，eg:更新应用
 * 作者：Dozen
 * 创建时间：2020/11/17
 */
public class VersionUpdateDialog extends Dialog {

    private Context mContext;
    private LinearLayout llProgress;
    private LinearLayout llSelect;
    private TextView tvProgressNumber;
    private ProgressBar pbShow;
    private TextView tvDetail;
    private TextView tvTitle;

    private String url = "http://imtt.dd.qq.com/16891/myapp/channel_92757348_1001280_9bde84da0d1295f2e7ef9db3ba5e3dce.apk?hsr=5848";
    private String path = Environment.getExternalStorageDirectory()+"/DCIM/commonbase/";
    private String fileName = "commonBase_update.apk";

    private DialogCommonListener dialogCommonListener;

    public void setDialogCommonListener(DialogCommonListener dialogCommonListener) {
        this.dialogCommonListener = dialogCommonListener;
    }

    public VersionUpdateDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version_update);
        setDialogWindowAttr();
        initView();
    }

    //设置宽高位置等属性

    private void setDialogWindowAttr() {
        Window window = getWindow();
        //中间显示
        window.setGravity(Gravity.CENTER);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = (int) (ScreenUtils.getScreenWidth(mContext) * 0.8);
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        //设置外面不可点击
        setCancelable(false);
    }
    //初始化

    private void initView() {
        tvDetail = findViewById(R.id.tv_version_detail);
        findViewById(R.id.rtv_version_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llProgress.setVisibility(View.VISIBLE);
                llSelect.setVisibility(View.GONE);
                downloadFile();
            }
        });

        findViewById(R.id.rtv_version_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dialogCommonListener.isCancel();
            }
        });

        tvTitle = findViewById(R.id.tv_version_title);
        pbShow = findViewById(R.id.pb_version_update);
        llProgress = findViewById(R.id.ll_version_progress);
        llSelect = findViewById(R.id.ll_version_select);
        tvProgressNumber = findViewById(R.id.tv_progress_number);
        pbShow.setMax(100);
        llProgress.setVisibility(View.GONE);
        llSelect.setVisibility(View.VISIBLE);
    }

    //下载文件
    private void downloadFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadUtil.get().download(url, path, fileName, new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        MyLog.d("suss");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = file;
                        progressHandler.sendMessage(msg);
                    }

                    @Override
                    public void onDownloading(int progress) {
                        MyLog.d(progress + "");
                        Message msg = new Message();
                        msg.what = 2;
                        msg.arg1 = progress;
                        progressHandler.sendMessage(msg);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        MyLog.e(e.toString());
                        Message msg = new Message();
                        msg.what = 3;
                        progressHandler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler progressHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StyleToastUtil.success("下载成功");
                    dismiss();
                    dialogCommonListener.isConfirm();
                    break;
                case 2:
                    pbShow.setProgress(msg.arg1);
                    tvProgressNumber.setText(msg.arg1 + "");
                    break;
                case 3:
                    StyleToastUtil.error("下载失败");
                    dialogCommonListener.isClose();
                    llProgress.setVisibility(View.GONE);
                    llSelect.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    //设置更新app,下载成功后使用VersionInfoUtils.installApk(this)安装应用
    public void setUpdateApp(){
        String filePath= VersionInfoUtils.getVersionFilePath();
        String[] ss=filePath.split("/");
        String[] paths=filePath.split(ss[ss.length-1]);
        this.path = paths[0];
        this.fileName = ss[ss.length-1];
        MyLog.d(path);
        MyLog.d(fileName);
    }

    //设置说明内容
    public void setDetail(String detail) {
        tvDetail.setText(detail);
    }

    public void setDetail(String detail, int color) {
        tvDetail.setText(detail);
        tvDetail.setTextColor(color);
    }

    //设置标题
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(String title, int color) {
        tvTitle.setText(title);
        tvTitle.setTextColor(color);
    }

    //设置下载链接、地址和文件名
    public void setDownLoadInfo(String url, String path, String fileName) {
        this.url = url;
        this.path = path;
        this.fileName = fileName;
    }

    public void setDownLoadInfo(String url) {
        this.url = url;
    }
}
