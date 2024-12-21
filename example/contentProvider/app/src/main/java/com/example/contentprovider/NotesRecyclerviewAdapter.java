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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final RecyclerviewItemBinding binding;
        public ViewHolder(RecyclerviewItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public NotesRecyclerviewAdapter(Context context){
        this.inflater=LayoutInflater.from(context);
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
        if(cursor.moveToPosition(position)){
            String idx=String.valueOf(
                    cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            );
            String title=String.valueOf(
                    cursor.getString(cursor.getColumnIndexOrThrow("title"))
            );
            String content=String.valueOf(
                    cursor.getString(cursor.getColumnIndexOrThrow("content"))
            );

            holder.binding.txtIdx.setText(idx);
            holder.binding.txtTitle.setText(title);
            holder.binding.txtContent.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor==null)?0:cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor!=null){
            cursor.close();
        }
        cursor=newCursor;
        notifyDataSetChanged();
    }

}
