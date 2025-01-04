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

    private static final int LOADER_ID=1;

    private DataRecyclerviewAdapter adapter;
    private boolean isLoaderStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=new DataRecyclerviewAdapter(this);
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
                getSupportLoaderManager().initLoader(LOADER_ID,args,this);
                isLoaderStarted=true;
            }else{
                getSupportLoaderManager().restartLoader(LOADER_ID,args,this);
            }

            binding.editDeleteTable.setText("");
            binding.editDeleteIdx.setText("");
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String table;
        String idx="";
        Uri uri=Uri.EMPTY;

        if (args != null) {
            table=args.getString("table");
            idx=args.getString("idx");
            if(table!=null) uri=checkTable(table);
        }

        String selection = BaseColumns._ID+"=?";
        String [] selectionArgs=new String[]{idx};

        if (id == LOADER_ID&&idx!=null) {
            if (idx.equals("0")) {
                return new CursorLoader(
                        this, uri, null, null, null, null
                );
            } else {
                return new CursorLoader(
                        this, uri, null, selection, selectionArgs, null
                );
            }
        }

        throw new IllegalArgumentException("Invalid loader ID");

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