package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.Permission;
import cn.ac.ict.cana.models.User;

/**
 * Author: saukymo
 * Date: 9/13/16
 */

@EViewGroup(R.layout.adapter_user)
public class UserView extends LinearLayout {

    @ViewById(R.id.tv_user_id) public TextView tvUserId;
    @ViewById(R.id.tv_user_name) public TextView tvUserName;
    @ViewById(R.id.tv_user_age) public TextView tvUserAge;
    @ViewById(R.id.tv_user_gender) public TextView tvUserGender;
    public Context mContext;

    public UserView(Context context) {
        super(context);
        mContext = context;
    }

    public void bind(User user) {
        tvUserId.setText(String.valueOf(user.id));
        tvUserName.setText(user.name);
        tvUserAge.setText(String.valueOf(user.age));
        tvUserGender.setText(String.valueOf(user.gender));
    }
}


