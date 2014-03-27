package ca.pluszero.emotive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import ca.pluszero.emotive.models.Choice;

public class ChoiceDataSource {

    private final String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TIMES_TAPPED
    };
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ChoiceDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        // database = null; // TODO: Should I do this?
    }

    public Choice createChoice(Choice choice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TIMES_TAPPED, choice.getTimesTapped());
        String insertId = String.valueOf(database.insert(DatabaseHelper.CHOICE_TABLE_NAME, null, values));
        Cursor cursor = database.query(DatabaseHelper.CHOICE_TABLE_NAME, allColumns, DatabaseHelper.COLUMN_ID + " = ?" , new String[] {insertId}, null, null, null);
        cursor.moveToFirst();
        Choice databaseChoice = cursorToChoice(cursor);
        cursor.close();
        return databaseChoice;
    }

    private Choice cursorToChoice(Cursor cursor) {
        Choice choice = Choice.getEnumForTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
        return choice;
    }
}
