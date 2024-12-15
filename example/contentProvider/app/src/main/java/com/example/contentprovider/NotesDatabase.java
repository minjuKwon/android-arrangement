package com.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class NotesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="notes.db";
    private static final int DATABASE_VERSION=1;

    public static final class JavaNotesEntry implements BaseColumns{
        static final String TABLE_NAME="java_notes";
        static final String COLUMN_TITLE="title";
        static final String COLUMN_CONTENT="content";
    }

    public static final class KotlinNotesEntry implements BaseColumns{
        static final String TABLE_NAME="kotlin_notes";
        static final String COLUMN_TITLE="title";
        static final String COLUMN_CONTENT="content";
    }

    public NotesDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQLITE_CREATE_JAVA_TABLE=
                "CREATE TABLE "+JavaNotesEntry.TABLE_NAME +"("+
                JavaNotesEntry._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                JavaNotesEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                JavaNotesEntry.COLUMN_CONTENT+" TEXT NOT NULL);";

        final String SQLITE_CREATE_KOTLIN_TABLE=
                "CREATE TABLE "+KotlinNotesEntry.TABLE_NAME +"("+
                KotlinNotesEntry._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KotlinNotesEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                KotlinNotesEntry.COLUMN_CONTENT+" TEXT NOT NULL);";

        db.execSQL(SQLITE_CREATE_JAVA_TABLE);
        db.execSQL(SQLITE_CREATE_KOTLIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+JavaNotesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+KotlinNotesEntry.TABLE_NAME);
        onCreate(db);
    }

}
