package com.example.coursera_task;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderImage extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView iv_company;
    CompaniesAdapter.OnItemListener onItemListener;

    public ViewHolderImage(@NonNull View itemView, CompaniesAdapter.OnItemListener onItemListener) {
        super(itemView);
        iv_company = itemView.findViewById(R.id.iv_company);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public ImageView getImageView() {
        return iv_company;
    }

    public void setImageView(int resDraw) {
        iv_company.setImageResource(resDraw);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}
