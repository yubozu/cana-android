package cn.ac.ict.cana.modules.stride;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.utils.FloatVector;

import static java.lang.Boolean.FALSE;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class StrideEvaluation {
    public StrideEvaluation() {

    }

    static public String evaluation(History history,Context context){
        int trailCount = 0;
        int currentTrail = 0;
        boolean isAcc = FALSE;
        ArrayList<FloatVector> accFloatVectors = null;
        ArrayList<FloatVector> gyroFloatVectors = null;
        ArrayList<ArrayList<FloatVector>> accList = new ArrayList<>();
        ArrayList<ArrayList<FloatVector>> gyroList = new ArrayList<>();
//        Do something here.
        try {
            FileReader reader = new FileReader(history.filePath);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            trailCount = Integer.parseInt(line);
            line = br.readLine();
            while(line!=null && !line.isEmpty())
            {
                if(line.startsWith("ACC"))
                {
                    currentTrail++;
                    if(accFloatVectors!=null)
                    {
                     accList.add(accFloatVectors);
                    }
                    accFloatVectors = new ArrayList<>();
                    isAcc = true;
                }else if (line.startsWith("GYRO"))
                {
                    if(gyroFloatVectors!=null)
                    {
                        gyroList.add(gyroFloatVectors);
                    }
                    gyroFloatVectors = new ArrayList<>();
                    isAcc = false;
                }else
                {
                    String[] t = line.split(",");
                    FloatVector vector = new FloatVector();
                    vector.timeStamp = Long.parseLong(t[0].trim());
                    vector.x = Float.parseFloat(t[1].trim());
                    vector.y = Float.parseFloat(t[2].trim());
                    vector.z = Float.parseFloat(t[3].trim());
                    if(isAcc)
                    {
                        accFloatVectors.add(vector);
                    }else
                    {
                        gyroFloatVectors.add(vector);
                    }

                }
                line = br.readLine();

            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(accFloatVectors!=null)
            {
                accList.add(accFloatVectors);
            }
            if(gyroFloatVectors!=null)
            {
                gyroList.add(gyroFloatVectors);
            }
        }

        return context.getString(R.string.default_feedback);
    }

}
