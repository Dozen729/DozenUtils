package com.dozen.commonbase.mode;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/4/23
 */
public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater _listener;

    public MiitHelper(AppIdsUpdater callback) {
        _listener = callback;
    }


    public void getDeviceIds(Context cxt) {
        long timeb = System.currentTimeMillis();
        int nres = CallFromReflect(cxt);
        //        int nres=DirectCall(cxt);
        long timee = System.currentTimeMillis();
        long offset = timee - timeb;
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//1008612 不支持的设备
            MyLog.e(nres + "");
        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//1008613 加载配置文件出错
            MyLog.e(nres + "");
        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//1008611 不支持的设备厂商
            MyLog.e(nres + "");
        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//1008614 获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
            MyLog.e(nres + "");
        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//1008615 反射调用出错
            MyLog.e(nres + "");
        }

    }

    /*
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     *
     * */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if (_supplier == null) {
            return;
        }
       /* String oaid=_supplier.getOaid();
        String vaid=_supplier.getVAID();
        String aaid=_supplier.getAAID();
        String udid=_supplier.getUDID();
        StringBuilder builder=new StringBuilder();
        builder.append("support: ").append(isSupport?"true":"false").append("\n");
        builder.append("UDID: ").append(udid).append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext=builder.toString();*/

        String oaid = _supplier.getOAID();
        SPUtils.setString(APPBase.getApplication(), CommonConstant.OAID, oaid);
        if (_listener != null) {
            _listener.OnIdsAvalid(oaid);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull String ids);
    }

}