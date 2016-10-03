package cn.ac.ict.cana.modules.count;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import cn.ac.ict.cana.R;

/**
 * Created by zhongxi on 2016/8/30.
 */
public class CountMainActivity extends Activity {

    Button bt_begin;
    Vibrator vibrator;
    MediaPlayer mp;
    long[] pattern = {100, 400};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_main);

        init();
    }

    public void init(){

        bt_begin = (Button)findViewById(R.id.count_begin);
        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp!=null)
                {
                    mp.stop();
                    mp.release();
                    mp=null;

                }
                Intent intent = new Intent(getApplicationContext(), CountGameActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.count_guide);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                vibrator.vibrate(pattern, -1);
                mp = null;
            }
        });

        mp.start();
    }

    @Override
    protected void onPause() {
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;

        }
        finish();
        super.onPause();
    }
}

