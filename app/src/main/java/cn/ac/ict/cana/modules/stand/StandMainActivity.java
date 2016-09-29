package cn.ac.ict.cana.modules.stand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import cn.ac.ict.cana.R;

public class StandMainActivity extends Activity {
    private Button bt_begin;
    private ToggleButton toggleFoot;
    private boolean isRight = true;
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
                Intent intent = new Intent(StandMainActivity.this,StandTestingActivity.class);
                intent.putExtra("isRight",isRight);
                startActivity(intent);
                finish();
            }
        });


    }

}
