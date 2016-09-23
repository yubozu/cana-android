package cn.ac.ict.cana.modules.stand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.events.NewHistoryFile;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;
import cn.ac.ict.cana.utils.FloatVector;


public class StandTestingActivity extends Activity {
    private NumberProgressBar pbx;
    private NumberProgressBar pby;
    private NumberProgressBar pbz;
    private TickTockView ttv;
    AlertDialog.Builder builder;
    Vibrator vibrator;
    long[] pattern = {100, 400};
    SensorManager sm;
    AccEventListener accEventListener;
    GyroEventListener gyroEventListener;
    ArrayList<FloatVector> accVectors;
    ArrayList<FloatVector> gyroVectors;
    boolean start = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_testing);

    }
    private void init(){
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        accVectors = new ArrayList<>();
        gyroVectors = new ArrayList<>();
        initSensors();
        initProgressBars();
        initTickTockView();
    }
    private void initTickTockView()
    {
        ttv = (TickTockView)findViewById(R.id.ttv);
        Calendar start = Calendar.getInstance();
        start.add(Calendar.SECOND,-1);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.SECOND,10);
        ttv.setOnTickListener(new TickTockView.OnTickListener() {
            @Override
            public String getText(long timeRemainingInMillis) {
                if(timeRemainingInMillis<=0)
                {
                    vibrator.vibrate(pattern, -1);
                    stopSensors();
                    showDialog();
                }
                return timeRemainingInMillis/1000+1+"秒";
            }
        });
        ttv.start(start,end);
    }
    private void initSensors()
    {
        accEventListener = new AccEventListener();
        gyroEventListener = new GyroEventListener();
        sm = (SensorManager) StandTestingActivity.this.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(accEventListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(gyroEventListener,
                sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);

    }
    private void initProgressBars()
    {
        pbx = (NumberProgressBar)findViewById(R.id.pb_x);
        pby = (NumberProgressBar)findViewById(R.id.pb_y);
        pbz = (NumberProgressBar)findViewById(R.id.pb_z);
        pbx.setMax(100);
        pby.setMax(100);
        pbz.setMax(100);

    }
    private void stopSensors()
    {
        sm.unregisterListener(accEventListener);
        sm.unregisterListener(gyroEventListener);
        ttv.stop();
    }
    private void showDialog()
    {
        builder = new AlertDialog.Builder(StandTestingActivity.this);
        builder.setTitle("iTug Stopped");
        builder.setMessage("Please accept or reject this try");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToStorage();
                finish();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
    class AccEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(start) {
                FloatVector vector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                accVectors.add(vector);
                pbx.setProgress((int)(event.values[0]*10));
                pby.setProgress((int)(event.values[1]*10));
                pbz.setProgress((int)(event.values[2]*10));
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
                FloatVector gyroVector = new  FloatVector(event.values[0], event.values[1], event.values[2]);
                gyroVectors.add(gyroVector);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onPause() {
        stopSensors();
        this.finish();
        super.onPause();
    }

    @Override
    protected void onResume() {
        init();
        super.onResume();
    }

    public void saveToStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("selectedUser", "None");
        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
        History history = new History(this, uuid, ModuleHelper.MODULE_STAND);

        // Example: How to write data to file.
        File file = new File(history.filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);

            for (FloatVector acc: accVectors) {
                bufferedWriter.write(acc.timeStamp + ", " + acc.x + ", " + acc.y + ", " + acc.z + "\n");
                Log.d("GoActivity", String.valueOf(acc.timeStamp));
            }

            for (FloatVector gyro: gyroVectors) {
                bufferedWriter.write(gyro.timeStamp + ", " + gyro.x + ", " + gyro.y + ", " + gyro.z + "\n");
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
