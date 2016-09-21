package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.User;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EBean
public class UserAdapter extends BaseAdapter {

    private Context mContext;
    public UserAdapter(Context context){
        mContext = context;
    }
    public User selectedUser = null;
    private List<User> users;

    // temporary solution.
    private int[] textViews = {R.id.tv_user_name, R.id.tv_user_id, R.id.tv_user_age, R.id.tv_user_gender};

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final UserView userView;
        if (view == null) {

            userView = UserView_.build(mContext);
        } else {
            userView = (UserView) view;
        }


        final User user = getItem(position);
        LinearLayout linearLayout = (LinearLayout) userView.findViewById(R.id.layout_user);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUser = user;
                notifyDataSetChanged();
            }
        });


        int textColor = mContext.getResources().getColor(R.color.freebie_6);
        int BackgroundColor = mContext.getResources().getColor(R.color.freebie_1);

        //swap two color
        if (user.equals(selectedUser)) {
            BackgroundColor = mContext.getResources().getColor(R.color.freebie_2);
            textColor = mContext.getResources().getColor(R.color.freebie_1);
        }

        linearLayout.setBackgroundColor(BackgroundColor);
        for(int id: textViews){
            TextView textView = (TextView) userView.findViewById(id);
            textView.setTextColor(textColor);
        }


        userView.bind(user);

        return userView;
    }

    public void setList(ArrayList<User> userlist) {
        users = userlist;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(User user){
        users.add(user);
    }

}
