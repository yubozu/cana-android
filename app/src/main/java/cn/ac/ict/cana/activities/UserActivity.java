package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.UserAdapter;
import cn.ac.ict.cana.events.NewUser;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
import cn.ac.ict.cana.models.User;
import cn.ac.ict.cana.providers.UserProvider;

/**
 * Author: saukymo
 * Date: 9/19/16
 */
public class UserActivity extends Activity {
    LinearLayout linearLayout;
    Button btAdd;
    Button btSave;
    Button btCancel;
    Button btContinue;
    ListView lvUser;
    TextView etUsername;
    TextView etAge;
    ToggleButton tgGender;

    UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(this));

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    protected void init() {
        settings = getSharedPreferences("Cana", 0);

        linearLayout = (LinearLayout) findViewById(R.id.layout_add_new_user);
        btAdd = (Button) findViewById(R.id.bt_add_user);
        btSave = (Button) findViewById(R.id.bt_save_user);
        btCancel = (Button) findViewById(R.id.bt_cancel);
        btContinue = (Button) findViewById(R.id.bt_continue);
        lvUser = (ListView) findViewById(R.id.lv_user);
        etUsername = (TextView) findViewById(R.id.edittext_username);
        etAge = (TextView) findViewById(R.id.edittext_age);
        tgGender = (ToggleButton) findViewById(R.id.toggle_gender);
        linearLayout.setVisibility(View.GONE);

        userProvider = new UserProvider(DataBaseHelper.getInstance(this));
        final ArrayList<User> userList = userProvider.getUsers();
        Log.d("MainAdapter", userList.toString());

        final UserAdapter userAdapter = new UserAdapter(this);

        userAdapter.setList(userList);
        if (userList.size() > 0) {
            userAdapter.selectedUser = userList.get(0);
        }
        lvUser.setAdapter(userAdapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etUsername.getText().toString().equals("") && !etAge.getText().toString().equals("")) {
                    String username = etUsername.getText().toString();
                    int age = Integer.parseInt(etAge.getText().toString());


                    User user = new User(username, age, tgGender.isChecked());
                    Log.d("UserActivity", "Add new user uuid:" + user.uuid);
                    user.id = userProvider.InsertUser(user);
                    userAdapter.selectedUser = user;
                    EventBus.getDefault().post(new NewUser(user));

                    resetForm();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAdapter.selectedUser != null) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("selectedUser", userAdapter.selectedUser.uuid);
                    editor.apply();
                    startModuleActivity();
                    finish();
                }
            }
        });

    }

    private void startModuleActivity() {
        Intent intent = new Intent();

        settings = getSharedPreferences("Cana", Context.MODE_PRIVATE);
        String ModuleName = settings.getString("ModuleName", "None");

        Log.d("StartModule", ModuleName);

        intent.setClass(this, ModuleHelper.getModule(ModuleName));
        startActivity(intent);
        finish();
    }

    private void resetForm() {
        etUsername.setText("");
        etAge.setText("");
        linearLayout.setVisibility(View.GONE);
    }
}
