package cn.ac.ict.cana.modules.count;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import cn.ac.ict.cana.R;

public class CountMainActivity extends Activity {

    Handler mHandler = new Handler();
    private ScaleAnimation scaleAnimation;
    private TextView tvnum;
    private int width;
    private int height;
    private Random random;
    private int tvnumWidth;
    private int tvnumHeight;
    private AnimationSet set;
    private RotateAnimation rotateAnimation;
    private Handler myHandler;
    private int count;
    private int tempRandom;
    private int delayMillis;
    private Runnable myRunnable;
    private String randomStr = "";
//    private Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        width = defaultDisplay.getWidth() - 300;
        height = defaultDisplay.getHeight() - 300;
        delayMillis = 200;
        random = new Random();
        setContentView(R.layout.activity_count_main);


        tvnum = (TextView) findViewById(R.id.tvnum);
        tvnumWidth = 200;
        tvnumHeight = 200;
        set = new AnimationSet(true);

        scaleAnimation = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setFillAfter(true);

        rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setInterpolator(new AccelerateInterpolator());
        rotateAnimation.setFillAfter(true);

        set.addAnimation(scaleAnimation);
        set.addAnimation(rotateAnimation);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvnum.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvnum.setVisibility(View.INVISIBLE);
                if (count <= 5) {
                    genRandomNumber();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(CountMainActivity.this, NextActivity.class);
                    intent.putExtra("data", randomStr);
//                    intent.putExtra("patient", patient);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Bundle bundle = getIntent().getExtras();
//        patient = (Patient) getIntent().getSerializableExtra("patient");
        count = 0;
        genRandomNumber();
    }

    public void genRandomNumber() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvnum.getLayoutParams();
        layoutParams.leftMargin = random.nextInt(width - 2 * tvnumWidth) + tvnumWidth;
        layoutParams.topMargin = random.nextInt(height - 2 * tvnumHeight) + tvnumHeight;
        tempRandom = random.nextInt(10);

        randomStr += tempRandom;

        tvnum.setLayoutParams(layoutParams);
        tvnum.setText(String.valueOf(tempRandom));

        myHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count++;
                tvnum.startAnimation(set);
            }
        }, delayMillis);
    }
}