package com.example.spotifywrapped.ui.wrapped;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.AuthenticationActivity;
import com.example.spotifywrapped.FeedAdapter;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.data_classes.Artist;
import com.example.spotifywrapped.data_classes.MasterUser;
import com.example.spotifywrapped.data_classes.Post;
import com.example.spotifywrapped.data_classes.Song;
import com.example.spotifywrapped.data_classes.User;
import com.example.spotifywrapped.data_classes.Wrapped;
import com.example.spotifywrapped.viewmodel.viewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class UserFeedFragment extends Fragment {

    private final viewmodel vm = viewmodel.getInstance();
    static List<User> following = new ArrayList<>();
    List<Post> feedPosts = new ArrayList<>();
    RecyclerView feed;
    FeedAdapter adapter;
    ArrayList<Artist> topArtists;
    ArrayList<Song>topSongs;
    String topGenre;
    int minutes;
    Wrapped wrap;
    User user1;
    User user2;
    User user3;
    User masterUser;

    private void setPostData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("tag", "yes");
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User postUser = new User(document);
                                Log.d("Data", following.toString());
                                postUser.addPost();

                                UserFeedFragment.following.add(postUser);
                                Log.d("Added", UserFeedFragment.following.toString());
                            }
                            for (User u : following) {
                                vm.addAll(u.getPosts());
                                Log.d("vm update","posts added to view model");
                            }
                            adapter.notifyDataSetChanged();
                            feed.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        User user1 = new User("1", "adam");
//        User user2 = new User("2", "alexis");
//        User user3 = new User("3", "caitlyn");
//
//
//
//        masterUser = new MasterUser("masteruser", "alex");
//
//        user1.addPost();
//        user2.addPost();
//        user3.addPost();
//
//        user1.setTopFiveArtists(topArtists);
//        user1.setTopFiveSongs(topSongs);
//
//        following.add(user1);
//        following.add(user2);
//        following.add(user3);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm.resetData();
        if (vm.getPostLiveData().getValue().size() == 0) {
            setPostData();
            Log.d("dummydata","dummydata initialized");
            Log.d("dummydata", following.toString());

        }
        //vm.printValues(); use to print values in the view model
        View view = inflater.inflate(R.layout.feed_fragment, container, false);
        feed = view.findViewById(R.id.feedRV);
        feed.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        Log.d("vm size ", String.valueOf(vm.getPostLiveData().getValue().size()));
//        Log.d("postdata in feed fragment", "debug start");
//        for (Post p : vm.getPostLiveData().getValue()) {
//            System.out.println(p.getPostId());
//        }
        Context context = getContext();
        adapter = new FeedAdapter(feedPosts, AuthenticationActivity.currentUser, context, this, vm);
        Log.d("userFeedfragment", "created");

        Switch likedPosts = view.findViewById(R.id.likeSwitch);
        Switch commentedPosts = view.findViewById(R.id.commentSwitch);

        likedPosts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commentedPosts.setChecked(false);
                }
                adapter.changeLikeFilter();
                adapter.notifyDataSetChanged();
            }
        });

        commentedPosts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    likedPosts.setChecked(false);
                }
                adapter.changeCommentFilter();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
