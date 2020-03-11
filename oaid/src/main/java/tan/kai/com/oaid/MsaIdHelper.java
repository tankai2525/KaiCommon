package tan.kai.com.oaid;

import android.content.Context;
import android.text.TextUtils;

import com.bun.miitmdid.core.JLibrary;


/**
 * @author tk
 * @date 2019/12/16
 */
public class MsaIdHelper implements MiitHelper.AppIdsUpdater {

    private static final String SP_SUPPORT = "sp_msaid_is_support";
    private static final String SP_OAID = "sp_msaid_oaid";

    private static MsaIdHelper instance;
    private String oaid;
    private Context context;
    /**
     * 0 未初始化
     * 1 支持
     * 2 不支持
     */
    private int support;

    private MsaIdHelper() {
    }

    public static MsaIdHelper getInstance() {
        if (instance == null) {
            instance = new MsaIdHelper();
        }
        return instance;
    }

    public String getOaid() {
        if (context == null) {
            throw new RuntimeException("请调用在application中调用MsaIdHelper.init()");
        }
        if (TextUtils.isEmpty(oaid)) {
            oaid = PreferencesUtils.getString(context, SP_OAID, "");
        }
        return oaid;
    }

    public boolean isSupportOaid() {

        if (context == null) {
            throw new RuntimeException("请调用在application中调用MsaIdHelper.init()");
        }

        if (support == 0) {
            support = PreferencesUtils.getInt(context, SP_SUPPORT, 0);
        }

        if (support == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void init(Context context) {
        try {
            this.context = context;
            JLibrary.InitEntry(context);
            MiitHelper miitHelper = new MiitHelper(this);
            miitHelper.getDeviceIds(context);
        } catch (Exception e) {
            e.printStackTrace();
            //如果初始报错,则不支持oaid
            this.support = 2;
            PreferencesUtils.putInt(context, SP_SUPPORT, support);
        }
    }

    @Override
    public void onIdsAvalid(boolean isSupport, String oaid, String vaid, String aaid) {
        this.support = isSupport ? 1 : 2;
        this.oaid = oaid;
        if (context != null) {
            PreferencesUtils.putInt(context, SP_SUPPORT, support);
            PreferencesUtils.putString(context, SP_OAID, oaid);
        }
    }
}
