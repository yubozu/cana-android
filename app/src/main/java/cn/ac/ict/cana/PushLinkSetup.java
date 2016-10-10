package cn.ac.ict.cana;

import android.app.Application;
import android.provider.Settings;

import com.pushlink.android.PushLink;

/**
 * Author: saukymo
 * Date: 10/10/16
 */

public class PushLinkSetup extends Application {

    private String android_id;

    @Override
    public void onCreate() {
        super.onCreate();
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        PushLink.start(this, R.drawable.ic_app, "a6fcc607t71ai95q", android_id);
    }
}