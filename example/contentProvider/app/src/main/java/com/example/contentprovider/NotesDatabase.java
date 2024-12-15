package com.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="notes.db";
    private static final int DATABASE_VERSION=1;

    public NotesDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQLITE_CREATE_JAVA_TABLE=
                "CREATE TABLE "+ NotesContract.JavaNotesEntry.TABLE_NAME +"("+
                NotesContract.JavaNotesEntry._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NotesContract.JavaNotesEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                NotesContract.JavaNotesEntry.COLUMN_CONTENT+" TEXT NOT NULL);";

        final String SQLITE_CREATE_KOTLIN_TABLE=
                "CREATE TABLE "+ NotesContract.KotlinNotesEntry.TABLE_NAME +"("+
                NotesContract.KotlinNotesEntry._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NotesContract.KotlinNotesEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                NotesContract.KotlinNotesEntry.COLUMN_CONTENT+" TEXT NOT NULL);";

        db.execSQL(SQLITE_CREATE_JAVA_TABLE);
        db.execSQL(SQLITE_CREATE_KOTLIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NotesContract.JavaNotesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ NotesContract.KotlinNotesEntry.TABLE_NAME);
        onCreate(db);
    }

}
