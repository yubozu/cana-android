package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import org.androidannotations.annotations.EBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.Exam;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;

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

        Button btAddHistory = (Button) examView.findViewById(R.id.bt_add_history);
        btAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(mContext));
                History history = new History(mContext, 0, "Stride");

                // Example: How to write data to file.
                File file = new File(history.filePath);
                try {
                    FileWriter fileWrite = new FileWriter(file, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);

                    bufferedWriter.write("Test data.");

                    //Important! Have a new line in the end of txt file.
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    fileWrite.close();
                } catch (IOException e) {
                    Log.e("ExamAdapter", e.toString());
                }

                historyProvider.InsertHistory(history);
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


