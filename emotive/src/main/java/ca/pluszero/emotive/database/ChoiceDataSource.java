package ca.pluszero.emotive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public int updateChoice(Choice choice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TIMES_TAPPED, choice.getTimesTapped());
//        String insertId = String.valueOf(database.insert(DatabaseHelper.TABLE_CHOICES, null, values));
        return database.update(DatabaseHelper.TABLE_CHOICES, values, DatabaseHelper.COLUMN_TITLE + " = ?", new String[]{choice.getTitle()});
//        Cursor cursor = database.query(DatabaseHelper.TABLE_CHOICES, allColumns, DatabaseHelper.COLUMN_ID + " = ?" , new String[] {insertId}, null, null, null);
//        cursor.moveToFirst();
//        Choice databaseChoice = cursorToChoice(cursor);
//        cursor.close();
//        return databaseChoice;
    }

    public List<Choice> getAllChoices() {
        List<Choice> choices = new ArrayList<Choice>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CHOICES, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            choices.add(cursorToChoice(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return choices;
    }

    private Choice cursorToChoice(Cursor cursor) {
        Choice choice = Choice.getEnumForTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));
//        choice.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        return choice;
    }
}
