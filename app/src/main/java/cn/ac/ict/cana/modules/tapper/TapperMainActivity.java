package cn.ac.ict.cana.modules.tapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import cn.ac.ict.cana.R;

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


    }

}
