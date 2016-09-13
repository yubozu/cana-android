package cn.ac.ict.cana.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.PermissionAdapter;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.Permission;

/**
 * Author: saukymo
 * Date: 9/12/16
 */
@EActivity(R.layout.activity_permission)
public class PermissionActivity extends Activity {

    @ViewById(R.id.bt_permission_pass)
    Button button;
    @ViewById(R.id.lv_permission_not_granted)
    ListView lvPermission;

    @ViewById(R.id.lv_permission_granted)
    ListView lvPermissionGranted;

    @Bean
    ToastManager toastManager;
    @Bean
    PermissionAdapter mPermissionAdapter;
    @Bean
    PermissionAdapter mPermissionGrantedAdapter;

    private ArrayList<Permission> mPermissionSet = new ArrayList<>();
    private ArrayList<Permission> mPermissionGrantedSet = new ArrayList<>();

    @AfterViews
    protected void init(){
        InitialPermissionCheckList();

        PermissionCheckStatus();
    }

    @AfterInject
    public void GeneratePermissionSet() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(Manifest.permission.CAMERA));
        permissions.add(new Permission(Manifest.permission.VIBRATE));
        permissions.add(new Permission(Manifest.permission.INTERNET));
        permissions.add(new Permission(Manifest.permission.RECORD_AUDIO));

        for (Permission permission: permissions) {
            permission.permissionStatus = getPackageManager().checkPermission(permission.permissionName, getPackageName()) == PackageManager.PERMISSION_GRANTED;
            if (permission.permissionStatus) {
                mPermissionGrantedSet.add(permission);
            } else {
                mPermissionSet.add(permission);
            }
        }
    }

    public void InitialPermissionCheckList(){
        mPermissionAdapter.setList(mPermissionSet);
        mPermissionGrantedAdapter.setList(mPermissionGrantedSet);

        lvPermission.setAdapter(mPermissionAdapter);
        lvPermissionGranted.setAdapter(mPermissionGrantedAdapter);
    }

    @Click(R.id.bt_permission_pass)
    public void StartOurApp(){

        toastManager.show("Start main activity.");
        MainActivity_.intent(PermissionActivity.this).start();
        PermissionActivity.this.finish();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            toastManager.show("Permission Granted");
            Permission permission = mPermissionAdapter.getItem(requestCode);
            permission.permissionStatus = true;

            mPermissionAdapter.deleteItem(requestCode);
            mPermissionGrantedAdapter.addItem(permission);

        } else {
            toastManager.show("Permission Denied");
        }
        mPermissionAdapter.notifyDataSetChanged();
        mPermissionGrantedAdapter.notifyDataSetChanged();

        PermissionCheckStatus();
    }

    public void PermissionCheckStatus() {
        boolean is_passed = mPermissionAdapter.getCount() == 0;
        if (is_passed) {
            button.setText(R.string.permission_pass);
        } else {
            button.setText(String.format(Locale.CHINA, "%d permission remained", mPermissionAdapter.getCount()));
        }
        button.setEnabled(is_passed);
    }
}