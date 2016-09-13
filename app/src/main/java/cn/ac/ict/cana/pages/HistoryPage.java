package cn.ac.ict.cana.pages;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.adapters.HistoryAdapter;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.providers.HistoryProvider;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class HistoryPage{

    static public View InitialHistoryPageView(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.pageview_history, null, false);
        final HistoryProvider historyProvider = new HistoryProvider(DataBaseHelper.getInstance(context));

        final ArrayList<History> historyList = historyProvider.getHistory();
        Log.d("MainAdapter", historyList.toString());
        final ListView lvHistory = (ListView) view.findViewById(R.id.lv_history);
        final HistoryAdapter historyAdapter = new HistoryAdapter(context);

        historyAdapter.setList(historyList);
        lvHistory.setAdapter(historyAdapter);


        Button button = (Button) view.findViewById(R.id.bt_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManager toastManager = new ToastManager(context);
                toastManager.show("Upload to server.");
            }
        });
        return view;
    }
}

