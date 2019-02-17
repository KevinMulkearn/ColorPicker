package com.mulkearn.kevin.colorpicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;  // Version needs to be update if DB is updated
    private static final String DATABASE_NAME = "colorpicker.db";  // File name on device
    private static final String TABLE_NAME = "SavedColors";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_COLOR = "ColorHex";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Is called the very first time the app runs
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_COLOR + " TEXT " +
                ");";
        db.execSQL(query);  // Execute SQL query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add a new entry (row) to database
     */
    public boolean addColor(String item){
        SQLiteDatabase db = this.getWritableDatabase(); // Get db connection
        ContentValues values = new ContentValues();
        values.put(COLUMN_COLOR, item);

        long result = db.insert(TABLE_NAME, null, values);  // Insert new row in table

        if (result == -1) {
            return false;
        } else {
            // Limit number of color to 100
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " NOT IN " +
                    "(SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " ORDER BY " +
                    COLUMN_ID + " DESC LIMIT 100);");
            db.close();
            return true;
        }
    }

    /**
     * Returns all the data from database
     */
    public Cursor getColors(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_COLOR + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     */
    public void updateColor(String newColor, int id, String oldColor){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_COLOR +
                " = '" + newColor + "' WHERE " + COLUMN_ID + " = '" + id + "'" +
                " AND " + COLUMN_COLOR + " = '" + oldColor + "'";
        db.execSQL(query);
    }

    /**
     * Delete from database
     */
    public void deleteColor(int id, String colorName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = '" + id + "'" +
                " AND " + COLUMN_COLOR + " = '" + colorName + "'";
        db.execSQL(query);
    }

    /**
     * Clear database
     */
    public void clearColors(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
