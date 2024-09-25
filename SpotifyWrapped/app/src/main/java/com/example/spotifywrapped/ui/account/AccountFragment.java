package com.example.spotifywrapped.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifywrapped.AuthenticationActivity;
import com.example.spotifywrapped.MainActivity;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.authentication.SpotifyAccountFragment;
import com.example.spotifywrapped.databinding.FragmentAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private Button change;
    private Button logout;
    private Button delete;
    private TextInputLayout editPassword;
    private TextInputLayout confirmPassword;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textAccount;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        change = binding.changeButton;
        logout = binding.logoutButton;
        delete = binding.deleteButton;
        editPassword = binding.editPassword;
        confirmPassword = binding.editConfirm;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textPassword = editPassword.getEditText().getText().toString();
                String textConfirm = confirmPassword.getEditText().getText().toString();
                if (passwordValidated(textPassword, textConfirm)) {
                    changePassword(textPassword);
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), AuthenticationActivity.class);
                startActivity(i);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.getEmail())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "User account deleted.");
                                }
                            }
                        });

                Intent i = new Intent(getContext(), AuthenticationActivity.class);
                startActivity(i);
            }
        });
    }
    private boolean passwordValidated(String textPassword, String textConfirm) {
        editPassword.setError(null);
        confirmPassword.setError(null);
        if (TextUtils.isEmpty(textPassword)) {
            Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_LONG).show();
            editPassword.setError("Password is required");
            editPassword.requestFocus();
        } else if (TextUtils.isEmpty(textConfirm)) {
            Toast.makeText(getActivity(), "Please confirm your password", Toast.LENGTH_LONG).show();
            confirmPassword.setError("Confirmation of password is required");
            confirmPassword.requestFocus();
        } else if (textPassword.length() < 6) {
            Toast.makeText(getActivity(), "Your password is too short", Toast.LENGTH_LONG).show();
            editPassword.setError("Password must be at least 6 characters");
            editPassword.requestFocus();
        } else if (!textPassword.equals(textConfirm)) {
            Toast.makeText(getActivity(), "Your passwords don't match", Toast.LENGTH_LONG).show();
            confirmPassword.setError("Your passwords must match");
            confirmPassword.requestFocus();
            editPassword.getEditText().getText().clear();
            confirmPassword.getEditText().getText().clear();
        } else {
            return true;
        }
        return false;
    }
    private void changePassword(String passwordT) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(passwordT)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User password updated.");
                            Toast.makeText(getActivity(), "Your password has been updated", Toast.LENGTH_LONG).show();
                            editPassword.getEditText().getText().clear();
                            confirmPassword.getEditText().getText().clear();
                        } else {
                            Log.d("TAG", "User password not updated.");
                            Toast.makeText(getActivity(), "Your password has not been updated", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}