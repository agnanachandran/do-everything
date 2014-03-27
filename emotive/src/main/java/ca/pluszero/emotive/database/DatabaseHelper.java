package ca.pluszero.emotive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ca.pluszero.emotive.models.Choice;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CHOICE_TABLE_NAME = "choices";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MAIN_INFO = "main_info";
    public static final String COLUMN_TIMES_TAPPED = "times_tapped";
    private static final String CREATE_CHOICE_TABLE_ON_CREATE = "CREATE TABLE " + CHOICE_TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TIMES_TAPPED + " integer not null " + COLUMN_NAME + " text not null " + COLUMN_MAIN_INFO + " text not null);";
    private static final String INCOMPLETE_INSERT_CHOICES_STATEMENT = "INSERT INTO " + CHOICE_TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_MAIN_INFO + ") VALUES (%s, %s);";
    private static final String DATABASE_NAME = "emotive.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Choices table and insert all the primary choices
        db.execSQL(CREATE_CHOICE_TABLE_ON_CREATE);
        for (Choice choice : Choice.values()) {
            db.execSQL(String.format(INCOMPLETE_INSERT_CHOICES_STATEMENT, choice.getTitle(), choice.getMainInfo()));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: migrate all data
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from v. " + oldVersion + " to v. "
                + newVersion + " which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
        onCreate(db);
    }
}
