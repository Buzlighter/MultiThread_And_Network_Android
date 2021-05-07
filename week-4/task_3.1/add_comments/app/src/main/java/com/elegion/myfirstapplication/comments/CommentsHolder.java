package com.elegion.myfirstapplication.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elegion.myfirstapplication.R;
import com.elegion.myfirstapplication.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentsHolder extends RecyclerView.ViewHolder {

    private final TextView mComment;
    private final TextView mAuthor;
    private final TextView mDate;

    public CommentsHolder(View itemView) {
        super(itemView);

        mComment = itemView.findViewById(R.id.tvComment);
        mAuthor = itemView.findViewById(R.id.tvAuthor);
        mDate = itemView.findViewById(R.id.tvDate);
    }

    public void bind(Comment comment) {
        final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU"));
        final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));

        mAuthor.setText("Автор: ");
        mComment.setText(comment.getText());
        mAuthor.append(comment.getAuthor());

        Date commentTimestamp = comment.getTimestamp();
        Date nowDate = new Date();
        if (nowDate.getTime() < commentTimestamp.getTime()) {
            Date tmpDate = nowDate;
            nowDate = commentTimestamp;
            commentTimestamp = tmpDate;
        }

        String createdDate;
        if (nowDate.getTime() - commentTimestamp.getTime() > 24L * 3600 * 1000) {
            createdDate = formatDate.format(commentTimestamp);
        } else {
            createdDate = formatTime.format(commentTimestamp);
        }

        mDate.setText("Опубликован: ");
        mDate.append(createdDate);
    }
}
