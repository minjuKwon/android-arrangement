package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesContentProvider extends ContentProvider {

    private static final String AUTHORITY="com.example.content-provider";
    private static final String JAVA_NOTES="java_notes";
    private static final String KOTLIN_NOTES="kotlin_notes";
    private static final Uri JAVA_CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+JAVA_NOTES);
    private static final Uri KOTLIN_CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+KOTLIN_NOTES);

    private static final int JAVA_TABLE=1;
    private static final int JAVA_TABLE_ID=2;
    private static final int KOTLIN_TABLE=3;
    private static final int KOTLIN_TABLE_ID=4;

    private NotesDatabase notesDatabase;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(AUTHORITY,JAVA_NOTES,JAVA_TABLE);
        uriMatcher.addURI(AUTHORITY,JAVA_NOTES+"/#",JAVA_TABLE_ID);
        uriMatcher.addURI(AUTHORITY,KOTLIN_NOTES,KOTLIN_TABLE);
        uriMatcher.addURI(AUTHORITY,KOTLIN_NOTES+"/#",KOTLIN_TABLE_ID);
    }

    @Override
    public boolean onCreate() {
        notesDatabase=new NotesDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor=null;
        SQLiteDatabase db=notesDatabase.getReadableDatabase();

        switch(uriMatcher.match(uri)){
            case JAVA_TABLE:
                cursor=db.query(JAVA_NOTES,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case JAVA_TABLE_ID:
                selection="_ID = ?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(JAVA_NOTES, projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case KOTLIN_TABLE:
                cursor=db.query(KOTLIN_NOTES,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case KOTLIN_TABLE_ID:
                selection="_ID = ?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(KOTLIN_NOTES, projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throwInvalidUriException(uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case JAVA_TABLE:
                return "vnd.android.cursor.dir/vnd.com.example.content-provider/java";
            case JAVA_TABLE_ID:
                return "vnd.android.cursor.item/vnd.com.example.content-provider/java";
            case KOTLIN_TABLE:
                return "vnd.android.cursor.dir/vnd.com.example.content-provider/kotlin";
            case KOTLIN_TABLE_ID:
                return "vnd.android.cursor.item/vnd.com.example.content-provider/kotlin";
            default:
                throwInvalidUriException(uri);
                return "";
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id=-1L;
        SQLiteDatabase db=notesDatabase.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case JAVA_TABLE_ID:
                id=db.insert(JAVA_NOTES,null,values);
                break;
            case KOTLIN_TABLE_ID:
                id=db.insert(KOTLIN_NOTES,null,values);
                break;
            default:
                throwInvalidUriException(uri);
        }

        if(id>0) notifyChange(uri);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int rowsDeleted=0;
        SQLiteDatabase db=notesDatabase.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case JAVA_TABLE_ID:
                rowsDeleted=db.delete(JAVA_NOTES,selection,selectionArgs);
                break;
            case KOTLIN_TABLE_ID:
                rowsDeleted=db.delete(KOTLIN_NOTES,selection,selectionArgs);
                break;
            default:
                throwInvalidUriException(uri);
        }

        if(rowsDeleted>0) notifyChange(uri);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int rowsUpdated=0;
        SQLiteDatabase db=notesDatabase.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case JAVA_TABLE_ID:
                rowsUpdated=db.update(JAVA_NOTES,values, selection,selectionArgs);
                break;
            case KOTLIN_TABLE_ID:
                rowsUpdated=db.update(KOTLIN_NOTES,values,selection,selectionArgs);
                break;
            default:
                throwInvalidUriException(uri);
        }

        if(rowsUpdated>0) notifyChange(uri);
        return rowsUpdated;
    }

    private void notifyChange(Uri uri){
        getContext().getContentResolver().notifyChange(uri,null);
    }

    private void throwInvalidUriException(Uri uri) {
        throw new InvalidUriException(uri.toString());
    }

    private static class InvalidUriException extends RuntimeException{
        public InvalidUriException(String uri){
            super("Invalid uri"+uri);
        }
    }

}