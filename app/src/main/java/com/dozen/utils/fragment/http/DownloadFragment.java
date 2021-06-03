package com.dozen.utils.fragment.http;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.dialog.DialogCommonListener;
import com.dozen.commonbase.dialog.VersionUpdateDialog;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class DownloadFragment extends BaseFragment {

    public static final String KEY_TEXT = "empty";

    public static DownloadFragment newInstance(String text) {
        DownloadFragment mineFragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private EditText etUrl;
    private EditText etNamePath;
    private TextView tvShow;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_download;
    }

        @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        etUrl = getContentView().findViewById(R.id.download_et_url);
        etNamePath = getContentView().findViewById(R.id.download_et_name);
        tvShow = getContentView().findViewById(R.id.download_location);

        btnSetClick(R.id.download_btn1);

    }

    @Override
    protected void setUpData() {

    }

    private void btnSetClick(int id){
        Button btn = getContentView().findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.download_btn1:
                    startDownload();
                    break;
            }
        }
    };

    private void startDownload() {
        String url = etUrl.getText().toString();
        String name = etNamePath.getText().toString();

        if (EmptyCheckUtil.isEmpty(url)){
            return;
        }
        if (EmptyCheckUtil.isEmpty(name)){
            return;
        }

        String path = Environment.getExternalStorageDirectory()+"/DCIM/dozen/";

        tvShow.setText(path+name);
        VersionUpdateDialog dialog = new VersionUpdateDialog(getBaseContext());
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {

            }

            @Override
            public void isCancel() {

            }

            @Override
            public void isClose() {

            }
        });
        dialog.show();
        dialog.setDetail("下载文件");
        dialog.setTitle("文件下载");
        dialog.setDownLoadInfo(url,path,name);
    }

}
