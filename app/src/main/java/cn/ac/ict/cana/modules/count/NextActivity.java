package cn.ac.ict.cana.modules.count;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cn.ac.ict.cana.R;

/**
 * Created by zhongxi on 2016/8/22.
 */
public class NextActivity extends Activity {

    private String randomStr;
    private EditText nextet;
    private Button nextbtn;
    private Intent intent;
    private int counttoRight;
    private boolean isRight;
    private ArrayList<String> result;
    private String filePath;
//    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_next);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(NextActivity.this,StartActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
        intent = this.getIntent();
        randomStr = intent.getStringExtra("data");
//        patient = (Patient) getIntent().getSerializableExtra("patient");
        randomStr = randomStr.substring(0,6);
        nextet = (EditText)findViewById(R.id.nextet);
        nextet.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        nextbtn = (Button) findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = nextet.getText().toString().trim();
                result.add(str);
                if(str.equals(randomStr)){
                    isRight = true;
                    int shuchu = counttoRight+1;
                    Toast.makeText(NextActivity.this,"Right!\nJust "+shuchu+" times!",Toast.LENGTH_SHORT).show();
                    String msg = "正确答案是："+randomStr+"\n您的输入为：";
                    for (String x:result){
                        msg+="\n"+x;
                    }
                    dialog(msg);

                }else{
                    if(!isRight) {
                        counttoRight++;
                        if(counttoRight>=5){
                            String msg = "正确答案是："+randomStr+"\n您的输入为：";
                            for (String x:result){
                                msg+="\n"+x;
                            }
                            dialog(msg);
                        }
                    }
                    Toast.makeText(NextActivity.this,"Wrong!\nTry "+counttoRight+" times!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        result = new ArrayList<>();
    }

    protected void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NextActivity.this);
        builder.setMessage(msg+"\n保存此次测试结果吗？");

        builder.setTitle("测试结果");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String context = randomStr;
                if(isRight){
                    context+=";1";
                }else{
                    context+=";0";
                }
                for(String x: result){
                    context+=";"+x;
                }
//                CognitionRecord record = new CognitionRecord(patient,System.currentTimeMillis(),context);
//                record.setRigthStr(randomStr);
//                record.setTryData(result);
//                record.saveToStorage();
//                new DataManager(getApplicationContext()).addToSQLite(record);
                startActivity(new Intent(NextActivity.this,StartActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(NextActivity.this,StartActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
