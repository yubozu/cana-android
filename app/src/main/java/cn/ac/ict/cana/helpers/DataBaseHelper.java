package cn.ac.ict.cana.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    // database
    public static final String DB_NAME = "cana.db";
    public static final int DB_VERSION = 1;

    // User-info table
    public static final String USER_TABLE_NAME = "user_table";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_AGE = "user_age";
    public static final String USER_GENDER = "user_gender";

    private static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE_NAME + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_NAME + " CHAR(256) NOT NULL,"
            + USER_AGE + " INTEGER NOT NULL,"
            + USER_GENDER + " INTEGER NOT NULL"
            + ");";

    // History table
    public static final String HISTORY_TABLE_NAME = "history_table";
    public static final String HISTORY_ID = "_id";
    public static final String HISTORY_USER_ID = "history_user_id";
    public static final String HISTORY_TYPE = "history_type";
    public static final String HISTORY_FILE = "history_file";
    public static final String HISTORY_IS_UPLOADED = "history_is_uploaded";
    public static final String HISTORY_CREATE_TIME = "history_create_time";

    private static final String HISTORY_TABLE_CREATE = "CREATE TABLE " + HISTORY_TABLE_NAME + "("
            + HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + HISTORY_USER_ID + " INTEGER NOT NULL,"
            + HISTORY_TYPE + " CHAR(20) NOT NULL,"
            + HISTORY_FILE + " CHAR(256) NOT NULL,"
            + HISTORY_IS_UPLOADED + " INTEGER NOT NULL,"
            + HISTORY_CREATE_TIME + " TIMESTAMP NOT NULL DEFAULT current_timestamp"
            + ");";

    private volatile static DataBaseHelper mDataBaseHelper;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (mDataBaseHelper == null) {
            synchronized (DataBaseHelper.class) {
                if (mDataBaseHelper == null) {
                    mDataBaseHelper = new DataBaseHelper(context);
                }
            }
        }

        return mDataBaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(HISTORY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need.
    }

}
