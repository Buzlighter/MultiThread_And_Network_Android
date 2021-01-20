package com.example.coursera_task;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewHolderText extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView tv_company;
    CompaniesAdapter.OnItemListener onItemListener;

    public ViewHolderText(@NonNull View itemView, CompaniesAdapter.OnItemListener onItemListener) {
        super(itemView);
        tv_company = itemView.findViewById(R.id.tv_company);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }
    public TextView getTextView() {
        return tv_company;
    }
    public void setTextView(String text) {
        tv_company.setText(text);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}
