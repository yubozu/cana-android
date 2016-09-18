package cn.ac.ict.cana.pages;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity;
import cn.ac.ict.cana.adapters.HistoryAdapter;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;
import cn.ac.ict.cana.widget.TreeView;
import okhttp3.Request;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class HistoryPage{

    static public View InitialHistoryPageView(final Context context){
        View view = View.inflate(context, R.layout.pageview_history, null);
        final HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(context));

        final TreeView treeView = (TreeView)view.findViewById(R.id.tree_view);
        ArrayList<ArrayList<ContentValues>> mChild = new ArrayList<>();
        for(int i=0;i<6;i++)
        {
            mChild.add(new ArrayList<ContentValues>());
        }
        ArrayList<String> mGroup = new ArrayList<> (Arrays.asList("Stride", "Face", "Sound", "Stand", "Tapping", "Recognition"));
        ArrayList<History> historyList = historyProvider.getHistories();

        for (History history: historyList) {
            ContentValues content = new ContentValues();
            content.put("id", history.id);
            content.put("user_id", history.userId);
            content.put("file", history.filePath);
            content.put("type", history.type);
            content.put("is_uploaded", history.isUpload);
            content.put("created_time", history.createdTime);
            Log.d("HistoryPage", String.valueOf(history.type));
            mChild.get(mGroup.indexOf(history.type)).add(content);
        }
        final HistoryAdapter historyAdapter = new HistoryAdapter(context,treeView,mGroup,mChild);
        treeView.setAdapter(historyAdapter);

        Button button = (Button) view.findViewById(R.id.bt_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ContentValues> Ids  = new ArrayList<>(historyAdapter.getCheckedIds());
                historyProvider.uploadHistories(Ids);

                MainActivity activity = (MainActivity) context;
                activity.showProgressBar(true, "Start Uploading..");
            }
        });
        return view;
    }
}

