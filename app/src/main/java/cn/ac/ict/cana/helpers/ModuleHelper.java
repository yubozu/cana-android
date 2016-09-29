package cn.ac.ict.cana.helpers;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.FeedBackActivity;
import cn.ac.ict.cana.activities.MainActivity_;
import cn.ac.ict.cana.modules.count.CountEvaluation;
import cn.ac.ict.cana.modules.count.CountMainActivity;
import cn.ac.ict.cana.modules.face.FaceEvaluation;
import cn.ac.ict.cana.modules.face.FaceMainActivity;
import cn.ac.ict.cana.modules.sound.SoundEvaluation;
import cn.ac.ict.cana.modules.sound.SoundMainActivity;
import cn.ac.ict.cana.modules.stand.StandEvaluation;
import cn.ac.ict.cana.modules.stand.StandMainActivity;
import cn.ac.ict.cana.modules.stride.StrideEvaluation;
import cn.ac.ict.cana.modules.stride.StrideMainActivity;
import cn.ac.ict.cana.modules.tapper.TapperEvaluation;
import cn.ac.ict.cana.modules.tapper.TapperMainActivity;

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
    public final static String MODULE_TAPPER = "Tapper";

    public final static ArrayList<String> ModuleList= new ArrayList<>(Arrays.asList(MODULE_COUNT, MODULE_FACE, MODULE_SOUND, MODULE_STAND, MODULE_STRIDE, MODULE_TAPPER));

    public static String getName(Context context, String moduleName){
        switch (moduleName) {
            case MODULE_COUNT:
                return context.getString(R.string.module_count);
            case MODULE_FACE:
                return context.getString(R.string.module_face);
            case MODULE_SOUND:
                return context.getString(R.string.module_sound);
            case MODULE_STAND:
                return context.getString(R.string.module_stand);
            case MODULE_STRIDE:
                return context.getString(R.string.module_stride);
            case MODULE_TAPPER:
                return context.getString(R.string.module_tapper);
            default:
                return "";
        }

    }

    public static Class getModule(String moduleName) {
        Class module;
        switch (moduleName) {
            case MODULE_COUNT:
                module = CountMainActivity.class;
                break;
            case MODULE_STRIDE:
                module = StrideMainActivity.class;
                break;
            case MODULE_STAND:
                module = StandMainActivity.class;
                break;
            case MODULE_FACE:
                module = FaceMainActivity.class;
                break;
            case MODULE_TAPPER:
                module = TapperMainActivity.class;
                break;
            case MODULE_SOUND:
                module = SoundMainActivity.class;
                break;
            default:
                module = MainActivity_.class;
        }
        return module;
    }

    public static Class getActivityAfterExam(){
        return FeedBackActivity.class;
    }

    public static Class getEvaluation(String moduleName) {
        Class module;
        switch (moduleName) {
            case MODULE_COUNT:
                module = CountEvaluation.class;
                break;
            case MODULE_STRIDE:
                module = StrideEvaluation.class;
                break;
            case MODULE_STAND:
                module = StandEvaluation.class;
                break;
            case MODULE_FACE:
                module = FaceEvaluation.class;
                break;
            case MODULE_TAPPER:
                module = TapperEvaluation.class;
                break;
            case MODULE_SOUND:
                module = SoundEvaluation.class;
                break;
            default:
                throw new Resources.NotFoundException();
        }
        return module;
    }
}
