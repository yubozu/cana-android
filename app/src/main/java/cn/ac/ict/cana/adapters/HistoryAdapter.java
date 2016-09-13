package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.helpers.ToastManager;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.widget.BaseTreeViewAdapter;
import cn.ac.ict.cana.widget.TreeView;

/**
 * Author: saukymo
 * Date: 9/13/16
 */

public class HistoryAdapter extends BaseTreeViewAdapter {
    private LayoutInflater mInflater;
    ArrayList<String> mGroups;
    ArrayList<ArrayList<Map>> mChildren;
    private final Set<Long> mCheckedItems = new HashSet<>();
    private Context mContext;

    public HistoryAdapter(Context context, TreeView treeView, ArrayList<String> mGroups, ArrayList<ArrayList<Map>> mChildren) {
        super(treeView);
        this.mGroups = mGroups;
        this.mChildren = mChildren;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildren.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildren.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class ChildHolder {
        TextView tvHistoryId;
        TextView tvHistoryUserId;
        TextView tvHistoryType;
        TextView tvHistoryFile;
        TextView tvHistoryIsUploaded;
        TextView tvHistoryCreatedTime;
        CheckBox cbHistory;

        public ChildHolder(View view) {
            tvHistoryId = (TextView) view. findViewById(R.id.tv_history_id);
            tvHistoryUserId = (TextView) view.findViewById(R.id.tv_history_user_id);
            tvHistoryType = (TextView) view. findViewById(R.id.tv_history_type);
            tvHistoryFile = (TextView) view.findViewById(R.id.tv_history_file);
            tvHistoryIsUploaded = (TextView) view. findViewById(R.id.tv_history_is_upload);
            tvHistoryCreatedTime = (TextView) view.findViewById(R.id.tv_history_created_time);
            cbHistory = (CheckBox) view.findViewById(R.id.cb_history);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_history, null);
        }

        Map content = mChildren.get(groupPosition).get(childPosition);
        ChildHolder holder = getChildHolder(convertView);

        // In android Studio, these have to extract a new variable;
        final long id = (long) content.get("id");
        final long userId = (long) content.get("user_id");
        final boolean isUploaded = (boolean) content.get("is_uploaded");
        holder.tvHistoryId.setText(String.valueOf(id));
        holder.tvHistoryUserId.setText(String.valueOf(userId));
        holder.tvHistoryType.setText((String) content.get("type"));
        holder.tvHistoryFile.setText((String) content.get("file"));
        holder.tvHistoryIsUploaded.setText(String.valueOf(isUploaded));
        holder.tvHistoryCreatedTime.setText((String) content.get("created_time"));


        holder.cbHistory.setChecked(mCheckedItems.contains(id));
        holder.cbHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    mCheckedItems.add(id);
                } else {
                    mCheckedItems.remove(id);
                }
            }
        });

        return convertView;
    }

    public Set<Long> getCheckedIds() {
        return mCheckedItems;
    }

    private ChildHolder getChildHolder(final View view) {
        ChildHolder holder = (ChildHolder) view.getTag();
        if (null == holder) {
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        return holder;
    }


    private class GroupHolder {
        TextView name;
        ImageView indicator;
        TextView onlineNum;

        public GroupHolder(View view) {
            name = (TextView) view.findViewById(R.id.group_name);
            indicator = (ImageView) view.findViewById(R.id.group_indicator);
            onlineNum = (TextView) view.findViewById(R.id.online_count);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_group_history, null);
        }

        GroupHolder holder = getGroupHolder(convertView);

        holder.name.setText(mGroups.get(groupPosition));
        holder.onlineNum.setText(String.format(Locale.CHINA, "%s", getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition)));
        if (isExpanded) {
            holder.indicator.setImageResource(R.drawable.indicator_expanded);
        } else {
            holder.indicator.setImageResource(R.drawable.indicator_unexpanded);
        }

        return convertView;
    }

    private GroupHolder getGroupHolder(final View view) {
        GroupHolder holder = (GroupHolder) view.getTag();
        if (null == holder) {
            holder = new GroupHolder(view);
            view.setTag(holder);
        }
        return holder;
    }


    @Override
    public void updateHeader(View header, int groupPosition, int childPosition, int alpha) {
        ((TextView) header.findViewById(R.id.group_name)).setText(mGroups.get(groupPosition));
        ((TextView) header.findViewById(R.id.online_count)).setText(String.format(Locale.CHINA, "%s", getChildrenCount
                (groupPosition) + "/" + getChildrenCount(groupPosition)));
        header.setAlpha(alpha);
    }
}
