package cn.ac.ict.cana.models;

import android.content.pm.PackageManager;

/**
 * Author: saukymo
 * Date: 9/12/16
 */
public class Permission{
    public String permissionName;
    public Boolean permissionStatus;

    public Permission(String permission) {
        permissionName = permission;
        permissionStatus = true;

    }

}
