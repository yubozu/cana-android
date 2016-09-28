package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.ac.ict.cana.events.CancelUploadEvent;
import cn.ac.ict.cana.events.ResponseEvent;
import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.History;
import cn.ac.ict.cana.models.User;
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
    private UserProvider userProvider;
    private String baseUrl = "b4cce327.ngrok.io";
    private String[] mHistoryColumns = {DataBaseHelper.HISTORY_ID, DataBaseHelper.HISTORY_USER_UUID, DataBaseHelper.HISTORY_TYPE, DataBaseHelper.HISTORY_FILE,
            DataBaseHelper.HISTORY_IS_UPLOADED, "datetime(history_create_time, 'localtime') as history_create_time"};
    private int total;
    private ArrayList<Call> uploadCall;

    public HistoryProvider(DataBaseHelper dataBaseHelper) {
        userProvider = new UserProvider(dataBaseHelper);
        mDatabase = dataBaseHelper.getWritableDatabase();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public long InsertHistory(History history) {

        //TODO: check whether history.userId exists.
        ContentValues values = new ContentValues();
        
        values.put(DataBaseHelper.HISTORY_USER_UUID, history.uuid);
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
                String uuid = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_USER_UUID));
                String type = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_TYPE));
                String file = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_FILE));
                boolean isUploaded = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HISTORY_IS_UPLOADED)) == 1;
                String createdTime = cursor.getString(cursor.getColumnIndex(DataBaseHelper.HISTORY_CREATE_TIME));
                histories.add(new History(id, uuid, type, file, isUploaded, createdTime));
            }
            cursor.close();
        }
        return histories;
    }

    private ArrayList<History> getHistoriesByIds(ArrayList<Long> ids) {
        String idString = TextUtils.join(",", ids);
        String QueryString = String.format("SELECT * FROM " + DataBaseHelper.HISTORY_TABLE_NAME + " WHERE " +DataBaseHelper.HISTORY_ID + " IN (%s)", new String[]{idString});
        Log.d("HistoryProvider", "QueryString" + QueryString);
        Cursor cursor = mDatabase.rawQuery(QueryString, null);

        return loadHistoryFromCursor(cursor);
    }

    public History getHistory(Long id) {
        Cursor cursor = mDatabase.query(DataBaseHelper.HISTORY_TABLE_NAME, mHistoryColumns, "_id =" + id, null, null, null, null);
        return loadHistoryFromCursor(cursor).get(0);
    }

    private User getUser(ArrayList<User> users, History history) {
        for (int j = 0; j < users.size(); j++) {
            if (history.uuid.equals(users.get(j).uuid)) {
                return users.get(j);
            }
        }
        return null;
    }

    public void uploadHistories(ArrayList<ContentValues> items) {
        ArrayList<Long> ids = getIds(items);
        ArrayList<History> histories = getHistoriesByIds(ids);
        ArrayList<String> uuids = getUuids(histories);
        ArrayList<User> users = userProvider.getUsersByUuids(uuids);
        uploadCall = new ArrayList<>();
        total = items.size();
        for (int i = 0; i < items.size(); i++) {
            Log.d("uploadHistories", "Uploading #" + String.valueOf(i));
            final History history = histories.get(i);
            final User user = getUser(users, history);

            ContentValues item = items.get(i);

            final int groupPosition = (int) item.get("groupPosition");
            final int childPosition = (int) item.get("childPosition");
            if (user == null) {
                EventBus.getDefault().post(new ResponseEvent(false, history.id, total, groupPosition, childPosition));
                continue;
            }
            Log.d("GetUploadRequest", "Filepath: " + history.filePath + "; User name: " + user.name + ";");
            String url = "http://" + baseUrl + "/cana-api/upload";
            File file = new File(history.filePath);
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data", file.getName(),
                            RequestBody.create(MediaType.parse("text/plain"), file))
                    .addFormDataPart("uuid", user.uuid)
                    .addFormDataPart("name", user.name)
                    .addFormDataPart("gender", user.gender?"Female":"Male")
                    .addFormDataPart("date", history.createdTime)
                    .addFormDataPart("type", history.type)
                    // TODO: fill in all information. Check with content write in file.
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    uploadCall.remove(call);
                    Log.e("Upload Failed", e.toString());
                    EventBus.getDefault().post(new ResponseEvent(false, history.id, total, groupPosition, childPosition));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    uploadCall.remove(call);
                    boolean result = false;
                    String jsonData = response.body().string();
                    try {
                        // TODO: Change to Gson
                        JSONObject Jobject = new JSONObject(jsonData);
                        Log.d("toJson", Jobject.toString());
                        updateHistoryUploadedById(history.id);
                        result = true;
                    }catch (JSONException e) {
                        Log.e("toJson", e.toString());
                    }
                    EventBus.getDefault().post(new ResponseEvent(result, history.id, total, groupPosition, childPosition));
                }
            });
            uploadCall.add(call);
        }
    }

    private void updateHistoryUploadedById(Long id) {
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

    private ArrayList<String> getUuids(ArrayList<History> histories) {
        Log.d("HistoryProvider", "getUuids");
        ArrayList<String> uuids = new ArrayList<>();
        for (History history: histories) {
            uuids.add(history.uuid);
        }
        return uuids;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelUpload(CancelUploadEvent event){
        for (Call call: uploadCall){
            call.cancel();
        }
    }
}
