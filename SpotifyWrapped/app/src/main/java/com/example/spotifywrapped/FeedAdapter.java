package com.example.spotifywrapped;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.data_classes.Artist;
import com.example.spotifywrapped.data_classes.Comment;
import com.example.spotifywrapped.data_classes.Like;
import com.example.spotifywrapped.data_classes.Post;
import com.example.spotifywrapped.data_classes.Song;
import com.example.spotifywrapped.data_classes.Stat;
import com.example.spotifywrapped.data_classes.User;
import com.example.spotifywrapped.ui.wrapped.CommentFragment;
import com.example.spotifywrapped.viewmodel.viewmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<Post> feedPosts;
    private List<Stat> stats;

    private User masterUser;

    Fragment feedFragment;
    Context context;

    viewmodel vm;

    boolean filter_liked = false;

    boolean filter_commented = false;

    MutableLiveData<List<Post>> data;

    CommentFragment cFragment;

    public FeedAdapter(List<Post> feedPosts, User masterUser,Context context, Fragment feedFragment, viewmodel vm) {
        this.feedPosts = feedPosts;
        this.masterUser = masterUser;
        this.context = context;
        this.feedFragment = feedFragment;
        this.vm = vm;
        this.data = vm.getPostLiveData();
        Log.d("vm size in adapter ", String.valueOf(vm.getPostLiveData().getValue().size()));
    }

    public void changeLikeFilter() {
        filter_liked = !filter_liked;
    }

    public void changeCommentFilter() {
        filter_commented = !filter_commented;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new FeedAdapter.FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

        Log.d("postdata in adapter", "debug start");
        for (Post p : data.getValue()) {
            System.out.println(p.getPostId());
        }
        Post post = data.getValue().get(position);
        User user = post.getUser();
        Log.d("feedPOst user at position", post.getUser().getSpotify_id() + "pos" + position);
        Log.d("postid",post.getPostId());
        holder.username.setText(user.getName());
        holder.likeButton.setText(String.valueOf(post.getLikeCount()));

        if (user.getName().charAt(user.getName().length()-1) == 's') {
            holder.title.setText(user.getName()+"' Spotify Wrapped 2024");
        } else {
            holder.title.setText(user.getName()+"'s Spotify Wrapped 2024");
        }


        ArrayList<Artist> totalARTISTS = user.getTopFiveArtists();
        ArrayList<Song> totalSONGS = user.getTopFiveSongs();


        //List<Stat> genre = statify(wrap.getTopGenre());

        //List<Stat> minutes = statify(wrap.getMinutesListened());


        //UserStatAdapter artistAdapter = new UserStatAdapter(artists);
        //UserStatAdapter genreAdapter = new UserStatAdapter(genre);
        //UserStatAdapter songAdapter = new UserStatAdapter(songs);
        //UserStatAdapter minutesAdapter = new UserStatAdapter(minutes);

        int i = 0;
        while (i < totalARTISTS.size()) {
            ((TextView) holder.itemView.findViewById(holder.textviewartists[i])).setText(totalARTISTS.get(i).getName());
            i++;
        }
        while (i < holder.textviewartists.length) {
            ((TextView) holder.itemView.findViewById(holder.textviewartists[i])).setText("");
            i++;
        }

        int z = 0;
        while (z < totalSONGS.size()) {
            String description = (String) totalSONGS.get(z).getName() + " by " + totalSONGS.get(z).getArtist();
            ((TextView) holder.itemView.findViewById(holder.textviewsongs[z])).setText(description);
            z++;
        }
        while (z < holder.textviewsongs.length) {
            ((TextView) holder.itemView.findViewById(holder.textviewsongs[z])).setText("");
            z++;
        }

        if (!totalARTISTS.isEmpty()) {
            ImageView imageView = holder.imageView;
            Picasso.get().load(totalARTISTS.get(0).getPicture())
                    .placeholder(R.drawable.imgload)
                    .into(imageView);
        }

        //holder.topArtists.setAdapter(artistAdapter);
        //holder.topArtists.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //holder.topGenre.setAdapter(genreAdapter);
        //holder.topGenre.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //holder.topSongs.setAdapter(songAdapter);
        //holder.topSongs.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //holder.minutes.setAdapter(minutesAdapter);
        //holder.minutes.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));


        //user holder to access button count display and change value accordingly.
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (masterUser.getLikedPosts().contains(post)) {
                    post.removeLike(masterUser);
                    vm.removeLikedPosts(post);
                    Log.d("removepost", "ran");
                } else {
                    post.addLike(masterUser, new Date());
                    Log.d("liekdpost ran", " ran");
                    vm.addLikedPosts(post);
                }
                notifyItemChanged(holder.getAdapterPosition());
                int index = vm.getLikedPostsLiveData().getValue().size() - 1;
                if (index >= 0) {
                    Log.d("likedpost id ",   "size" + vm.getLikedPostsLiveData().getValue().size());
                    Like l = vm.getLikedPostsLiveData().getValue().get(index).getLikes().get(0);
                    Log.d("likeID", l.getUserId());
                } else {
                    Log.d("LIKESTATUS", "no lieks left");
                }
            }
        });

        holder.openCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("postid",post.getPostId());
                cFragment = new CommentFragment(post, holder.itemView.getContext(),vm, masterUser);
                DialogFragment commentFragment = cFragment;
                Log.d("comment fragment","CFRAGMENT Ceated");
                for (Comment c : post.getComments()) {
                    Log.d("post.getComments()", c.toString());
                }
                commentFragment.show(feedFragment.getParentFragmentManager(), "comments");
            }
        });
    }

    private List<Stat> statify(List<String> list) {
        List<Stat> stat = new ArrayList<>();
        //int i = 0;
        for (String s : list) {
            stat.add(new Stat(s,s));
        }
        return stat;
    }

    @Override
    public int getItemCount() {
        if (filter_commented) {
            data = vm.getCommentedPostsLiveData();
            return vm.getCommentedPostsLiveData().getValue().size();
        } else if (filter_liked) {
            data = vm.getLikedPostsLiveData();
            return vm.getLikedPostsLiveData().getValue().size();
        }
        data = vm.getPostLiveData();
        return vm.getPostLiveData().getValue().size();
    }


    static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView pfp;
        TextView username;
        RecyclerView topArtists;
        TextView topGenre;
        RecyclerView topSongs;
        TextView minutes;

        ImageView imageView;

        Button likeButton;
        Button openCommentsButton;
        int[] textviewsongs;
        int[] textviewartists;

        TextView title;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            pfp = itemView.findViewById(R.id.profile_picture);
            username = itemView.findViewById(R.id.username);
            topArtists = itemView.findViewById(R.id.rvTopArtists);
            textviewsongs = new int[]{R.id.textViewSong1, R.id.textViewSong2, R.id.textViewSong3, R.id.textViewSong4, R.id.textViewSong5};
            textviewartists = new int[]{R.id.textViewArtist1, R.id.textViewArtist2, R.id.textViewArtist3, R.id.textViewArtist4, R.id.textViewArtist5};
            likeButton = itemView.findViewById(R.id.like_button);
            openCommentsButton = itemView.findViewById(R.id.comment_button);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
