package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.History;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class HistoryProvider {
    private SQLiteDatabase mDatabase;
    private String[] mHistoryColumns = {DataBaseHelper.HISTORY_ID, DataBaseHelper.HISTORY_USER_ID, DataBaseHelper.HISTORY_TYPE, DataBaseHelper.HISTORY_FILE,
            DataBaseHelper.HISTORY_IS_UPLOADED, "datetime(history_create_time, 'localtime') as history_create_time"};

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

        return mDatabase.insert(DataBaseHelper.HISTORY_TABLE_NAME, null, values);
    }
    
    public ArrayList<History> getHistories() {
        Cursor cursor = mDatabase.query(DataBaseHelper.HISTORY_TABLE_NAME, mHistoryColumns, null, null, null, null, DataBaseHelper.HISTORY_ID);
        return loadHistoryFromCursor(cursor);
    }

    private ArrayList<History> loadHistoryFromCursor(Cursor cursor) {
        ArrayList<History> histories = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.HISTORY_ID));
                long userId = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.HISTORY_USER_ID));
                String type = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_TYPE));
                String file = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_FILE));
                boolean isUploaded = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HISTORY_IS_UPLOADED)) == 1;
                String createdTime = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_CREATE_TIME));
                histories.add(new History(id, userId, type, file, isUploaded, createdTime));
            }
            cursor.close();
        }
        return histories;
    }

    public  ArrayList<History> getHistoriesByIds(ArrayList<Long> ids) {
        ArrayList<History> histories = getHistories();
        Iterator<History> iterator = histories.iterator();
        while (iterator.hasNext()) {
            History history = iterator.next();
            if (!ids.contains(history.id)) iterator.remove();
        }
        return histories;
    }

    public ArrayList<Request> getUploadRequest(ArrayList<Long> ids) {
        ArrayList<History> histories = getHistoriesByIds(ids);
        ArrayList<Request> requests = new ArrayList<>();
        for (History history: histories) {
            String url = "http://web.ngrok.cc/cana-api/upload";
            File file = new File(history.filePath);
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data", file.getName(),
                            RequestBody.create(MediaType.parse("text/plain"), file))
                    .build();
            requests.add(new Request.Builder().url(url).post(formBody).build());
        }
        return requests;
    }
}
