package com.example.weatherapp.fragments.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

public class RecyclerDataAdapterHistory extends RecyclerView.Adapter<RecyclerDataAdapterHistory.ViewHolder> {
    private String[] data;

    public RecyclerDataAdapterHistory(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_layout_history, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data[position];
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_textview_history);
        }
    }
}
