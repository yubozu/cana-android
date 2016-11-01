package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import cn.ac.ict.cana.helpers.DataBaseHelper;
import cn.ac.ict.cana.models.User;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class UserProvider {

    private SQLiteDatabase mDatabase;
    private String[] mUserColumns = {DataBaseHelper.USER_ID, DataBaseHelper.USER_UUID,
            DataBaseHelper.USER_NAME, DataBaseHelper.USER_AGE, DataBaseHelper.USER_GENDER,
            DataBaseHelper.USER_CLINICAL_NUMBER, DataBaseHelper.USER_IDENTIFICATION, DataBaseHelper.USER_STUDY_NUMBER
    };

    public UserProvider(DataBaseHelper dataBaseHelper) {
        mDatabase = dataBaseHelper.getWritableDatabase();
    }

    public long insertUser(User user) {
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.USER_UUID, user.uuid);
        values.put(DataBaseHelper.USER_NAME, user.name);
        values.put(DataBaseHelper.USER_AGE, user.age);
        values.put(DataBaseHelper.USER_GENDER, user.gender?1 : 0);
        values.put(DataBaseHelper.USER_CLINICAL_NUMBER, user.clinicalNumber);
        values.put(DataBaseHelper.USER_IDENTIFICATION, user.identification);
        values.put(DataBaseHelper.USER_STUDY_NUMBER, user.studyNumber);
        return mDatabase.insert(DataBaseHelper.USER_TABLE_NAME, null, values);
    }

    public void updateUser(User user) {
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.USER_UUID, user.uuid);
        values.put(DataBaseHelper.USER_NAME, user.name);
        values.put(DataBaseHelper.USER_AGE, user.age);
        values.put(DataBaseHelper.USER_GENDER, user.gender?1 : 0);
        values.put(DataBaseHelper.USER_CLINICAL_NUMBER, user.clinicalNumber);
        values.put(DataBaseHelper.USER_IDENTIFICATION, user.identification);
        values.put(DataBaseHelper.USER_STUDY_NUMBER, user.studyNumber);
        mDatabase.update(DataBaseHelper.USER_TABLE_NAME, values, "_id=" + user.id, null);
    }


    public ArrayList<User> getUsers() {
        Cursor cursor = mDatabase.query(DataBaseHelper.USER_TABLE_NAME, mUserColumns, null, null, null, null, DataBaseHelper.USER_ID);
        Log.d("UserProvider", "Total user number: " + String.valueOf(cursor.getCount()));
        return loadUsersFromCursor(cursor);
    }

    private ArrayList<User> loadUsersFromCursor(Cursor cursor) {
        ArrayList<User> users = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.USER_ID));
                String uuid = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_UUID));
                String name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.USER_AGE));
                boolean gender = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.USER_GENDER)) == 1;
                String clinicalNumber = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_CLINICAL_NUMBER));
                String studyNumber = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_STUDY_NUMBER));
                String identification = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_IDENTIFICATION));
                users.add(new User(id, uuid, name, age, gender, clinicalNumber, studyNumber, identification));
            }
            cursor.close();
        }
        return users;
    }

    ArrayList<User> getUsersByUuids(ArrayList<String> uuids) {
        Log.d("UserProvider", "getUsersByUuids");
        ArrayList<String> newUuids = new ArrayList<>();
        for (String uuid: uuids) {
            newUuids.add("'" + uuid + "'");
        }

        String uuidString = TextUtils.join(",", newUuids);
        String QueryString = String.format("SELECT * FROM " + DataBaseHelper.USER_TABLE_NAME + " WHERE " +DataBaseHelper.USER_UUID + " IN (%s)", new String[]{uuidString});

        Log.d("getUsersByUuids", QueryString);
        Cursor cursor = mDatabase.rawQuery(QueryString, null);
        return loadUsersFromCursor(cursor);
    }

    public User getUserByUuid(String uuid) {
        Log.d("UserProvider", "getUserByUuid");
        String QueryString = String.format("SELECT * FROM " + DataBaseHelper.USER_TABLE_NAME + " WHERE " +DataBaseHelper.USER_UUID + " = '%s'", uuid);

        Log.d("getUsersByUuids", QueryString);
        Cursor cursor = mDatabase.rawQuery(QueryString, null);
        return loadUsersFromCursor(cursor).get(0);
    }

    public String getUsernameByUuid(String uuid){
        String username = "";
        String QueryString = String.format("SELECT " + DataBaseHelper.USER_NAME + " FROM " + DataBaseHelper.USER_TABLE_NAME + " WHERE " +DataBaseHelper.USER_UUID + " = '%s'", uuid);
        Cursor cursor = mDatabase.rawQuery(QueryString, null);
        if (cursor.moveToNext()) {
            username = cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_NAME));
        }
        cursor.close();
        return username;
    }

    public void deleteUser(User user) {

        String QueryString = String.format("DELETE FROM " +DataBaseHelper.USER_TABLE_NAME + " WHERE " +DataBaseHelper.USER_ID + "== %d", user.id);
        mDatabase.execSQL(QueryString);
    }

}
