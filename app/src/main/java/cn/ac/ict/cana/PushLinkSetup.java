package cn.ac.ict.cana;

import android.app.Application;

import com.pushlink.android.PushLink;

/**
 * Author: saukymo
 * Date: 10/10/16
 */

public class PushLinkSetup extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PushLink.start(this, R.drawable.ic_app, "yourApiKey", "yourDeviceID");
    }
}