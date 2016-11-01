package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.UserAdapter;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.User;
import cn.ac.ict.cana.providers.UserProvider;

/**
 * Author: saukymo
 * Date: 9/19/16
 */
public class UserActivity extends Activity {
    Button btContinue;
    ListView lvUser;

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

        btContinue = (Button) findViewById(R.id.bt_continue);
        lvUser = (ListView) findViewById(R.id.lv_user);

        userProvider = new UserProvider(DataBaseHelper.getInstance(this));
        final ArrayList<User> userList = userProvider.getUsers();
        Log.d("MainAdapter", userList.toString());

        final UserAdapter userAdapter = new UserAdapter(this);

        userAdapter.setList(userList);
        if (userList.size() > 0) {
            userAdapter.selectedUser = userList.get(0);
        }
        lvUser.setAdapter(userAdapter);


        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAdapter.selectedUser != null) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("SelectedUser", userAdapter.selectedUser.uuid);
                    editor.apply();
                    startModuleActivity();
                    finish();
                }
            }
        });

    }

    private void startModuleActivity() {
        Intent intent = new Intent();

        intent.setClass(this, MedicalInfoActivity.class);
        startActivity(intent);
        finish();
    }

}
