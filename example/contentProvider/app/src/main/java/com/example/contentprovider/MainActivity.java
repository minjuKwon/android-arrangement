package com.example.contentprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;

import com.example.contentprovider.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_TABLE_JAVA=1;
    private static final int LOADER_ID_TABLE_KOTLIN=2;

    private ActivityMainBinding binding;
    private NotesRecyclerviewAdapter adapter;
    private ContentObserver contentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new NotesRecyclerviewAdapter(this);
        binding.javaList.setLayoutManager(new LinearLayoutManager(this));
        binding.kotlinList.setLayoutManager(new LinearLayoutManager(this));
        binding.javaList.setAdapter(adapter);
        binding.kotlinList.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID_TABLE_JAVA,null,this);
        getSupportLoaderManager().initLoader(LOADER_ID_TABLE_KOTLIN,null,this);

        String deleteTable= String.valueOf(binding.editDeleteTable.getText());
        String deleteIdx= String.valueOf(binding.editDeleteIdx);
        String insertTable= String.valueOf(binding.editInsertTable);
        String insertColumnName= String.valueOf(binding.editInsertColumnName);
        String insertColumnValue= String.valueOf(binding.editInsertColumnValue);
        String updateTable= String.valueOf(binding.editUpdateTable);
        String updateIdx= String.valueOf(binding.editUpdateIdx);
        String updateColumnName= String.valueOf(binding.editUpdateColumnName);
        String updateColumnValue= String.valueOf(binding.editUpdateColumnValue);
        
        binding.btnDelete.setOnClickListener(
                v->deleteData(deleteTable, deleteIdx) );
        binding.btnInsert.setOnClickListener(
                v->insertData(insertTable, insertColumnName, insertColumnValue) );
        binding.btnUpdate.setOnClickListener(
                v->updateData(updateTable, updateIdx, updateColumnName, updateColumnValue) );
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerContentObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri;
        switch (id){
            case LOADER_ID_TABLE_JAVA:
                uri=NotesContract.JavaNotesEntry.CONTENT_URI;
                break;
            case LOADER_ID_TABLE_KOTLIN:
                uri=NotesContract.KotlinNotesEntry.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Invalid loader ID");
        }
        return new CursorLoader(
                this,uri,null,null,null,null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void deleteData(String table, String idx){
        Uri uri=checkTable(table);

        String selection = BaseColumns._ID+"=?";
        String [] selectionArgs=new String[]{idx};
        getContentResolver().delete(uri,selection, selectionArgs);
    }

    public void insertData(String table, String name, String value){
        Uri uri=checkTable(table);

        ContentValues values=new ContentValues();
        values.put("name", name);
        values.put("value",value);
        getContentResolver().insert(uri,values);
    }

    public void updateData(String table, String idx, String name, String value){
        Uri uri=checkTable(table);

        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("values",value);
        String selection=BaseColumns._ID+"=?";
        String [] selectionArgs=new String[]{idx};
        getContentResolver().update(uri,values,selection,selectionArgs);
    }

    private Uri checkTable(String table){
        Uri uri=Uri.EMPTY;
        if(table.equals("j")){
            uri=NotesContract.JavaNotesEntry.CONTENT_URI;
        }else if(table.equals("k")){
            uri=NotesContract.KotlinNotesEntry.CONTENT_URI;
        }
        return uri;
    }

    private void registerContentObserver(){
        contentObserver=new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, @Nullable Uri uri) {
                super.onChange(selfChange, uri);

                if(uri==null) throw new NullPointerException("uri is null");

                if(uri.equals(NotesContract.JavaNotesEntry.CONTENT_URI)){
                    getSupportLoaderManager().restartLoader(
                            LOADER_ID_TABLE_JAVA,null,MainActivity.this);
                }else if(uri.equals(NotesContract.KotlinNotesEntry.CONTENT_URI)){
                    getSupportLoaderManager().restartLoader(
                            LOADER_ID_TABLE_KOTLIN,null,MainActivity.this);
                }

            }
        };

        getContentResolver().registerContentObserver(
                NotesContract.JavaNotesEntry.CONTENT_URI,true,contentObserver);
        getContentResolver().registerContentObserver(
                NotesContract.KotlinNotesEntry.CONTENT_URI,true,contentObserver
        );
    }

}