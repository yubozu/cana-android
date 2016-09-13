package cn.ac.ict.cana.pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
        ArrayList<ArrayList<Map>> mChild = new ArrayList<>();
        for(int i=0;i<6;i++)
        {
            mChild.add(new ArrayList<Map>());
        }
        ArrayList<String> mGroup = new ArrayList<> (Arrays.asList("Stride", "Face", "Sound", "Stand", "Tapping", "Recognition"));
        ArrayList<History> historyList = historyProvider.getHistories();

        for (History history: historyList) {
            Map<String,Object> map = new HashMap<>();
            map.put("id", history.id);
            map.put("user_id", history.userId);
            map.put("file", history.filePath);
            map.put("type", history.type);
            map.put("is_uploaded", history.isUpload);
            map.put("created_time", history.createdTime);
            Log.d("HistoryPage", String.valueOf(history.type));
            mChild.get(mGroup.indexOf(history.type)).add(map);
        }
        final HistoryAdapter historyAdapter = new HistoryAdapter(context,treeView,mGroup,mChild);
        treeView.setAdapter(historyAdapter);

        Button button = (Button) view.findViewById(R.id.bt_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Long> Ids  = new ArrayList<>(historyAdapter.getCheckedIds());
                ArrayList<Request> requests = historyProvider.getUploadRequest(Ids);

                MainActivity activity = (MainActivity) context;
                activity.startUpload(requests);

            }
        });
        return view;
    }
}

