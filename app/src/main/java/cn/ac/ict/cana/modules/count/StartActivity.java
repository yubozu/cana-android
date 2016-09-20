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

    EditText et_patient;
    EditText et_age;
    RadioGroup rg_gender;
    boolean isFemale = true;
    String patientName;
    int age;
    Button bt_begin;
    View patientView;
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
//        patientView = findViewById(R.id.congnition_patient);
//        et_patient = (EditText)patientView.findViewById(R.id.et_name);
//        et_age = (EditText)patientView.findViewById(R.id.et_age);
//        rg_gender = (RadioGroup) patientView.findViewById(R.id.rg_gender);
//        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int checkedId = radioGroup.getCheckedRadioButtonId();
//                isFemale = checkedId == R.id.rb_female ? true : false;
//            }
//        });

        bt_begin = (Button)findViewById(R.id.congnition_begin);
        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                patientName = et_patient.getText().toString().trim();
//                age = Integer.parseInt(et_age.getText().toString().trim());
//
//                if (patientName == null || patientName.equals("")) {
//                    Toast.makeText(StartActivity.this, getResources().getString(R.string.patientnotnull), Toast.LENGTH_SHORT).show();
//                    et_patient.requestFocus();
//                } else {
                    mp.start();
//                    Intent intent = new Intent(getApplicationContext(), CountMainActivity.class);
//                    Patient patient = new Patient(patientName, age, isFemale);
//                    intent.putExtra("patient", patient);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.cognition_guide);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vibrator.vibrate(pattern, -1);
                Intent intent = new Intent(getApplicationContext(), CountMainActivity.class);
//                Patient patient = new Patient(patientName, age, isFemale);
//                intent.putExtra("patient", patient);
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

