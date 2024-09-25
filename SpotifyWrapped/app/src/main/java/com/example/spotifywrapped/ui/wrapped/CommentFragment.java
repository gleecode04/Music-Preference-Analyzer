package com.example.spotifywrapped.ui.wrapped;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.R;
import com.example.spotifywrapped.data_classes.Comment;
import com.example.spotifywrapped.data_classes.Post;
import com.example.spotifywrapped.data_classes.User;
import com.example.spotifywrapped.ui.CommentAdapter;
import com.example.spotifywrapped.viewmodel.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentFragment extends DialogFragment {



    public CommentFragment(Post post, Context context, viewmodel vm, User user) {
        this.comments = post.getComments();
        this.context = context;
        this.post = post;
        this.vm = vm;
        this.user = user;
    }

    private viewmodel vm;

    private User user;

    Context context;
    RecyclerView commentView;
    CommentAdapter adapter;
    Post post;
    List<Comment> comments;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SharedPreferences sp;

    private void addComments(String str, viewmodel vm) {
        Log.d("comment", "new Comment added");
        Comment newComment = new Comment(user.getSpotify_id(), str, sdf.format(new Date()));
        post.getComments().add(newComment);
        vm.updatePost(post);
        vm.addCommentedPosts(post);
        //vm.updateCommentedPosts(user);
//        int size = vm.getCommentedPostsLiveData().getValue().size();
//        Log.d("commentedPosts update", vm.getCommentedPostsLiveData().getValue().get(size - 1).getPostId());
        adapter.notifyDataSetChanged();
    }
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.comment_section, null);
        commentView = view.findViewById(R.id.commentRV);
        commentView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new CommentAdapter(comments);
        commentView.setAdapter(adapter);

        Button commentButton = view.findViewById(R.id.submitbutton);
        commentButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("submit button", "clicked submit");
                        EditText text = view.findViewById(R.id.editTextComment);
                        if (text.getText().toString() != null && !text.getText().toString().isEmpty()) {
                            String str = text.getText().toString();
                            addComments(str, vm);
                            text.setText("");
                        }
                    }
                }
        );

        builder.setView(view);
        return builder.create();
    }

}

