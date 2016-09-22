package cn.ac.ict.cana.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ac.ict.cana.activities.MainActivity_;
import cn.ac.ict.cana.modules.count.StartActivity;
import cn.ac.ict.cana.modules.stride.StrideMainActivity;

/**
 * Author: saukymo
 * Date: 9/19/16
 */
public class ModuleHelper {
    public final static String MODULE_COUNT = "Count";
    public final static String MODULE_FACE = "Face";
    public final static String MODULE_SOUND = "Sound";
    public final static String MODULE_STAND = "Stand";
    public final static String MODULE_STRIDE = "Stride";
    public final static String MODULE_TAPPING = "Tapping";

    public final static ArrayList<String> ModuleList= new ArrayList<>(Arrays.asList(MODULE_COUNT, MODULE_FACE, MODULE_SOUND, MODULE_STAND, MODULE_STRIDE, MODULE_TAPPING));

    public static Class getModule(String moduleName) {
        Class module;
        switch (moduleName) {
            case MODULE_COUNT:
                module = StartActivity.class;
                break;
            case MODULE_STRIDE:
                module = StrideMainActivity.class;
                break;
            default:
                module = MainActivity_.class;
        }
        return module;
    }
}
