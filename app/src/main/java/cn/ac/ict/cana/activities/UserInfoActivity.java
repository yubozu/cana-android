package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.events.NewUserEvent;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.User;
import cn.ac.ict.cana.providers.UserProvider;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author: saukymo
 * Date: 9/19/16
 */
public class UserInfoActivity extends Activity {
    Button btSave,btEdit,btCancel;
    EditText etUsername, etAge;
    ToggleButton tgGender;
    TextView etUserClinicalNumber, tvAddTitle;
    TextView etUserStudyNumber;
    TextView etUserIdentification;
    LinearLayout llAdd;
    LinearLayout llEdit;
    LinearLayout llInfo;
    UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(this));
    SharedPreferences settings;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        intent = getIntent();
        init();
    }

    private void init() {

        tvAddTitle = (TextView)findViewById(R.id.tv_title_add);
        settings = getSharedPreferences("Cana", 0);
        llEdit = (LinearLayout)findViewById(R.id.ll_edit);
        llAdd = (LinearLayout)findViewById(R.id.ll_add);
        llInfo = (LinearLayout)findViewById(R.id.layout_info);
        btSave = (Button) findViewById(R.id.bt_save_user);
        btCancel = (Button) findViewById(R.id.bt_cancel);
        etUsername = (EditText) findViewById(R.id.edittext_username);
        etAge = (EditText) findViewById(R.id.edittext_age);
        tgGender = (ToggleButton) findViewById(R.id.toggle_gender);
        etUserClinicalNumber = (TextView) findViewById(R.id.edittext_clinicalnumber);
        etUserStudyNumber = (TextView) findViewById(R.id.edittext_studynumber);
        etUserIdentification = (TextView) findViewById(R.id.edittext_identification);
        btEdit = (Button)findViewById(R.id.bt_edit_user);
        final int from  = intent.getIntExtra("from",1);
        userProvider = new UserProvider(DataBaseHelper.getInstance(this));
        if(from==1)
        {
            tvAddTitle.setText("添加用户");
            llAdd.setVisibility(View.VISIBLE);
            llEdit.setVisibility(View.GONE);
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etUsername.getText().toString().equals("") && !etAge.getText().toString().equals("") && !TextUtils.isEmpty(etUserClinicalNumber.getText())&& !TextUtils.isEmpty(etUserStudyNumber.getText())) {
                        String username = etUsername.getText().toString();
                        int age = Integer.parseInt(etAge.getText().toString());
                        String userClinicalNumber = etUserClinicalNumber.getText().toString();
                        String userStudyNumber = etUserStudyNumber.getText().toString();
                        String userIdentification  = etUserIdentification.getText().toString();
                        User user = new User(username, age, tgGender.isChecked(),userClinicalNumber,userStudyNumber,userIdentification);
                        Log.d("UserActivity", "Add new user uuid:" + user.uuid);
                        user.id = userProvider.insertUser(user);
                        //userAdapter.selectedUser = user;
                        EventBus.getDefault().post(new NewUserEvent(user));
                        MainActivity_.intent(UserInfoActivity.this).start();
                        finish();
                    }
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity_.intent(UserInfoActivity.this).start();
                    finish();
                }
            });
        }else{
            tvAddTitle.setText("修改用户信息");
            String uuid = intent.getStringExtra("uuid");
            final User user = userProvider.getUserByUuid(uuid);
            llEdit.setVisibility(View.VISIBLE);
            llAdd.setVisibility(View.GONE);
            llInfo.setFocusable(false);
            etUsername.setText(user.name);
            etAge.setText(String.valueOf(user.age));
            etUserClinicalNumber.setText(user.clinicalNumber);
            etUserStudyNumber.setText(user.studyNumber);
            etUserIdentification.setText(user.identification);
            tgGender.setChecked(user.gender);
            etUsername.setEnabled(false);
            etAge.setEnabled(false);
            etUserClinicalNumber.setEnabled(false);
            etUserStudyNumber.setEnabled(false);
            etUserIdentification.setEnabled(false);
            tgGender.setClickable(false);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etUsername.setEnabled(true);
                    tgGender.setClickable(true);
                    etAge.setEnabled(true);
                    etUserClinicalNumber.setEnabled(true);
                    etUserStudyNumber.setEnabled(true);
                    etUserIdentification.setEnabled(true);
                    etUsername.requestFocus();
                    llEdit.setVisibility(View.GONE);
                    llAdd.setVisibility(View.VISIBLE);
                }
            });
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(UserInfoActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.are_you_sure))
                            .setContentText(getString(R.string.cannot_recover))
                            .setConfirmText("保存")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    // TODO: Update new values of user.
                                    userProvider.updateUser(user);
                                    Toast.makeText(UserInfoActivity.this, "保存", Toast.LENGTH_SHORT).show();
                                    sDialog.dismissWithAnimation();
                                    init();
                                }
                            })
                            .setCancelText(String.valueOf(getResources().getText(R.string.btn_discard)))
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            });
                    sweetAlertDialog.show();
                }
            });
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    init();
                }
            });
        }

    }

}
