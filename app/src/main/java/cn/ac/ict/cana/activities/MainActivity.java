package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.MainAdapter;
import cn.ac.ict.cana.events.CheckedItemChangedEvent;
import cn.ac.ict.cana.events.ResponseEvent;
import cn.ac.ict.cana.helpers.ToastManager;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.ntb) NavigationTabBar navigationTabBar;
    @ViewById(R.id.vp_horizontal_ntb) ViewPager viewPager;
    @Bean MainAdapter mMainAdapter;
    @Bean ToastManager toastManager;

    public ArrayList<Call> callArrayList;
    private SpotsDialog mProgressDialog;
    private int success, failed;

    @AfterViews
    public void init() {
        viewPager.setAdapter(mMainAdapter);
        callArrayList = new ArrayList<>();
        int color =  Color.parseColor("#68BED9");

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_test),
                       color
                ).title(getResources().getString(R.string.page_exam))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_user),
                        color
                ).title(getResources().getString(R.string.page_user))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_history),
                        color
                ).title(getResources().getString(R.string.page_history))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_settings),
                        color
                ).title(getResources().getString(R.string.page_setting))
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setBgColor(Color.parseColor("#F3F5F7"));
        navigationTabBar.setTitleSize(40);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEvent(ResponseEvent event) {
        if (event.success) {
            success += 1;
        } else {
          failed += 1;
        }
        if (success + failed >= event.total) {
            finishUpload();
        }
    }

    private void finishUpload(){
        if (failed + success == 0){
            return;
        }
        showProgressBar(false, "");
        if (failed > 0) {
            toastManager.show(String.format(Locale.CHINA, getResources().getString(R.string.upload_failed), success, failed));
        } else {
            toastManager.show(getResources().getString(R.string.upload_success));
        }
        success = 0;
        failed = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onCheckedItemChangedEvent(CheckedItemChangedEvent event) {
        Button uploadButton = (Button) findViewById(R.id.bt_upload);
        if (event.count == 0) {
            uploadButton.setEnabled(false);
        } else {
            uploadButton.setEnabled(true);
        }
    }

    public void cancelUpload(){
        Log.d("cancelUpload", "Number of upload call: " + callArrayList.size());
        for (Call call: callArrayList){
            call.cancel();
        }
    }

    //ProgressBar
    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new SpotsDialog(this, R.style.Custom);
//            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
        }
    }

    public void showProgressBar(boolean show, String message) {
        initProgressBar();
        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        Log.d("onBackPressed", "Function entered");
        cancelUpload();
        super.onBackPressed();
    }

}
