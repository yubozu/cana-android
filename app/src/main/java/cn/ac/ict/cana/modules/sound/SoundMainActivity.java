package cn.ac.ict.cana.modules.sound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity_;
import cn.ac.ict.cana.events.NewHistoryEvent;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;

public class SoundMainActivity extends FragmentActivity {

    private MediaRecorder recorder;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FirstFragment()).commit();
        path = getFilesDir().getAbsolutePath()+"/"+"temp.3gp";
    }

    public void prepareRecorder(String fileName, String type) {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            recorder.setOutputFile(path);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                recorder.prepare();
                recorder.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void releaseRecorder() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public void showDialog(final boolean isFirstPager) {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(getString(R.string.dialog_title)).setMessage(R.string.dialog_content).setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveToStorage();
                if (isFirstPager) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new SecondFragment()).commit();
                } else {
                    startActivity(new Intent(SoundMainActivity.this, MainActivity_.class));
                    finish();
                }
            }
        }).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }

                if (!isFirstPager) {
                    startActivity(new Intent(SoundMainActivity.this, MainActivity_.class));
                    finish();
                }
            }
        }).show();
    }

    private void SaveToStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("selectedUser", "None");
        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
        History history = new History(this, uuid, ModuleHelper.MODULE_SOUND);

        // Example: How to write data to file.
        File file = new File(path);
        file.renameTo(new File(history.filePath));
        history.id = historyProvider.InsertHistory(history);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("HistoryId", history.id);
        editor.apply();

        Log.d("CountSaveToStorage", String.valueOf(history.id));
        EventBus.getDefault().post(new NewHistoryEvent());
    }
}
