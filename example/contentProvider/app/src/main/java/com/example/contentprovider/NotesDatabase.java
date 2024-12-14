package com.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class NotesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="notes.db";
    private static final int DATABASE_VERSION=1;

    public static final class NotesEntry implements BaseColumns{
        static final String TABLE_NAME="notes";
        static final String COLUMN_TITLE="title";
        static final String COLUMN_CONTENT="content";
    }

    public NotesDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQLITE_CREATE_NOTES_TABLE=
                "CREATE TABLE "+NotesEntry.TABLE_NAME +"("+
                NotesEntry._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NotesEntry.COLUMN_TITLE+" TEXT, "+
                NotesEntry.COLUMN_CONTENT+" TEXT);";
        db.execSQL(SQLITE_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

}
