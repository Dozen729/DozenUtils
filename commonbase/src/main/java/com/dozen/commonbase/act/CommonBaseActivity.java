package com.dozen.commonbase.act;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dozen.commonbase.dialog.DialogCommonListener;
import com.dozen.commonbase.dialog.LoginTipDialog;

import java.util.ArrayList;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class CommonBaseActivity extends AppCompatActivity {
    /**
     * 权限申请
     */
    private final int REQUEST_PERMISSIONS = 1111;
    private Runnable performRunnable;
    private Runnable failCallBack;
    private long startTime;

    public void perform(String[] permissions, Runnable isSuccess, Runnable isFail) {
        performRunnable = isSuccess;
        failCallBack = isFail;
        //6.0前不作权限申请判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            isSuccess.run();
            return;
        }
        ArrayList<String> noHave = new ArrayList<>();
        for (String p : permissions) {
            //判断该权限是否已授以
            if (!havePermission(p)) {
                noHave.add(p);
            }
        }

        //全部授以 直接通过
        if (noHave.size() == 0) {
            isSuccess.run();
        } else {
            //申请权限
            startTime = System.currentTimeMillis();
            String[] noHaveArr = noHave.toArray(new String[noHave.size()]);
            ActivityCompat.requestPermissions(this, noHaveArr, REQUEST_PERMISSIONS);
        }
    }

    //检测是否拥有权限
    public boolean havePermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    if (System.currentTimeMillis() - startTime < 500) {//很多时间内用户就完成了改操作，用户没有选择，系统直接放回失败
                        showMessageOKCancel();
                    }
                    showMessageOKCancel();
                    return;
                }
            }
            if (performRunnable != null)
                performRunnable.run();
        }
    }

    //权限的弹框
    private void showMessageOKCancel() {
        LoginTipDialog dialog=new LoginTipDialog(this);
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                //跳转到系统权限设置页面
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
            }

            @Override
            public void isCancel() {

            }

            @Override
            public void isClose() {
                //退出app
//                ActivityManager.getIntance().finishAllActivity();
//                System.exit(0);
                if (failCallBack != null)
                    failCallBack.run();
            }
        });
        dialog.show();
    }

    //触摸回调
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击空白处，隐藏键盘
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hintKb();
        }
        return super.onTouchEvent(event);
    }

    //隐藏软键盘
    public void hintKb() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = getCurrentFocus();
            if (view != null && view.getWindowToken() != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void setTranslucentStatus() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
