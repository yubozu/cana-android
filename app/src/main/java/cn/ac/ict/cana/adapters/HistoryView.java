package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.History;

/**
 * Author: saukymo
 * Date: 9/13/16
 */

@EViewGroup(R.layout.adapter_history)
public class HistoryView extends LinearLayout {

    @ViewById(R.id.tv_history_id) public TextView tvHistoryId;
    @ViewById(R.id.tv_history_user_id) public TextView tvHistoryUserId;
    @ViewById(R.id.tv_history_type) public TextView tvHistoryType;
    @ViewById(R.id.tv_history_file) public TextView tvHistoryFile;
    @ViewById(R.id.tv_history_is_upload) public TextView tvHistoryIsUpload;
    @ViewById(R.id.tv_history_created_time) public TextView tvHistoryCreatedTime;
    public Context mContext;

    public HistoryView(Context context) {
        super(context);
        mContext = context;
    }

    public void bind(History history) {
        tvHistoryId.setText(String.valueOf(history.id));
        tvHistoryUserId.setText(String.valueOf(history.userId));
        tvHistoryType.setText(history.type);
        tvHistoryFile.setText(history.filePath);
        tvHistoryIsUpload.setText(String.valueOf(history.isUpload));
        tvHistoryCreatedTime.setText(history.createdTime);
    }
}
