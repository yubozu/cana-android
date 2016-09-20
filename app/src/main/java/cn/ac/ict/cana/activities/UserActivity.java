package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    LinearLayout linearLayout;
    Button btAdd;
    Button btSave;
    Button btCancel;
    ListView lvUser;
    TextView etUsername;
    TextView etAge;
    ToggleButton tgGender;

    UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    protected void init() {
        linearLayout = (LinearLayout) findViewById(R.id.layout_add_new_user);
        btAdd = (Button) findViewById(R.id.bt_add_user);
        btSave = (Button) findViewById(R.id.bt_save_user);
        btCancel = (Button) findViewById(R.id.bt_cancel);
        lvUser = (ListView) findViewById(R.id.lv_user);
        etUsername = (TextView) findViewById(R.id.edittext_username);
        etAge = (TextView) findViewById(R.id.edittext_age);
        tgGender = (ToggleButton) findViewById(R.id.toggle_gender);
        linearLayout.setVisibility(View.GONE);

        final UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(this));
        final ArrayList<User> userList = userProvider.getUsers();
        Log.d("MainAdapter", userList.toString());

        final UserAdapter userAdapter = new UserAdapter(this);

        userAdapter.setList(userList);
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
                if (etUsername.getText() != null && etAge.getText() != null) {
                    String username = etUsername.getText().toString();
                    int age = Integer.parseInt(etAge.getText().toString());


                    User user = new User(username, age, tgGender.isChecked());
                    user.id = userProvider.InsertUser(user);
                    userAdapter.addItem(user);


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

    }

    private void resetForm() {
        etUsername.setText("");
        etAge.setText("");
        linearLayout.setVisibility(View.GONE);
    }
}
