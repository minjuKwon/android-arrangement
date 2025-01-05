package com.example.contentprovider_user;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contentprovider_user.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID_TABLE_JAVA=1;
    private static final int LOADER_ID_TABLE_KOTLIN=2;

    private final ArrayList<Note> list= new ArrayList<>();

    private DataRecyclerviewAdapter adapter;
    private ContentObserver contentObserver;
    private boolean isLoaderStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new DataRecyclerviewAdapter(this,list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.btnDelete.setOnClickListener(v->deleteAllData());
        binding.btnQuery.setOnClickListener(v->{

            String deleteTable= String.valueOf(binding.editDeleteTable.getText());
            String deleteIdx= String.valueOf(binding.editDeleteIdx.getText());

            Bundle args = new Bundle();
            args.putString("table", deleteTable);
            args.putString("idx",deleteIdx);

            if(!isLoaderStarted){
                if(deleteTable.equals("j")){
                    getSupportLoaderManager().initLoader(LOADER_ID_TABLE_JAVA,args,this);
                }else if(deleteTable.equals("k")) {
                    getSupportLoaderManager().initLoader(LOADER_ID_TABLE_KOTLIN, args, this);
                }
                isLoaderStarted=true;
            }else{
                if(deleteTable.equals("j")){
                    getSupportLoaderManager().restartLoader(LOADER_ID_TABLE_JAVA,args,this);
                }else if(deleteTable.equals("k")) {
                    getSupportLoaderManager().restartLoader(LOADER_ID_TABLE_KOTLIN,args,this);
                }
            }

            binding.editDeleteTable.setText("");
            binding.editDeleteIdx.setText("");
        });
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
        String table="";
        String idx="";

        if (args != null) {
            table=args.getString("table");
            idx=args.getString("idx");
        }

        String selection = BaseColumns._ID+"=?";
        String [] selectionArgs=new String[]{idx};

        if(idx!=null&&table!=null){
            if (id == LOADER_ID_TABLE_JAVA) {
                if (idx.equals("0")) {
                    return new CursorLoader(
                            this, DataContract.JavaNotesEntry.CONTENT_URI,
                            null, null, null, null
                    );
                } else {
                    return new CursorLoader(
                            this, DataContract.JavaNotesEntry.CONTENT_URI,
                            null, selection, selectionArgs, null
                    );
                }
            }else if(id == LOADER_ID_TABLE_KOTLIN){
                if (idx.equals("0")) {
                    return new CursorLoader(
                            this, DataContract.KotlinNotesEntry.CONTENT_URI,
                            null, null, null, null
                    );
                } else {
                    return new CursorLoader(
                            this, DataContract.KotlinNotesEntry.CONTENT_URI,
                            null, selection, selectionArgs, null
                    );
                }

            }
        }

        throw new IllegalArgumentException("Invalid loader ID");

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        loadCursorInToList(loader.getId(),data);
        adapter.insertData(data.getCount());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.resetList();
    }

    public void deleteAllData(){
        adapter.resetList();
    }

    private void registerContentObserver(){
        contentObserver=new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, @Nullable Uri uri) {
                super.onChange(selfChange, uri);

                if(uri==null) throw new NullPointerException("uri is null");

                if(uri.equals(DataContract.JavaNotesEntry.CONTENT_URI)){
                    getSupportLoaderManager().restartLoader(
                            LOADER_ID_TABLE_JAVA,null,MainActivity.this);
                }else if(uri.equals(DataContract.KotlinNotesEntry.CONTENT_URI)){
                    getSupportLoaderManager().restartLoader(
                            LOADER_ID_TABLE_KOTLIN,null,MainActivity.this);
                }

            }
        };

        getContentResolver().registerContentObserver(
                DataContract.JavaNotesEntry.CONTENT_URI,true,contentObserver);
        getContentResolver().registerContentObserver(
                DataContract.KotlinNotesEntry.CONTENT_URI,true,contentObserver
        );
    }

    private void loadCursorInToList(int loaderId,Cursor cursor){

        if (cursor == null || cursor.isClosed()) return;

        String title = "";
        String content= "";

        if(cursor.moveToFirst()){
            do{
                if(loaderId==LOADER_ID_TABLE_JAVA){
                    title=String.valueOf(
                            cursor.getString(cursor.getColumnIndexOrThrow(
                                    DataContract.JavaNotesEntry.COLUMN_TITLE)) );
                    content=String.valueOf(
                            cursor.getString(cursor.getColumnIndexOrThrow(
                                    DataContract.JavaNotesEntry.COLUMN_CONTENT)) );
                }else if(loaderId==LOADER_ID_TABLE_KOTLIN){
                    title=String.valueOf(
                            cursor.getString(cursor.getColumnIndexOrThrow(
                                    DataContract.KotlinNotesEntry.COLUMN_TITLE)) );
                    content=String.valueOf(
                            cursor.getString(cursor.getColumnIndexOrThrow(
                                    DataContract.KotlinNotesEntry.COLUMN_CONTENT)) );
                }

                list.add(new Note(title,content));
            } while(cursor.moveToNext());
        }

    }

}