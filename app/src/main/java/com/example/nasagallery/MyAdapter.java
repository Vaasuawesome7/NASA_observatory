package com.example.nasagallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<String> mSearchNames;
    private OnNASAItemClickListener mListener;

    public interface OnNASAItemClickListener{
        void onItemClick(int pos);
    }

    public MyAdapter(ArrayList<String> mSearchNames) {
        this.mSearchNames = mSearchNames;
    }

    public void setListener(OnNASAItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nasa_item_search, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.searchItem.setText(mSearchNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mSearchNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnNASAItemClickListener{

        TextView searchItem;

        public MyViewHolder(@NonNull View itemView, OnNASAItemClickListener listener) {
            super(itemView);

            searchItem = itemView.findViewById(R.id.nasa_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                            listener.onItemClick(pos);
                    }
                }
            });
        }

        @Override
        public void onItemClick(int pos) {

        }
    }
}
