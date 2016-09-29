package cn.ac.ict.cana.modules.count;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.ac.ict.cana.models.History;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class CountEvaluation {
    public CountEvaluation() {

    }

    static public String evaluation(History history){

//        Do something here.

        boolean isRight = false;
        String rightAnswer = "";
        String linet = "";
        ArrayList<String> tryInput = new ArrayList<String>();

        try{
            FileReader reader = new FileReader(history.filePath);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            linet = line;
            String[] strings = line.trim().split(";");
            rightAnswer = strings[0].trim();
            isRight = strings[1].trim().equals("0")?false:true;
            for(int i=2;i<strings.length;i++){
                tryInput.add(strings[i]);
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }

//        return rightAnswer+":"+isRight+":"+linet;
        return linet;

    }
}
