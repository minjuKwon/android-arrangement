package com.example.contentprovider_user;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentprovider_user.databinding.RecyclerviewItemBinding;

public class DataRecyclerviewAdapter extends RecyclerView.Adapter<DataRecyclerviewAdapter.ViewHolder>{

    private Cursor cursor;
    private final LayoutInflater inflater;
    private String table="";

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final RecyclerviewItemBinding binding;
        public ViewHolder(RecyclerviewItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;

        }
    }

    public DataRecyclerviewAdapter(Context context){
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewItemBinding binding=
                RecyclerviewItemBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor == null || cursor.isClosed()) return;

        String title = "";
        String content= "";

        if(cursor.moveToPosition(position)){

            if(table.equals(DataContract.TABLE_JAVA)){
                title=String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DataContract.JavaNotesEntry.COLUMN_TITLE)) );
                content=String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DataContract.JavaNotesEntry.COLUMN_CONTENT)) );
            }else if(table.equals(DataContract.TABLE_KOTLIN)) {
                title = String.valueOf(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        DataContract.KotlinNotesEntry.COLUMN_TITLE)));
                content = String.valueOf(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DataContract.KotlinNotesEntry.COLUMN_CONTENT)));
            }

            holder.binding.txtIdx.setText(String.valueOf(position+1));
            holder.binding.txtTitle.setText(title);
            holder.binding.txtContent.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor==null||cursor.isClosed())?0:cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(cursor==newCursor) return;
        cursor=newCursor;
        notifyDataSetChanged();
    }

    public void setTable(String table){
        this.table=table;
    }

}
