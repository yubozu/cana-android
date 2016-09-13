package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.History;

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

    public long InsertHistory(History history) {

        //TODO: check whether history.userId exists.
        ContentValues values = new ContentValues();
        
        values.put(DataBaseHelper.HISTORY_USER_ID, history.userId);
        values.put(DataBaseHelper.HISTORY_TYPE, history.type);
        values.put(DataBaseHelper.HISTORY_FILE, history.filePath);
        values.put(DataBaseHelper.HISTORY_IS_UPLOADED, history.isUpload);

        long id = mDatabase.insert(DataBaseHelper.HISTORY_TABLE_NAME, null, values);
        return id;
    }
    
    public ArrayList<History> getHistory() {
        ArrayList<History> users = new ArrayList<>();
        Cursor cursor = mDatabase.query(DataBaseHelper.HISTORY_TABLE_NAME, mHistoryColumns, null, null, null, null, DataBaseHelper.HISTORY_ID);
        Log.d("HistoryProvider", String.valueOf(cursor.getCount()));
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.HISTORY_ID));
                long userId = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.HISTORY_USER_ID));
                String type = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_TYPE));
                String file = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_FILE));
                boolean isUploaded = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HISTORY_IS_UPLOADED)) == 1;
                String createdTime = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_CREATE_TIME));
                users.add(new History(id, userId, type, file, isUploaded, createdTime));
            }
            cursor.close();
        }
        Log.d("HistoryProvider", users.toString());
        return users;
    }
}
