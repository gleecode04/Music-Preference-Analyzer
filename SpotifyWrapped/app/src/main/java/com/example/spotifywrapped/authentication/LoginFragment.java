package com.example.spotifywrapped.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spotifywrapped.AuthenticationActivity;
import com.example.spotifywrapped.MainActivity;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private TextInputLayout email;
    private TextInputLayout password;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        Button login_button = view.findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = email.getEditText().getText().toString();
                String textPassword = password.getEditText().getText().toString();
                signInUser(textEmail, textPassword);
            }
        });
    }



    private void signInUser(String textEmail, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("YAYYYY", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(user.getEmail());
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
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            email.getEditText().getText().clear();
                            password.getEditText().getText().clear();
                        }
                    }
                });
    }
}