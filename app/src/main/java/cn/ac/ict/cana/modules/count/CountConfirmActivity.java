package cn.ac.ict.cana.modules.count;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;

/**
 * Created by zhongxi on 2016/8/22.
 */
public class CountConfirmActivity extends Activity {

    private String randomStr;
    private EditText nextet;
    private Button nextbtn;
    private Intent intent;
    private int times;
    private boolean isRight;
    private ArrayList<String> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_confirm);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(CountConfirmActivity.this,CountMainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
        intent = this.getIntent();
        randomStr = intent.getStringExtra("data");
        randomStr = randomStr.substring(0,6);
        nextet = (EditText)findViewById(R.id.et_answer);
        nextet.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        nextbtn = (Button) findViewById(R.id.btn_confirm);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = nextet.getText().toString().trim();
                result.add(str);
                if(str.equals(randomStr)){
                    isRight = true;
                    Toast.makeText(CountConfirmActivity.this, String.format(Locale.CHINA, getString(R.string.count_times), times + 1),Toast.LENGTH_SHORT).show();
                    String msg = String.format(Locale.CHINA, getString(R.string.count_right_answer), randomStr);
                    for (String x:result){
                        msg+="\n"+x;
                    }
                    dialog(msg);

                }else{
                    if(!isRight) {
                        times++;
                        if(times >=5){
                            String msg = String.format(Locale.CHINA, getString(R.string.count_right_answer), randomStr);
                            for (String x:result){
                                msg+="\n"+x;
                            }
                            dialog(msg);
                        }
                    }
                    Toast.makeText(CountConfirmActivity.this, String.format(Locale.CHINA, getString(R.string.count_wrong_answer), times),Toast.LENGTH_SHORT).show();
                }
            }
        });

        result = new ArrayList<>();
    }

    protected void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CountConfirmActivity.this);
        builder.setMessage(getString(R.string.dialog_content));

        builder.setTitle(getString(R.string.dialog_title));
        builder.setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = randomStr;
                if(isRight){
                    content+=";1";
                }else{
                    content+=";0";
                }
                for(String x: result){
                    content+=";"+x;
                }

                saveToStorage(content);
                startActivity(new Intent(CountConfirmActivity.this, ModuleHelper.getActivityAfterExam()));
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CountConfirmActivity.this, ModuleHelper.getActivityAfterExam()));
                finish();
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void saveToStorage(String content){
        SharedPreferences sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
//        String uuid = sharedPreferences.getString("selectedUser", "None");
//        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
////        History history = new History(this, uuid, ModuleHelper.MODULE_COUNT);

        // Example: How to write data to file.
        String filePath = History.getFilePath(this, ModuleHelper.MODULE_COUNT);
        File file = new File(filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);

            bufferedWriter.write(content);

            //Important! Have a new line in the end of txt file.
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWrite.close();
        } catch (IOException e) {
            Log.e("ExamAdapter", e.toString());
        }

//        history.id = historyProvider.InsertHistory(history);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HistoryFilePath", filePath);
        editor.apply();

//        EventBus.getDefault().post(new NewHistoryEvent());
    }
}
