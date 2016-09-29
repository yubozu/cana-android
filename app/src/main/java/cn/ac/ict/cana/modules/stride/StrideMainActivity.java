package cn.ac.ict.cana.modules.stride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.ac.ict.cana.R;

public class StrideMainActivity extends Activity {

    RadioGroup rg_trail;
    int trail = 1;
    Button bt_begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride_main);
        initWidget();
    }

    private void initWidget() {
        rg_trail = (RadioGroup) findViewById(R.id.rg_trail);
        bt_begin = (Button) findViewById(R.id.bt_begin);
        bt_begin.setOnClickListener(new onBeginListener());
        rg_trail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                trail = Integer.parseInt(rb.getText().toString());
            }
        });
    }

    class onBeginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), GoActivity.class);

            intent.putExtra("trail", trail);
            startActivity(intent);
            finish();

        }
    }
}
