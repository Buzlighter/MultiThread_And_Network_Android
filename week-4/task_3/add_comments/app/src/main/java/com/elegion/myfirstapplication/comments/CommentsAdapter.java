package com.elegion.myfirstapplication.comments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.elegion.myfirstapplication.R;
import com.elegion.myfirstapplication.model.Comment;
import com.elegion.myfirstapplication.model.Song;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsHolder> {
    private final List<Comment> commentList = new ArrayList<>();
    private Comment comment;

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_comment, parent, false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsHolder holder, int position) {
        comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addData(List<Comment> data, boolean isRefreshed) {
        if (isRefreshed) {
            commentList.clear();
        }

        commentList.addAll(data);
        notifyDataSetChanged();
    }
}
