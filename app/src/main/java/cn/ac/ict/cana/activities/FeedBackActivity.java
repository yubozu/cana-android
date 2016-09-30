package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.events.NewHistoryEvent;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;
import cn.ac.ict.cana.providers.UserProvider;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class FeedBackActivity extends Activity {

    Button btnSave;
    Button btnCancel;
    TextView tvEvaluation, tv_name, tv_time;
    TextView tvModule;
    EditText editTextDocotr;
    private SharedPreferences sharedPreferences;
    SimpleRatingBar ratingBar;
    ToastManager toastManager;
    int rate;
    String doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        init();
    }

    private void init() {
        toastManager = new ToastManager(this);
        editTextDocotr = (EditText) findViewById(R.id.edittext_doctor);
        rate = 0;
        ratingBar = (SimpleRatingBar) findViewById(R.id.rating_bar);
        ratingBar.setStepSize(1);
        ratingBar.setRating(0f);
        ratingBar.setFillColor(ContextCompat.getColor(this, R.color.freebie_2));
        ratingBar.setBorderColor(ContextCompat.getColor(this, R.color.freebie_2));
        ratingBar.setPressedBorderColor(ContextCompat.getColor(this, R.color.freebie_2));
        ratingBar.setPressedFillColor(ContextCompat.getColor(this, R.color.freebie_2));
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                Log.d("RateBar", "Rating: " + rating);
            }
        });
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                rate = Math.round(rating);
            }
        });

        tvEvaluation = (TextView) findViewById(R.id.tv_evaluation);
        tvModule = (TextView) findViewById(R.id.tv_module_name);
        tv_name = (TextView) findViewById(R.id.tv_fb_name);
        tv_time = (TextView) findViewById(R.id.tv_fb_time);
        sharedPreferences = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("SelectedUser", "None");
        String moduleName = sharedPreferences.getString("ModuleName", "None");
        String filePath = sharedPreferences.getString("HistoryFilePath", "None");

//        Set to doctor who used last time by default.
        doctor = sharedPreferences.getString("DoctorName", "");
        editTextDocotr.setText(doctor);

        UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(this));
        String name = userProvider.getUsernameByUuid(uuid);

        final History history = new History(uuid, moduleName, filePath, 0, "");
        tv_name.setText(name);
        tv_time.setText(history.createdTime);

        tvEvaluation.setText(ModuleHelper.getEvaluation(history));
        tvModule.setText(ModuleHelper.getName(this, history.type));

        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextDocotr.getText().toString().equals("")) {
                    toastManager.show(getResources().getText(R.string.save_success));
                    history.rating = rate;
                    history.doctor = editTextDocotr.getText().toString();
                    saveToStorage(history);
                    startNextActivity();
                } else {
                    toastManager.show(getResources().getText(R.string.input_doctor_name));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.are_you_sure))
                        .setContentText(getString(R.string.cannot_recover))
                        .setConfirmText(getString(R.string.btn_cancel))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText(String.valueOf(getResources().getText(R.string.btn_discard)))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deleteHistory(history);
                                startNextActivity();
                                sDialog.cancel();
                            }
                        });
                sweetAlertDialog.show();
            }
        });
    }

    private void startNextActivity() {

        startActivity(new Intent(FeedBackActivity.this, MainActivity_.class));
        finish();

    }

    private void deleteHistory(History history) {
        File file = new File(history.filePath);
        if (file.exists()) {
            Boolean result = file.delete();
            Log.d("deleteHistory", "File delete result: " + result);
        }
    }

    private void saveToStorage(History history) {
        HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(this));
        history.id = historyProvider.InsertHistory(history);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("HistoryId", history.id);
        editor.putString("DoctorName", history.doctor);
        editor.apply();

        EventBus.getDefault().post(new NewHistoryEvent());
    }
}
