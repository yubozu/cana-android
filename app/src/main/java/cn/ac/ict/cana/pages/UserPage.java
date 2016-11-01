package cn.ac.ict.cana.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.UserAddActivity;
import cn.ac.ict.cana.adapters.UserAdapterForMain;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.User;
import cn.ac.ict.cana.providers.UserProvider;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class UserPage{

    static public View InitialUserPageView(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.pageview_user, null, false);
        final UserProvider userProvider = new UserProvider(DataBaseHelper.getInstance(context));
        final SharedPreferences settings = context.getSharedPreferences("Cana", 0);
        final ArrayList<User> userList = userProvider.getUsers();
        Log.d("MainAdapter", userList.toString());
        final ListView lvUser = (ListView) view.findViewById(R.id.lv_user);
        final UserAdapterForMain userAdapter = new UserAdapterForMain(context);
        final Button bt_add = (Button)view.findViewById(R.id.bt_add_user);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserAddActivity.class);
                intent.putExtra("from",1);
                context.startActivity(intent);
            }
        });
        userAdapter.setList(userList);

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,UserAddActivity.class);
                intent.putExtra("from",2);
                intent.putExtra("uuid",userList.get(i).uuid);
                context.startActivity(intent);
            }
        });
        lvUser.setAdapter(userAdapter);
        return view;
    }
}
