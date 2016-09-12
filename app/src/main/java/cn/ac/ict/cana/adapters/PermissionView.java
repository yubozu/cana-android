package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.Permission;

/**
 * Author: saukymo
 * Date: 9/12/16
 * Reference: https://github.com/excilys/androidannotations/wiki/Adapters-and-lists
 */
@EViewGroup(R.layout.adapter_permission)
public class PermissionView extends LinearLayout {

    @ViewById(R.id.tv_permission_name) public TextView tvPermissionName;
    @ViewById(R.id.tv_permission_description) public TextView tvPermissionDescription;
    @ViewById(R.id.sw_permission_status) public SwitchCompat swPermissionStatus;

    public PermissionView(Context context) {
        super(context);
    }

    public void bind(Permission permission) {
        tvPermissionName.setText(permission.permissionName);
        tvPermissionDescription.setText(permission.permissionDescription);
        swPermissionStatus.setChecked(permission.permissionStatus);
    }
}
