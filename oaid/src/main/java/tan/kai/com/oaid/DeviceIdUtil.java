package tan.kai.com.oaid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceIdUtil {

    private static final String TAG = "DeviceIdUtil";


    /**
     * 获取唯一标识
     * <p>
     * 优先级：imei > oaid > android_id > 0
     */
    public static String getId(Context context) {
        //imei
        String id = getIMEI(context);

        //oaid
        if (TextUtils.isEmpty(id)) {
            id = getOAID();
        }

        //android_id
        if (TextUtils.isEmpty(id)) {
            id = getAndroidId(context);
        }

        //0
        if (TextUtils.isEmpty(id)) {
            id = "0";
        }

        return id;
    }

    public static String getOAID() {
        String id = "";
        if (MsaIdHelper.getInstance().isSupportOaid()) {
            id = MsaIdHelper.getInstance().getOaid();
        }
        return id;
    }

    /**
     * 获取IMEI
     * 高版本需要主动申请权限
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tel.getImei();
            } else {
                return tel.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取AndroidID
     */
    public static String getAndroidId(Context context) {
        try {
            @SuppressLint("HardwareIds")
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return androidId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
