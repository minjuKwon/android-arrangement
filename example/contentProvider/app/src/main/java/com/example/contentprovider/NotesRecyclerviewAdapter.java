package com.example.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentprovider.databinding.RecyclerviewItemBinding;

public class NotesRecyclerviewAdapter extends RecyclerView.Adapter<NotesRecyclerviewAdapter.ViewHolder>{

    private Cursor cursor;
    private final LayoutInflater inflater;
    private final String table;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final RecyclerviewItemBinding binding;
        public ViewHolder(RecyclerviewItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public NotesRecyclerviewAdapter(Context context, String table){
        this.inflater=LayoutInflater.from(context);
        this.table=table;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewItemBinding binding=RecyclerviewItemBinding.
                inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor == null || cursor.isClosed()) return;

        String idx = "";
        String title = "";
        String content= "";

        if(cursor.moveToPosition(position)){

            if(table.equals(NotesContract.TABLE_JAVA)){
                idx=String.valueOf(
                        cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)) );
                title=String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                NotesContract.JavaNotesEntry.COLUMN_TITLE)) );
                content=String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                NotesContract.JavaNotesEntry.COLUMN_CONTENT)) );
            }else if(table.equals(NotesContract.TABLE_KOTLIN)){
                idx=String.valueOf(
                        cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)) );
                title=String.valueOf(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        NotesContract.KotlinNotesEntry.COLUMN_TITLE)) );
                content=String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                NotesContract.KotlinNotesEntry.COLUMN_CONTENT)) );
            }

            holder.binding.txtIdx.setText(idx);
            holder.binding.txtTitle.setText(title);
            holder.binding.txtContent.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor==null||cursor.isClosed())?0:cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor==newCursor) return;
        cursor=newCursor;
        notifyDataSetChanged();
    }

}
