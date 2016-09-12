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

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V mView;

    public ViewWrapper(V view) {
        super(view);
        mView = view;
    }

    public V getView() {
        return mView;
    }
}

