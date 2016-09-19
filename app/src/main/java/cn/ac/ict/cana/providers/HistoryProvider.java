package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.ac.ict.cana.events.ResponseEvent;
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
    private OkHttpClient client;

    private String[] mHistoryColumns = {DataBaseHelper.HISTORY_ID, DataBaseHelper.HISTORY_USER_ID, DataBaseHelper.HISTORY_TYPE, DataBaseHelper.HISTORY_FILE,
            DataBaseHelper.HISTORY_IS_UPLOADED, "datetime(history_create_time, 'localtime') as history_create_time"};

    public HistoryProvider(DataBaseHelper dataBaseHelper) {
        mDatabase = dataBaseHelper.getWritableDatabase();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
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
        String idString = TextUtils.join(",", ids);
        String QueryString = String.format("SELECT * FROM " + DataBaseHelper.HISTORY_TABLE_NAME + " WHERE " +DataBaseHelper.HISTORY_ID + " IN (%s)", new String[]{idString});
        Log.d("QueryString", QueryString);
        Cursor cursor = mDatabase.rawQuery(QueryString, null);

        return loadHistoryFromCursor(cursor);
    }


    public void uploadHistories(ArrayList<ContentValues> items) {
        ArrayList<Long> ids = getIds(items);
        ArrayList<History> histories = getHistoriesByIds(ids);
        final int total = ids.size();
        for (int i = 0; i < items.size(); i++) {
            final History history = histories.get(i);
            ContentValues item = items.get(i);
            final int groupPosition = (int) item.get("groupPosition");
            final int childPosition = (int) item.get("childPosition");
            Log.d("GetUploadRequest", history.toString());
            String url = "http://web.ngrok.cc/cana-api/upload";
            File file = new File(history.filePath);
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data", file.getName(),
                            RequestBody.create(MediaType.parse("text/plain"), file))
                    .addFormDataPart("id", String.valueOf(history.id))
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Upload Failed", e.toString());
                    EventBus.getDefault().post(new ResponseEvent(false, history.id, total, groupPosition, childPosition));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    boolean result = false;
                    String jsonData = response.body().string();
                    try {
                        // TODO: Change to Gson
                        JSONObject Jobject = new JSONObject(jsonData);
                        Log.d("toJson", Jobject.toString());
                        Log.d("toJson", "ResponseId: " + Jobject.get("id").toString());
                        long historyId= Long.parseLong((String) Jobject.get("id"));
                        updateHistoryUploadedById(historyId);
                        result = true;
                    }catch (JSONException e) {
                        Log.e("toJson", e.toString());
                    }
                    EventBus.getDefault().post(new ResponseEvent(result, history.id, total, groupPosition, childPosition));
                }
            });
        }
    }

    public void updateHistoryUploadedById(Long id) {
        ContentValues args = new ContentValues();
        args.put(DataBaseHelper.HISTORY_IS_UPLOADED, 1);
        mDatabase.update(DataBaseHelper.HISTORY_TABLE_NAME,  args, "_id=" + id, null);
    }

    public void deleteHistories(ArrayList<ContentValues> items) {
        ArrayList<Long> ids = getIds(items);

        String idString = TextUtils.join(",", ids);
        String QueryString = String.format("DELETE FROM " +DataBaseHelper.HISTORY_TABLE_NAME + " WHERE " +DataBaseHelper.HISTORY_ID + " IN (%s)", new String[]{idString});

        mDatabase.execSQL(QueryString);
    }

    private ArrayList<Long> getIds(ArrayList<ContentValues> items) {
        ArrayList<Long> ids = new ArrayList<>();
        for (ContentValues item: items) {
            ids.add((Long) item.get("id"));
        }
        return ids;
    }
}
