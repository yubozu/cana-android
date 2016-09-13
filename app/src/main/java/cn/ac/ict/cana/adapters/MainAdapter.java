package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.User;
import cn.ac.ict.cana.pages.UserPage;
import cn.ac.ict.cana.providers.UserProvider;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EBean
public class MainAdapter extends PagerAdapter {

    @RootContext Context mContext;

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view;
        if (position != 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main, null, false);
            final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
            txtPage.setText(String.format(Locale.CHINA, "Page #%d", position));
        } else {
            view = UserPage.InitialUserPageView(mContext);
        }
        container.addView(view);
        return view;
    }

}
