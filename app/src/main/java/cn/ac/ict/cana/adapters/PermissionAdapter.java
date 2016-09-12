package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import cn.ac.ict.cana.helpers.RecyclerViewAdapterBase;
import cn.ac.ict.cana.helpers.ViewWrapper;
import cn.ac.ict.cana.models.Permission;

/**
 * Author: saukymo
 * Date: 9/12/16
 * Reference: https://github.com/excilys/androidannotations/wiki/Adapters-and-lists
 */

@EBean
public class PermissionAdapter extends RecyclerViewAdapterBase<Permission, PermissionView> {

    @RootContext Context mContext;

    @Override
    protected PermissionView onCreateItemView(ViewGroup parent, int viewType) {
        return PermissionView_.build(mContext);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<PermissionView> viewHolder, int position) {
        PermissionView permissionview = viewHolder.getView();
        Permission permission = mItems.get(position);

        permissionview.bind(permission);
    }

}
