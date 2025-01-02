package com.example.contentprovider_user;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contentprovider_user.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID_TABLE_JAVA=1;
    private static final int LOADER_ID_TABLE_KOTLIN=2;

    private DataRecyclerviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new DataRecyclerviewAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID_TABLE_JAVA,null,this);
        getSupportLoaderManager().initLoader(LOADER_ID_TABLE_KOTLIN,null,this);

        binding.btnDelete.setOnClickListener(v->deleteAllData());
        binding.btnQuery.setOnClickListener(v->{
            String deleteTable= String.valueOf(binding.editDeleteTable.getText());
            String deleteIdx= String.valueOf(binding.editDeleteIdx.getText());
            queryData(deleteTable, deleteIdx);
            binding.editDeleteTable.setText("");
            binding.editDeleteIdx.setText("");
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri;
        switch (id){
            case LOADER_ID_TABLE_JAVA:
                uri=DataContract.JavaNotesEntry.CONTENT_URI;
                break;
            case LOADER_ID_TABLE_KOTLIN:
                uri=DataContract.KotlinNotesEntry.CONTENT_URI;
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

    public void deleteAllData(){
        adapter.swapCursor(null);
    }

    public void queryData(String table, String idx){
        Uri uri=checkTable(table);

        String selection = BaseColumns._ID+"=?";
        String [] selectionArgs=new String[]{idx};
        Cursor cursor;
        if(idx.equals("0")){
            cursor=getContentResolver().query(uri,null,null, null,null);
        }else{
            cursor=getContentResolver().query(uri,null,selection, selectionArgs,null);
        }
        if(cursor!=null) cursor.close();
    }

    private Uri checkTable(String table){
        Uri uri=Uri.EMPTY;
        if(table.equals("j")){
            uri=DataContract.JavaNotesEntry.CONTENT_URI;
            adapter.setTable(DataContract.TABLE_JAVA);
        }else if(table.equals("k")){
            uri=DataContract.KotlinNotesEntry.CONTENT_URI;
            adapter.setTable(DataContract.TABLE_KOTLIN);
        }
        return uri;
    }

}