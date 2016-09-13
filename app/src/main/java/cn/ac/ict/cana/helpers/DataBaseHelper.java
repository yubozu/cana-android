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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldeVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE_NAME);

        onCreate(db);
    }

}
