package cn.ac.ict.cana.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ac.ict.cana.activities.UserActivity;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class Exam {
    public final String name;
    private Activity activity;

    @Deprecated
    public Exam(String examName) {
        name = examName;
    }

    public Exam(Builder builder) {

        this.name = builder.name;
        this.activity = builder.activity;
    }

    public void go(Context context) {
        Intent intent = new Intent();
//        intent.setClass(context, activity.getClass());
        intent.setClass(context, UserActivity.class);
        context.startActivity(intent);
    }

    public boolean hasActivity() {
        return activity != null;
    }

    public static class Builder {

        private String name;
        private Activity activity;

        public Builder() {
        }

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
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
