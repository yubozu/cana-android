package cn.ac.ict.cana.modules.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ac.ict.cana.R;

public class FaceMainActivity extends Activity {
    private Button bt_begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_main);
        initWidget();
    }
    private void initWidget()
    {
        bt_begin = (Button)findViewById(R.id.bt_begin);

        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaceMainActivity.this,VideoCaptureActivity.class);
                startActivity(intent);
            }
        });


    }

}
