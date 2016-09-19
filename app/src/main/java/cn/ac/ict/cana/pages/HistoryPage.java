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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.activities.MainActivity;
import cn.ac.ict.cana.adapters.HistoryAdapter;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ModuleHelper;
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
        ArrayList<ArrayList<History>> mChild = new ArrayList<ArrayList<History>>(){{
                for (int i=0; i<ModuleHelper.ModuleList.size(); i++) {
                    add(new ArrayList<History>());
                }}
        };
        ArrayList<String> mGroup = ModuleHelper.ModuleList;
        ArrayList<History> historyList = historyProvider.getHistories();

        for (History history: historyList) {
            mChild.get(mGroup.indexOf(history.type)).add(history);
        }
        final HistoryAdapter historyAdapter = new HistoryAdapter(context,treeView,mGroup,mChild);
        treeView.setAdapter(historyAdapter);

        Button uploadButton = (Button) view.findViewById(R.id.bt_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ContentValues> Ids  = new ArrayList<>(historyAdapter.getCheckedIds());
                historyProvider.uploadHistories(Ids);

                MainActivity activity = (MainActivity) context;
                activity.showProgressBar(true, "Start Uploading..");
            }
        });
        uploadButton.setEnabled(false);

        Button deleteButton = (Button) view.findViewById(R.id.bt_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContentValues> Ids  = new ArrayList<>(historyAdapter.getCheckedIds());
                historyProvider.deleteHistories(Ids);

                historyAdapter.removeItems(Ids);
            }
        });
        return view;
    }
}

