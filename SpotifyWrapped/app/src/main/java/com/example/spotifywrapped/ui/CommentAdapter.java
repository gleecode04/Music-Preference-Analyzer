package com.example.spotifywrapped.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.FeedAdapter;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.data_classes.Comment;
import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    List<Comment> comments;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment,parent,false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Comment comment = comments.get(position);
        System.out.println("comment userid: " + comment.getUserId() + " comment text: " + comment.getText() + "comment date: " + comment.getDate());
        holder.usernameTV.setText(comment.getUserId());
        holder.commentTV.setText(comment.getText());
        holder.dateTV.setText(comment.getDate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTV;
        TextView commentTV;
        TextView dateTV;
        Button commentButton;
        EditText textInput;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.usernameTV);
            commentTV = itemView.findViewById(R.id.commentTV);
            dateTV = itemView.findViewById(R.id.dateTV);
//            commentButton = itemView.findViewById(R.id.comment_button);
            //textInput = itemView.findViewById(R.id.editTextComment);
        }

    }
}
