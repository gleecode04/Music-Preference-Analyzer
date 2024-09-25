package com.example.spotifywrapped.ui.singleWrapped;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotifywrapped.AuthenticationActivity;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.authentication.LoginFragment;
import com.example.spotifywrapped.data_classes.Artist;
import com.example.spotifywrapped.data_classes.Song;
import com.example.spotifywrapped.databinding.FragmentSingleWrappedBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

// after cleaned up
public class SingleWrappedFragment extends Fragment {

    private FragmentSingleWrappedBinding binding;
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0;
    private ArrayList<String> nonNullSongUrls;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSingleWrappedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupUserDataDisplay(root);
        setupArtistsDisplay(root);
        setupSongsDisplay(root);
        setupMediaPlayer();
        startMusicPlaylist();

        return root;
    }

    private void setupUserDataDisplay(View root) {
        if (AuthenticationActivity.currentUser != null) {
            String userName = AuthenticationActivity.currentUser.getName() + "'s\nSpotify Wrapped 2024";
            TextView usernameTextView = root.findViewById(R.id.textViewTitle);
            usernameTextView.setText(userName);
        }
    }

    private void setupArtistsDisplay(View root) {
        ArrayList<Artist> artists = AuthenticationActivity.currentUser.getTopFiveArtists();
        for (int i = 0; i < artists.size(); i++) {
            String description = (i + 1) + ". " + artists.get(i).getName();
            int textViewId = root.getResources().getIdentifier("textViewArtist" + (i + 1), "id", getContext().getPackageName());
            ((TextView) root.findViewById(textViewId)).setText(description);
        }

        if (!artists.isEmpty()) {
            ImageView imageView = root.findViewById(R.id.imageView);
            Picasso.get().load(artists.get(0).getPicture())
                    .placeholder(R.drawable.imgload)
                    .into(imageView);
        }
    }

    private void setupSongsDisplay(View root) {
        ArrayList<Song> songs = AuthenticationActivity.currentUser.getTopFiveSongs();
        nonNullSongUrls = new ArrayList<>();

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            String description = (i + 1) + ". " + song.getName();
            int textViewId = root.getResources().getIdentifier("textViewSong" + (i + 1), "id", getContext().getPackageName());
            ((TextView) root.findViewById(textViewId)).setText(description);

            if (song.getPreview() != null && !song.getPreview().equals("null")) {
                nonNullSongUrls.add(song.getPreview());
            }
        }
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playNextSong());
    }

    private void startMusicPlaylist() {
        if (!nonNullSongUrls.isEmpty()) {
            playAudio(nonNullSongUrls.get(0));
        }
    }

    private void playNextSong() {
        mediaPlayer.reset();
        currentSongIndex++;
        if (currentSongIndex < nonNullSongUrls.size()) {
            playAudio(nonNullSongUrls.get(currentSongIndex));
        }
    }

    private void playAudio(String url) {
        try {
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("SingleWrappedFragment", "Error playing audio: " + e.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}




// before
//package com.example.spotifywrapped.ui.singleWrapped;
//
//import android.media.AudioAttributes;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.spotifywrapped.R;
//import com.example.spotifywrapped.authentication.LoginFragment;
//import com.example.spotifywrapped.data_classes.Artist;
//import com.example.spotifywrapped.data_classes.Song;
//import com.example.spotifywrapped.databinding.FragmentSingleWrappedBinding;
//import com.squareup.picasso.Picasso;
//
//import org.checkerframework.checker.units.qual.K;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//public class SingleWrappedFragment extends Fragment {
//
//    private FragmentSingleWrappedBinding binding;
//    private MediaPlayer m;
//    private int currentSongIndex = 0;
//
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        // fSingleWrappedViewModel singleWrappedViewModel = new ViewModelProvider(this).get(SingleWrappedViewModel.class);
//
//        binding = FragmentSingleWrappedBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        // Assume AuthenticationActivity.currentUser is already initialized and contains user data
//        if (AuthenticationActivity.currentUser != null) {
//            String userName = AuthenticationActivity.currentUser.getName() + "'s\nSpotify Wrapped 2024";
//            TextView usernameTextView = root.findViewById(R.id.textViewTitle);
//            usernameTextView.setText(userName);  // Set the user's name to the TextView
//        }
//
//
//        //Collects data for the top 5 songs
//        ArrayList<Song> totalSONGS = (AuthenticationActivity.currentUser.getTopFiveSongs());
//        //Collects data for the top 5 artists
//        ArrayList<Artist> totalARTISTS = (AuthenticationActivity.currentUser.getTopFiveArtists());
//        int[] textviewartists = new int[]{R.id.textViewArtist1, R.id.textViewArtist2, R.id.textViewArtist3, R.id.textViewArtist4, R.id.textViewArtist5};
//
//        int i = 0;
//        while (i < totalARTISTS.size()) {
//            // 1. "artist name"
//            // 2. "artist name"
//            // etc..
//            String description = (i + 1) + ". " + totalARTISTS.get(i).getName();
//            ((TextView) root.findViewById(textviewartists[i])).setText(description);
//            i++;
//        }
//        while (i < textviewartists.length) {
//            ((TextView) root.findViewById(textviewartists[i])).setText("");
//            i++;
//        }
//
//        String url = totalARTISTS.get(0).getPicture();
//        ImageView imageView = root.findViewById(R.id.imageView);
//        Picasso.get().load(url)
//                .placeholder(R.drawable.imgload)
//                .into(imageView);
//
//
//        //ARTIST 0: genre list, 1: popularity, 2: artist name, 3: artist pic url
//        //SONG 0: preview url, 1: artist name, 2: song name, 3: popularity
//        int[] textviewsongs = new int[]{R.id.textViewSong1, R.id.textViewSong2, R.id.textViewSong3, R.id.textViewSong4, R.id.textViewSong5};
//
//        int z = 0;
//        // 1. "Top 1 song title"
//        // 2. "Top 2 song title"
//        // etc..
//        while (z < totalSONGS.size()) {
//            String description = (z + 1) + ". " +  (String) totalSONGS.get(z).getName() + " by " + totalSONGS.get(z).getArtist();
//            ((TextView) root.findViewById(textviewsongs[z])).setText(description);
//            z++;
//        }
//        while (z < textviewsongs.length) {
//            ((TextView) root.findViewById(textviewsongs[z])).setText("");
//            z++;
//        }
//
//        //String of urls
//        String[] songUrls = new String[]{totalSONGS.get(0).getPreview(), totalSONGS.get(1).getPreview(), totalSONGS.get(2).getPreview(), totalSONGS.get(3).getPreview(), totalSONGS.get(4).getPreview()};
//        ArrayList<String> nonNullSongs = returnNonNull(songUrls);
//
//        //Starts song
//        m = new MediaPlayer();
//        String songLink = totalSONGS.get(0).getPreview();
//        startAudioStream(songLink, m);
//        currentSongIndex++;
//
//        //plays music repeatedly, if the amount of calls exceeds the amount of
//        musicPlaylist(m, nonNullSongs);
//        musicPlaylist(m, nonNullSongs);
//        musicPlaylist(m, nonNullSongs);
//        musicPlaylist(m, nonNullSongs);
//
//
//        final TextView textView = binding.textViewArtist1;
//        return root;
//    }
//
//    private ArrayList<String> returnNonNull(String[] n) {
//        ArrayList<String> ct = new ArrayList<>();
//        for (String i : n) {
//            if ((i != null) && (!i.equals("null"))) {
//                Log.d("null loop", i);
//                ct.add(i);
//            }
//        }
//        return ct;
//    }
//
//    private void musicPlaylist(MediaPlayer m, ArrayList<String> s) {
//
//        m.setOnCompletionListener(mp -> {
//            //currentSongIndex++;
//            //startAudioStream(totalSONGS.get(1).getPreview(), m);
//            m.reset();
//            if (currentSongIndex >= s.size()) {
//                return;
//            }
//            Log.d("Song playing", s.get(currentSongIndex));
//            startAudioStream(s.get(currentSongIndex), m);
//            currentSongIndex++;
//        });
//    }
//
//    public void startAudioStream(String url, MediaPlayer m) {
//        try {
//            Log.d("mylog", "Playing: " + url);
//            m.setAudioAttributes(
//                    new AudioAttributes
//                            .Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .build());
//            m.setDataSource(url);
//            m.prepare();
//            m.setVolume(1f, 1f);
//            m.setLooping(false);
//            m.start();
//
//        } catch (Exception e) {
//            Log.d("mylog", "Error playing in SoundHandler: " + e.toString());
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//        m.stop();
//    }
//}