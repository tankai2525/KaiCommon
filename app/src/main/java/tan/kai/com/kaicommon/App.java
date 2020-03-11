package tan.kai.com.kaicommon;

import android.app.Application;

import tan.kai.com.oaid.MsaIdHelper;

/**
 * @author tk
 * @date 2020/3/11
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MsaIdHelper.getInstance().init(this);
    }
}
