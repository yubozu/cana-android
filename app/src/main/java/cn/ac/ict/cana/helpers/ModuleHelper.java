package cn.ac.ict.cana.helpers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: saukymo
 * Date: 9/19/16
 */
public class ModuleHelper {
    final static String MODULE_COUNT = "Count";
    final static String MODULE_FACE = "Face";
    final static String MODULE_SOUND = "Sound";
    final static String MODULE_STAND = "Stand";
    final static String MODULE_STRIDE = "Stride";
    final static String MODULE_TAPPING = "Tapping";

    public final static ArrayList<String> ModuleList= new ArrayList<>(Arrays.asList(MODULE_COUNT, MODULE_FACE, MODULE_SOUND, MODULE_STAND, MODULE_STRIDE, MODULE_TAPPING));
}
