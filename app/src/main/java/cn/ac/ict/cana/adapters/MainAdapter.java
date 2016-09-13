package cn.ac.ict.cana.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Locale;

import cn.ac.ict.cana.R;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.pages.ExamPage;
import cn.ac.ict.cana.pages.HistoryPage;
import cn.ac.ict.cana.pages.UserPage;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
@EBean
public class MainAdapter extends PagerAdapter {

    @RootContext Context mContext;
    public View view;

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
        switch (position) {
            case 0:
                view = ExamPage.InitialExamPageView(mContext);
                Log.d("MainAdapter exam", String.valueOf(mContext));
                break;
            case 1:
                view = UserPage.InitialUserPageView(mContext);
                Log.d("MainAdapter user", String.valueOf(mContext));
                break;
            case 2:
                view = HistoryPage.InitialHistoryPageView(mContext);
                break;
            case 3:
                view = LayoutInflater.from(mContext).inflate(R.layout.pageview_setting, null, false);
                break;
//            default:
//                view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main, null, false);
//                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
//                txtPage.setText(String.format(Locale.CHINA, "Page #%d", position));
        }

        container.addView(view);
        return view;
    }

}
