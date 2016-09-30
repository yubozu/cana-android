package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import cn.ac.ict.cana.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                PermissionActivity_.intent(WelcomeActivity.this).start();
                finish();
            }
        },2500);
    }

    @Override
    public void onBackPressed() {

    }
}
