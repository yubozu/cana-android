package cn.ac.ict.cana.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private String[] mUserColumns = {DataBaseHelper.USER_ID, DataBaseHelper.USER_NAME, DataBaseHelper.USER_AGE, DataBaseHelper.USER_GENDER};

    public UserProvider(DataBaseHelper dataBaseHelper) {
        mDatabase = dataBaseHelper.getWritableDatabase();
    }

    public long InsertUser(User user) {
        ContentValues values = new ContentValues();

//        values.put(DataBaseHelper.USER_ID, user.id);
        values.put(DataBaseHelper.USER_NAME, user.name);
        values.put(DataBaseHelper.USER_AGE, user.age);
        values.put(DataBaseHelper.USER_GENDER, user.gender?1 : 0);

        long id = mDatabase.insert(DataBaseHelper.USER_TABLE_NAME, null, values);
        return id;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = mDatabase.query(DataBaseHelper.USER_TABLE_NAME, mUserColumns, null, null, null, null, DataBaseHelper.USER_ID);
        Log.d("UserProvider", String.valueOf(cursor.getCount()));
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                users.add(new User(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.USER_ID)), cursor.getString(cursor.getColumnIndex(DataBaseHelper.USER_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DataBaseHelper.USER_AGE)), cursor.getInt(cursor.getColumnIndex(DataBaseHelper.USER_GENDER)) == 1));
            }
            cursor.close();
        }
        Log.d("UserProvider", users.toString());
        return users;
    }

}
