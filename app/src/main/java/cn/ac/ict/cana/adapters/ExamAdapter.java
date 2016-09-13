package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.Exam;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EBean
public class ExamAdapter extends BaseAdapter {

    private Context mContext;
    public ExamAdapter(Context context){
        mContext = context;
    }

    private List<Exam> exams;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ExamView examView;
        if (view == null) {

            examView = ExamView_.build(mContext);
        } else {
            examView = (ExamView) view;
        }

        Exam exam = getItem(position);
        examView.bind(exam);

        examView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManager toastManager = new ToastManager(mContext);
                toastManager.show("Start exam");
            }
        });
        return examView;
    }

    public void setList(ArrayList<Exam> examlist) {
        exams = examlist;
    }
    @Override
    public int getCount() {
        return exams.size();
    }

    @Override
    public Exam getItem(int position) {
        return exams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Exam exam){
        exams.add(exam);
    }

}


