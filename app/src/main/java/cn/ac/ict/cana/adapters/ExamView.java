package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.Exam;

/**
 * Author: saukymo
 * Date: 9/13/16
 */


@EViewGroup(R.layout.adapter_exam)
public class ExamView extends LinearLayout {

    @ViewById(R.id.tv_exam_name) public TextView tvExamName;
    @ViewById(R.id.tv_exam_description) public TextView tvExamDescription;

    public Context mContext;

    public ExamView(Context context) {
        super(context);
        mContext = context;
    }

    public void bind(Exam exam) {
        Log.d("ExamView", String.valueOf(tvExamName));
        Log.d("ExamView", String.valueOf(exam.name));
        tvExamName.setText(exam.name);
        tvExamDescription.setText(exam.description);
    }
}