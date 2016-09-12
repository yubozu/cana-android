package cn.ac.ict.cana;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.ac.ict.cana.adapters.PermissionAdapter;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.Permission;

@EActivity(R.layout.activity_permission)
public class PermissionActivity extends AppCompatActivity {

    @ViewById(R.id.bt_permission_pass) Button button;
    @ViewById(R.id.rv_permission) RecyclerView rvPermission;
    @Bean ToastManager toastManager;
    @Bean PermissionAdapter mPermissionAdapter;

    private ArrayList<Permission> mPermissionSet = new ArrayList<>();
    Boolean is_passed = true;

    @AfterViews
    protected void init(){
        GeneratePermissionCheckList();
        InitialPermissionCheckList();

        button.setEnabled(is_passed);
    }

    public void GeneratePermissionCheckList() {

    }

    public void InitialPermissionCheckList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPermission.setLayoutManager(linearLayoutManager);

        mPermissionAdapter.setList(mPermissionSet);

        rvPermission.setAdapter(mPermissionAdapter);
        rvPermission.setHasFixedSize(true);
    }

    @Click(R.id.bt_permission_pass)
    public void StartOurApp(){
        toastManager.show("Start main activity.");
    }


}
