package cn.ac.ict.cana.modules.tapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.events.NewHistoryEvent;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;

public class TapperMainActivity extends Activity {
    private Button bt_begin;
    private ToggleButton toggleHand;
    private boolean isRight = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapper_main);
        initWidget();
    }

    private void initWidget()
    {
        toggleHand = (ToggleButton)findViewById(R.id.toggle_hand);

        bt_begin = (Button)findViewById(R.id.bt_begin);
        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRight = toggleHand.isChecked();
                Intent intent = new Intent(TapperMainActivity.this,TapperTestingActivity.class);
                intent.putExtra("isRight",isRight);
                startActivity(intent);

            }
        });

    public void showDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(getString(R.string.dialog_title)).setMessage(getString(R.string.dialog_content)).setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToStorage();
                startActivity(new Intent(TapperMainActivity.this, ModuleHelper.getActivityAfterExam()));
                finish();
                initNum();
                setEnable(true, false);
            }
        }).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content.clear();
                initNum();
                btnStart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.freebie_4));
                setEnable(true, false);
                startActivity(new Intent(TapperMainActivity.this, ModuleHelper.getActivityAfterExam()));
                finish();
            }
        }).show();
    }

    }

}
