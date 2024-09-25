package com.example.spotifywrapped.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotifywrapped.AuthenticationActivity;
import com.example.spotifywrapped.MainActivity;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.custom_exceptions.SpotifyMismatchException;
import com.example.spotifywrapped.data_classes.Artist;
import com.example.spotifywrapped.data_classes.Song;
import com.example.spotifywrapped.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyAccountFragment extends Fragment {
    private static final String CLIENT_ID = "1b5ff154cb554dbc9e189a18aff5d701";
    private static final String REDIRECT_URI = "com.example.spotifywrapped://auth";
    private static final int REQUEST_CODE = 69;
    private String access_token, access_code;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private Call uCall;
    private Call aCall;

    public SpotifyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spotify_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button token_btn = (Button) view.findViewById(R.id.connect_button);
        token_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationRequest.Builder builder =
                        new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
                builder.setScopes(new String[] {
                        "user-read-email",
                        "streaming",
                        "user-top-read"
                });
                AuthorizationRequest request = builder.build();

                Intent intent = AuthorizationClient.createLoginActivityIntent(getActivity(), request);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String token = response.getAccessToken();
                    Log.d("TokenRecieved", token);
                    final Request userRequest = new Request.Builder()
                            .url("https://api.spotify.com/v1/me")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    uCall = mOkHttpClient.newCall(userRequest);
                    final Request musicRequest = new Request.Builder()
                            .url("https://api.spotify.com/v1/me/top/tracks?time_range=medium_term&limit=5")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    mCall = mOkHttpClient.newCall(musicRequest);
                    final Request artistRequest = new Request.Builder()
                            .url("https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=5&offset=0")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    aCall = mOkHttpClient.newCall(artistRequest);

                    uCall.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("HTTP", "Failed to fetch data: " + e);
                            Toast.makeText(requireActivity(), "Failed to fetch data, watch Logcat for more details",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                final JSONObject jsonObject = new JSONObject(response.body().string());
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                Map < String, Object > userData = new HashMap < > ();
                                DocumentReference docRef = db.collection("users").document(user.getEmail());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            try {
                                                if (document.exists() && !document.get("spotify_id").equals(jsonObject.get("id"))) {
                                                    throw new SpotifyMismatchException();
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                });
                                userData.put("name", jsonObject.get("display_name"));
                                userData.put("spotify_id", jsonObject.get("id"));
                                userData.put("last_updated", java.time.LocalDateTime.now().toString());
                                JSONArray profilepic = (JSONArray) jsonObject.get("images");
                                if (profilepic.length() == 0) {
                                    userData.put("profile_picture", null);
                                } else {
                                    JSONObject image = (JSONObject) profilepic.get(0);
                                    userData.put("profile_picture", image.get("url").toString());
                                }

                                db.collection("users").document(user.getEmail())
                                        .set(userData)
                                        .addOnSuccessListener(new OnSuccessListener < Void > () {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(requireActivity(),
                                                        "Spotify data synced!",
                                                        Toast.LENGTH_SHORT).show();
                                                Log.d("yay", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("NOOO", "Error writing document", e);
                                            }
                                        });


                                    mCall.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("HTTP", "Failed to fetch data: " + e);
                                            Toast.makeText(requireActivity(), "Failed to fetch data, watch Logcat for more details",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            try {
                                                final JSONObject jsonObject = new JSONObject(response.body().string());
                                                JSONArray items = (JSONArray) jsonObject.get("items");
                                                Log.d("Test", (String) jsonObject.toString());
                                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                                FirebaseUser user = auth.getCurrentUser();
                                                Map<String, Object> userData = new HashMap<>();
                                                int total = (int) jsonObject.get("limit");
                                                if ((int) jsonObject.get("total") < total) {
                                                    total = (int) jsonObject.get("total");
                                                }
                                                Song[] topFive = new Song[total];
                                                for (int i = 0; i < total; i++) {
                                                    JSONObject track = items.getJSONObject(i);
                                                    JSONArray artists = (JSONArray) track.get("artists");
                                                    JSONObject artist = (JSONObject) artists.get(0);
                                                    JSONObject album = (JSONObject) track.get("album");
                                                    Song song = new Song(track.get("name").toString(),
                                                            artist.get("name").toString(),
                                                            album.get("name").toString(),
                                                            track.get("preview_url").toString(),
                                                            (int) track.get("popularity"));
                                                    topFive[i] = song;
                                                }
                                                List<Song> topFiveList = Arrays.asList(topFive);
                                                userData.put("topFiveSongs", topFiveList);

                                                db.collection("users").document(user.getEmail())
                                                        .update(userData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(requireActivity(),
                                                                        "Spotify data synced!",
                                                                        Toast.LENGTH_SHORT).show();
                                                                Log.d("yay", "DocumentSnapshot successfully written!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("NOOO", "Error writing document", e);
                                                            }
                                                        });
                                            } catch (JSONException e) {
                                                Log.d("JSON", "Failed to parse data: " + e);
                                                Toast.makeText(requireActivity(), "Failed to parse data, watch Logcat for more details",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    aCall.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("HTTP", "Failed to fetch data: " + e);
                                            Toast.makeText(requireActivity(), "Failed to fetch data, watch Logcat for more details",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            try {
                                                final JSONObject jsonObject = new JSONObject(response.body().string());
                                                JSONArray items = (JSONArray) jsonObject.get("items");
                                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                                FirebaseUser user = auth.getCurrentUser();
                                                Map<String, Object> userData = new HashMap<>();
                                                int total = (int) jsonObject.get("limit");
                                                if ((int) jsonObject.get("total") < total) {
                                                    total = (int) jsonObject.get("total");
                                                }
                                                Artist[] topFive = new Artist[total];
                                                for (int i = 0; i < total; i++) {
                                                    JSONObject artist = items.getJSONObject(i);
                                                    JSONArray images = (JSONArray) artist.get("images");
                                                    JSONObject image = (JSONObject) images.get(0);
                                                    JSONArray genres = (JSONArray) artist.get("genres");
                                                    String[] newGenres = new String[genres.length()];
                                                    for (int j = 0; j < genres.length(); j++) {
                                                        newGenres[j] = (String) genres.get(j);
                                                    }
                                                    List<String> genre = Arrays.asList(newGenres);
                                                    Artist currArtist = new Artist(artist.get("name").toString(),
                                                            image.get("url").toString(),
                                                            genre,
                                                            (int) artist.get("popularity"));
                                                    topFive[i] = currArtist;
                                                }
                                                List<Artist> topFiveList = Arrays.asList(topFive);
                                                userData.put("topFiveArtists", topFiveList);

                                                db.collection("users").document(user.getEmail())
                                                        .update(userData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(requireActivity(),
                                                                        "Spotify data synced!",
                                                                        Toast.LENGTH_SHORT).show();
                                                                Log.d("yay", "DocumentSnapshot successfully written!");
                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                Log.d("Hello", "DocumentSnapshot data: " + document.getData());
                                                                                AuthenticationActivity.currentUser = new User(document);
                                                                                Log.d("HELLO", AuthenticationActivity.currentUser.toString());
                                                                                Intent i = new Intent(getContext(), MainActivity.class);
                                                                                startActivity(i);
                                                                            } else {
                                                                                Log.d("HELLO", "No such document");
                                                                            }
                                                                        } else {
                                                                            Log.d("HELLO", "get failed with ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("NOOO", "Error writing document", e);
                                                            }
                                                        });
                                            } catch (JSONException e) {
                                                Log.d("JSON", "Failed to parse data: " + e);
                                                Toast.makeText(requireActivity(), "Failed to parse data, watch Logcat for more details",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            }
                            catch (JSONException e) {
                                Log.d("JSON", "Failed to parse data: " + e);
                                Toast.makeText(requireActivity(), "Failed to parse data, watch Logcat for more details",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

}