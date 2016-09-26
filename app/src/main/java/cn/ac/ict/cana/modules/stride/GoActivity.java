package cn.ac.ict.cana.modules.stride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity_;
import cn.ac.ict.cana.events.NewHistoryFile;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;
import cn.ac.ict.cana.utils.FloatVector;

public class GoActivity extends Activity {
    TextView goContent;
    Button btnGo;
    Vibrator vibrator;
    long[] pattern = {100, 400};
    boolean flag = false;
    boolean start = false;
    MediaPlayer mp;
    SensorManager sm;
    AccEventListener accEventListener;
    GyroEventListener gyroEventListener;
    ArrayList<FloatVector> accFloatVectors;
    ArrayList<FloatVector> gyroFloatVectors;
    ArrayList<ArrayList<FloatVector>> accList = new ArrayList<>();
    ArrayList<ArrayList<FloatVector>> gyroList = new ArrayList<>();

    AlertDialog.Builder builder;
    int currentTrail = 1;
    int trailCount;
    int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride_go);
        Bundle bundle = getIntent().getExtras();
        trailCount = bundle.getInt("trail");
        distance = bundle.getInt("distance");

        goContent = (TextView) findViewById(R.id.tv_go);
        goContent.setText("Please be prepared!");
        btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new onBtnClickListener());
        btnGo.setBackgroundColor(Color.BLUE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = true;
                btnGo.setText("Stop");
                btnGo.setBackgroundColor(Color.RED);
                btnGo.setVisibility(View.VISIBLE);
                vibrator.vibrate(pattern, -1);
                goContent.setText("The Test is on going...");
                start=true;

            }
        });
        sm = (SensorManager) GoActivity.this.getSystemService(Context.SENSOR_SERVICE);

        accEventListener = new AccEventListener();
        gyroEventListener = new GyroEventListener();
        flag = false;

    }

    private void register() {
        sm.registerListener(accEventListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(gyroEventListener,
                sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
    }

    private void stop() {
        start = false;
        sm.unregisterListener(accEventListener);
        sm.unregisterListener(gyroEventListener);
    }

    class AccEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
        if(start) {
            FloatVector vector = new FloatVector(event.values[0], event.values[1], event.values[2]);
            accFloatVectors.add(vector);
        }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class GyroEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(start) {
                FloatVector gyroFloatVector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                gyroFloatVectors.add(gyroFloatVector);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class onBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!flag) {
                prepare(currentTrail);
            } else {
                stop();
                builder = new AlertDialog.Builder(GoActivity.this);
                builder.setTitle("Dialog Title");
                builder.setMessage("Dialog Content");
                builder.setPositiveButton("Positive Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accList.add(accFloatVectors);
                        gyroList.add(gyroFloatVectors);

                        currentTrail++;
                        if (currentTrail > trailCount) {
                            saveToStorage();
                            Intent intent = new Intent(GoActivity.this, MainActivity_.class);
                            startActivity(intent);
                            finish();
                        } else {
                            prepare(currentTrail);
                        }
                    }
                });
                builder.setNegativeButton("Negative Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prepare(currentTrail);
                    }
                });
                builder.show();
                flag = false;
            }


        }
    }

    private void prepare(int currentTrail) {

        mp.start();
        goContent.setText(String.format(Locale.CHINA, "即将检测: 第 %d 个来回,共 %d 个来回", currentTrail, trailCount));
        btnGo.setVisibility(View.GONE);
        accFloatVectors = new ArrayList<>();
        gyroFloatVectors = new ArrayList<>();
        register();
    }

    @Override
    protected void onPause() {
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;

        }
        stop();
        super.onPause();
    }

    public void saveToStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("selectedUser", "None");
        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
        History history = new History(this, uuid, ModuleHelper.MODULE_STRIDE);

        // Example: How to write data to file.
        File file = new File(history.filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);

            for (ArrayList<FloatVector> trail: accList){
                bufferedWriter.write("ACC " + (accList.indexOf(trail) + 1) + "\n");
                for (FloatVector acc: trail) {
                    bufferedWriter.write(acc.timeStamp + ", " + acc.x + ", " + acc.y + ", " + acc.z + "\n");
                    Log.d("GoActivity", String.valueOf(acc.timeStamp));
                }
            }

            for (ArrayList<FloatVector> trail: gyroList){
                bufferedWriter.write("GYRO " + (gyroList.indexOf(trail) + 1) + "\n");
                for (FloatVector gyro: trail) {
                    bufferedWriter.write(gyro.timeStamp + ", " + gyro.x + ", " + gyro.y + ", " + gyro.z + "\n");
                }
            }

            //Important! Have a new line in the end of txt file.
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWrite.close();
        } catch (IOException e) {
            Log.e("ExamAdapter", e.toString());
        }

        history.id = historyProvider.InsertHistory(history);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("HistoryId", history.id);
        editor.apply();

        Log.d("CountSaveToStorage", String.valueOf(history.id));
        EventBus.getDefault().post(new NewHistoryFile());
    }
}
