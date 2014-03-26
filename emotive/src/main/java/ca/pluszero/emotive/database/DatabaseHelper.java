package ca.pluszero.emotive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CHOICE_TABLE_NAME = "choices";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMES_TAPPED = "times_tapped";
    private static final String DATABASE_CREATE = "create table " + CHOICE_TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TIMES_TAPPED + " integer not null);";
    private static final String DATABASE_NAME = "emotive.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from v. " + oldVersion + " to v. "
                + newVersion + " which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
        onCreate(db);
    }
}
