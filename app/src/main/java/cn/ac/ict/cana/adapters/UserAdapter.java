package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

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

    private List<User> users;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final UserView userView;
        if (view == null) {
            userView = UserView_.build(mContext);
        } else {
            userView = (UserView) view;
        }


        User user = getItem(position);
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

    public void addItem(User permission){
        users.add(permission);
    }

    public void deleteItem(int position) {
        users.remove(position);
    }



}
