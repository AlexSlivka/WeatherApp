package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDataAdapterCities extends RecyclerView.Adapter<RecyclerDataAdapterCities.ViewHolder> {
    private String[] data;
    private OnItemCitiesClick onItemCitiesClickCallback;

    public RecyclerDataAdapterCities(String[] data, OnItemCitiesClick onItemCitiesClickCallback) {
        this.data = data;
        this.onItemCitiesClickCallback = onItemCitiesClickCallback;
    }

    private void setOnClickForItem(@NonNull ViewHolder holder, final String text) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCitiesClickCallback != null) {
                    onItemCitiesClickCallback.onItemCitiesClicked(text);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_layout_cities, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data[position];
        holder.textView.setText(text);
        setOnClickForItem(holder, text);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_textview_cities);
        }
    }
}