package cn.ac.ict.cana.providers;

import android.database.sqlite.SQLiteDatabase;

import cn.ac.ict.cana.helpers.DataBaseHelper;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class HistoryProvider {
    private SQLiteDatabase mDatabase;
    private String[] mHistoryColumns = {DataBaseHelper.HISTORY_ID, DataBaseHelper.HISTORY_USER_ID, DataBaseHelper.HISTORY_TYPE, DataBaseHelper.HISTORY_FILE,
            DataBaseHelper.HISTORY_IS_UPLOADED, DataBaseHelper.HISTORY_CREATE_TIME};

    public HistoryProvider(DataBaseHelper dataBaseHelper) {
        mDatabase = dataBaseHelper.getWritableDatabase();
    }


}
