package cn.ac.ict.cana.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.MainAdapter;
import cn.ac.ict.cana.events.ResponseEvent;
import cn.ac.ict.cana.helpers.ToastManager;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

//    private ProgressDialog mProgressDialog;
    private SpotsDialog mProgressDialog;

    private OkHttpClient client;
    private int success, failed, total;
    @AfterViews
    public void init() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
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
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    public void startUpload(final ArrayList<Request> requests) {
//        toastManager.show(String.format(Locale.CHINA, "Message From MainActivity %d", requests.size()));
        showProgressBar(true, "Uploading");
        success = 0;
        failed = 0;
        total = requests.size();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (Request request: requests) {
                    boolean result = false;
                    try {
                        Response response = client.newCall(request).execute();
                        Log.d("upload", response.toString());
                        result = response.code() == 200;

                    } catch (IOException error) {
                        Log.e("upload", error.toString());
                    }
                    EventBus.getDefault().post(new ResponseEvent(result));
                }
                return null;
            }
        }.execute();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEvent(ResponseEvent event) {
        if (event.success) {
            success += 1;
        } else {
          failed += 1;
        }
        mProgressDialog.setMessage(String.format(Locale.CHINA, "Uploading...(%d/%d)", success + failed, total));
        if (success + failed == total) {
            showProgressBar(false, "");
            toastManager.show(String.format(Locale.CHINA, "Success: %d, Failed %d.", success, failed));
        }
    }

    //ProgressBar
    private void initProgressBar() {
        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this, R.layout.processdialog);
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCancelable(false);
            mProgressDialog = new SpotsDialog(this, R.style.Custom);
        }
    }

    public void showProgressBar(boolean show, String message) {
        initProgressBar();
        if (show) {
//            mProgressDialog.setMessage(message);
//            mProgressDialog.show();
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
//            mProgressDialog.hide();
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
