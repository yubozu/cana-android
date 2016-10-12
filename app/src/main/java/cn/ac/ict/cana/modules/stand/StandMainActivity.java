package cn.ac.ict.cana.modules.stand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity_;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class StandMainActivity extends Activity {
    private Button bt_begin;
    private ToggleButton toggleFoot;
    private boolean isRight = true;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_main);
        initWidget();
    }

    private void initWidget()
    {
        toggleFoot = (ToggleButton)findViewById(R.id.toggle_foot);

        bt_begin = (Button)findViewById(R.id.bt_begin);
        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRight = toggleFoot.isChecked();

                if(mp!=null)
                {
                    mp.stop();
                    mp.release();
                    mp=null;

                }
                Intent intent = new Intent(StandMainActivity.this,StandTestingActivity.class);
                intent.putExtra("isRight",isRight);
                startActivity(intent);
                finish();
            }
        });
        SensorManager sm = (SensorManager) StandMainActivity.this.getSystemService(Context.SENSOR_SERVICE);
        Sensor acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(acc==null ||gyro==null)
        {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(StandMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.attention))
                    .setContentText(getString(R.string.not_support))
                    .setConfirmText(getString(R.string.btn_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(StandMainActivity.this, MainActivity_.class));
                        }
                    });
            sweetAlertDialog.show();
        }else {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.stand_guide);
            mp.start();
        }
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
