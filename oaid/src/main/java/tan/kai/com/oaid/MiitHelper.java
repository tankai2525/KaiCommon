package tan.kai.com.oaid;

import android.content.Context;
import android.util.Log;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.core.MdidSdk;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.supplier.IdSupplier;


/**
 * Created by zheng on 2019/8/22.
 */

public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater listener;

    public MiitHelper(AppIdsUpdater callback) {
        listener = callback;
    }

    public void getDeviceIds(Context cxt) {
        long timeb = System.currentTimeMillis();
        int nres = callFromReflect(cxt);
//        int nres=DirectCall(cxt);
        long timee = System.currentTimeMillis();
        long offset = timee - timeb;
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {
            //不支持的设备

        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {
            //加载配置文件出错

        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {
            //不支持的设备厂商

        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {
            //获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程

        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {
            //反射调用出错

        }
        Log.d(getClass().getSimpleName(), "return value: " + nres + " --耗时" + offset);

    }


    /**
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     *
     * @param cxt
     * @return
     */
    private int callFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    /**
     * 直接java调用，如果这样调用，在android 9以前没有题，在android 9以后会抛找不到so方法的异常
     * 解决办法是和JLibrary.InitEntry(cxt)，分开调用，比如在A类中调用JLibrary.InitEntry(cxt)，在B类中调用MdidSdk的方法
     * A和B不能存在直接和间接依赖关系，否则也会报错
     *
     * @param cxt
     * @return
     */
    private int DirectCall(Context cxt) {
        MdidSdk sdk = new MdidSdk();
        return sdk.InitSdk(cxt, this);
    }

    @Override
    public void OnSupport(boolean isSupport, IdSupplier supplier) {
        if (supplier == null) {
            return;
        }
        String oaid = supplier.getOAID();
        String vaid = "";
        String aaid = "";
        String idstext = "support: " + (isSupport ? "true" : "false") + "\n"
                + "OAID: " + oaid + "\n" + "VAID: " + vaid + "\n" + "AAID: " + aaid + "\n";
        Log.d("MiitHelper", idstext);
        supplier.shutDown();
        if (listener != null) {
            listener.onIdsAvalid(isSupport, oaid, vaid, aaid);
        }
    }

    public interface AppIdsUpdater {
        void onIdsAvalid(boolean isSupport, String oaid, String vaid, String aaid);
    }

}
