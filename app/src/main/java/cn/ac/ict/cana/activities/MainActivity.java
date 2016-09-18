package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.util.Log;

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
import cn.ac.ict.cana.events.ResponseEvent;
import cn.ac.ict.cana.helpers.ToastManager;
import dmax.dialog.SpotsDialog;

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

    private SpotsDialog mProgressDialog;

    private int success, failed, total;

    @AfterViews
    public void init() {
        viewPager.setAdapter(mMainAdapter);
        int color =  Color.parseColor("#68BED9");

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_test),
                       color
                ).title("Exam")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_user),
                        color
                ).title("User")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_history),
                        color
                ).title("History")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_settings),
                        color
                ).title("Setting")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
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
        mProgressDialog.setMessage(String.format(Locale.CHINA, "Uploading...(%d/%d)", success + failed, total));
        Log.d("onResponse", String.format(Locale.CHINA, "Uploading...(%d/%d)", success + failed, total));
        if (success + failed >= event.total) {
            showProgressBar(false, "");
            toastManager.show(String.format(Locale.CHINA, "Success: %d, Failed %d.", success, failed));
            success = 0;
            failed = 0;
        }
    }

    //ProgressBar
    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new SpotsDialog(this, R.style.Custom);
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
}
