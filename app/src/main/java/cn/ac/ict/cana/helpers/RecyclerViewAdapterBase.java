package cn.ac.ict.cana.helpers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: saukymo
 * Date: 9/12/16
 * Reference: https://github.com/excilys/androidannotations/wiki/Adapters-and-lists
 */
public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> mItems = new ArrayList<T>();

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public void setList(List<T> list) {
        this.mItems = list;
    }
    // additional methods to manipulate the items
}