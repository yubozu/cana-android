package cn.ac.ict.cana.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.ac.ict.cana.activities.MainActivity;
import cn.ac.ict.cana.activities.UserActivity;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class Exam {
    public final String name;
    SharedPreferences settings;

    @Deprecated
    public Exam(String examName) {
        name = examName;
    }

    public Exam(Builder builder) {
        this.name = builder.name;
    }

    public void go(Context context) {
        Intent intent = new Intent();

        settings = context.getSharedPreferences("Cana", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings .edit();

        editor.putString("ModuleName", name);
        editor.apply();

        intent.setClass(context, UserActivity.class);
        context.startActivity(intent);

    }

    public static class Builder {

        private String name;
        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Exam build() {
            return new Exam(this);
        }
    }
}
