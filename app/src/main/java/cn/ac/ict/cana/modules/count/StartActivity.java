package cn.ac.ict.cana.modules.count;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.ac.ict.cana.R;

/**
 * Created by zhongxi on 2016/8/30.
 */
public class StartActivity extends Activity {

    Button bt_begin;
    Vibrator vibrator;
    MediaPlayer mp;
    long[] pattern = {100, 400};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_start);

        init();
    }

    public void init(){

        bt_begin = (Button)findViewById(R.id.congnition_begin);
        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mp.start();
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.cognition_guide);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vibrator.vibrate(pattern, -1);
                Intent intent = new Intent(getApplicationContext(), CountMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;

        }
        super.onStop();
    }
}

