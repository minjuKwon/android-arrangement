package com.example.contentprovider;

import static com.example.contentprovider.NotesContract.AUTHORITY;
import static com.example.contentprovider.NotesContract.TABLE_JAVA;
import static com.example.contentprovider.NotesContract.TABLE_KOTLIN;

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

    private static final int URI_CODE_JAVA_TABLE=1;
    private static final int URI_CODE_JAVA_TABLE_ID=2;
    private static final int URI_CODE_KOTLIN_TABLE=3;
    private static final int URI_CODE_KOTLIN_TABLE_ID=4;

    private NotesDatabase notesDatabase;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(AUTHORITY,TABLE_JAVA,URI_CODE_JAVA_TABLE);
        uriMatcher.addURI(AUTHORITY,TABLE_JAVA+"/#",URI_CODE_JAVA_TABLE_ID);
        uriMatcher.addURI(AUTHORITY,TABLE_KOTLIN,URI_CODE_KOTLIN_TABLE);
        uriMatcher.addURI(AUTHORITY,TABLE_KOTLIN+"/#",URI_CODE_KOTLIN_TABLE_ID);
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
            case URI_CODE_JAVA_TABLE:
                cursor=db.query(TABLE_JAVA,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case URI_CODE_JAVA_TABLE_ID:
                selection="_ID = ?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(TABLE_JAVA, projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case URI_CODE_KOTLIN_TABLE:
                cursor=db.query(TABLE_KOTLIN,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case URI_CODE_KOTLIN_TABLE_ID:
                selection="_ID = ?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(TABLE_KOTLIN, projection,selection,selectionArgs,
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
            case URI_CODE_JAVA_TABLE:
                return "vnd.android.cursor.dir/vnd.com.example.content-provider/java";
            case URI_CODE_JAVA_TABLE_ID:
                return "vnd.android.cursor.item/vnd.com.example.content-provider/java";
            case URI_CODE_KOTLIN_TABLE:
                return "vnd.android.cursor.dir/vnd.com.example.content-provider/kotlin";
            case URI_CODE_KOTLIN_TABLE_ID:
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
            case URI_CODE_JAVA_TABLE:
                id=db.insert(TABLE_JAVA,null,values);
                break;
            case URI_CODE_KOTLIN_TABLE:
                id=db.insert(TABLE_KOTLIN,null,values);
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
            case URI_CODE_JAVA_TABLE:
                rowsDeleted=db.delete(TABLE_JAVA,selection,selectionArgs);
                break;
            case URI_CODE_KOTLIN_TABLE:
                rowsDeleted=db.delete(TABLE_KOTLIN,selection,selectionArgs);
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
            case URI_CODE_JAVA_TABLE:
                rowsUpdated=db.update(TABLE_JAVA,values, selection,selectionArgs);
                break;
            case URI_CODE_KOTLIN_TABLE:
                rowsUpdated=db.update(TABLE_KOTLIN,values,selection,selectionArgs);
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