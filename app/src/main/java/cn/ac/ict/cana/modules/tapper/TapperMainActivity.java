package cn.ac.ict.cana.modules.tapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity_;
import cn.ac.ict.cana.events.NewHistoryFile;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;

public class TapperMainActivity extends Activity {

    @BindView(R.id.tv_left_count)
    TextView tvLeft;
    @BindView(R.id.tv_right_count)
    TextView tvRight;
    @BindView(R.id.btn_start_tiper)
    Button btnStart;
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_right)
    Button btnRight;

    private int leftCount, rightCount;
    private ArrayList<String> content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapper_main);
        ButterKnife.bind(this);
        content = new ArrayList<>();
    }


    @OnClick({R.id.btn_start_tiper, R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_tiper:
                new MyCount(10000, 1000).start();
                setEnable(false, true);
                break;
            case R.id.btn_left:
                tvLeft.setText(String.valueOf(++leftCount));
                content.add("left:" + System.currentTimeMillis());
                break;
            case R.id.btn_right:
                tvRight.setText(String.valueOf(++rightCount));
                content.add("right:" + System.currentTimeMillis());
                break;
        }
    }

    private void setEnable(boolean enabled, boolean enabled2) {
        btnStart.setEnabled(enabled);
        btnLeft.setEnabled(enabled2);
        btnRight.setEnabled(enabled2);
    }

    class MyCount extends CountDownTimer {

        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            btnStart.setText("测试倒计时:" + (l / 1000));
        }

        @Override
        public void onFinish() {
            showDialog();
            btnStart.setText("开始测试!");
        }
    }

    public void showDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle("结束").setMessage("请确定是否保存?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToStorage();
                startActivity(new Intent(TapperMainActivity.this, MainActivity_.class));
                finish();
                initNum();
                setEnable(true, false);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content.clear();
                initNum();
                setEnable(true, false);
            }
        }).show();
    }

    private void initNum() {
        leftCount = 0;
        rightCount = 0;
        tvLeft.setText(String.valueOf(0));
        tvRight.setText(String.valueOf(0));
    }

    public void saveToStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("selectedUser", "None");
        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
        History history = new History(this, uuid, ModuleHelper.MODULE_TAPPER);

        // Example: How to write data to file.
        File file = new File(history.filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);

            for (String line : content){
                bufferedWriter.write(line + "\n");
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
