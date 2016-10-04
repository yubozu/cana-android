package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import cn.ac.ict.cana.R;

public class WelcomeActivity extends Activity {
    private final static String TAG = "welcome";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //音量控制,初始化定义  
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量  
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量  
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "currentVolume: "+currentVolume);
        Log.d(TAG, "maxVolume: "+maxVolume);

        if(currentVolume<maxVolume/2)
       {
           Toast.makeText(this, getString(R.string.change_volume), Toast.LENGTH_SHORT).show();
           mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,
                   AudioManager.FX_FOCUS_NAVIGATION_UP);
       }
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
