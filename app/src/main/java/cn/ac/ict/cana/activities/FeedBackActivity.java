package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ac.ict.cana.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class FeedBackActivity extends Activity {

    Button btn_save;
    Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        init();
    }

    private void init(){
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText(getString(R.string.btn_cancel))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText(getString(R.string.btn_discard))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deleteHistory();
                                startNextActivity();
                                sDialog.cancel();
                            }
                        });
                sweetAlertDialog.show();
            }
        });
    }

    private void startNextActivity(){
        startActivity(new Intent(FeedBackActivity.this, MainActivity_.class));
        finish();
    }

    private void deleteHistory(){

    }
}
