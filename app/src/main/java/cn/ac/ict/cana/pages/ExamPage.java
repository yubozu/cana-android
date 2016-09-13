package cn.ac.ict.cana.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.ExamAdapter;
import cn.ac.ict.cana.models.Exam;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class ExamPage {

    static public View InitialExamPageView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.pageview_exam, null, false);

        final ListView lvExam = (ListView) view.findViewById(R.id.lv_exam);
        ArrayList<Exam> examList = new ArrayList<>();
        final ExamAdapter examAdapter = new ExamAdapter(context);

        examList.add(new Exam("stride", "stride description"));

        examAdapter.setList(examList);
        lvExam.setAdapter(examAdapter);

        return view;
    }
}
