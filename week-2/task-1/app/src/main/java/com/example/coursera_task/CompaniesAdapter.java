package com.example.coursera_task;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CompaniesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List <Object> items;
    private Context context;
    private final int IMAGE = 0, TEXT = 1;
    private CompaniesAdapter.OnItemListener onItemListener;

    public CompaniesAdapter(List <Object> items, Context context, OnItemListener onItemListener) {
        this.items = items;
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType) {
            case IMAGE:
                View viewImage = inflater.inflate(R.layout.image_data_holder, parent, false);
                viewHolder = new ViewHolderImage(viewImage, onItemListener);
                break;
            case TEXT:
                View viewText = inflater.inflate(R.layout.text_data_holder,parent,false);
                viewHolder = new ViewHolderText(viewText, onItemListener);
                break;
            default:
                View abstractView = inflater.inflate(R.layout.text_data_holder,parent,false);
                viewHolder = new RecyclerView.ViewHolder(abstractView) {
                };
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == IMAGE) {
            ViewHolderImage holderImage = (ViewHolderImage) holder;
            holderImage.getImageView().setImageResource((Integer) items.get(position));
        }
        else {
            ViewHolderText holderText = (ViewHolderText) holder;
            holderText.getTextView().setText((String) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Integer) {
            return IMAGE;
        }
        else if (items.get(position) instanceof String) {
            return TEXT;
        }
        return -1;
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

}
