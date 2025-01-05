package com.example.contentprovider_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentprovider_user.databinding.RecyclerviewItemBinding;

import java.util.ArrayList;
import java.util.List;

public class DataRecyclerviewAdapter extends RecyclerView.Adapter<DataRecyclerviewAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Note> list;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final RecyclerviewItemBinding binding;
        public ViewHolder(RecyclerviewItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public DataRecyclerviewAdapter(Context context, ArrayList<Note>list){
        this.inflater=LayoutInflater.from(context);
        this.list=list;
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
        Note note=list.get(position);

        holder.binding.txtIdx.setText(String.valueOf(position+1));
        holder.binding.txtTitle.setText(note.title);
        holder.binding.txtContent.setText(note.content);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public void insertData(int newDataSize){
        int size=list.size();
        notifyItemRangeInserted(size, newDataSize);
    }

    public void resetList(){
        int size=list.size();
        list.clear();
        notifyItemRangeRemoved(0,size);
    }

}
