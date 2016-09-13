package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.History;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EBean
public class HistoryAdapter extends BaseAdapter {

    private Context mContext;
    public HistoryAdapter(Context context){
        mContext = context;
    }

    private List<History> Exams;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final HistoryView ExamView;
        if (view == null) {

            ExamView = HistoryView_.build(mContext);
        } else {
            ExamView = (HistoryView) view;
        }

        History Exam = getItem(position);
        ExamView.bind(Exam);

        ExamView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManager toastManager = new ToastManager(mContext);
                toastManager.show("Show detailed");
            }
        });
        return ExamView;
    }

    public void setList(ArrayList<History> Examlist) {
        Exams = Examlist;
    }
    @Override
    public int getCount() {
        return Exams.size();
    }

    @Override
    public History getItem(int position) {
        return Exams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(History Exam){
        Exams.add(Exam);
    }

}
