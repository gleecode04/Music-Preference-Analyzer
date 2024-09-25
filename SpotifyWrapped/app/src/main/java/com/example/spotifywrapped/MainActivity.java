package com.example.spotifywrapped;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide; // Correct import for Glide


import com.example.spotifywrapped.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Ensure there's only one header
        if (navigationView.getHeaderCount() > 0) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
        }

        // Inflate the custom header layout
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        TextView usernameTextView = headerView.findViewById(R.id.nav_header_user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.nav_header_user_email);
        ImageView profileImageView = headerView.findViewById(R.id.imageView);

        setupUserDataDisplay(usernameTextView, userEmailTextView, profileImageView);

        // After resolving the conflict
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_singleWrapped, R.id.nav_feed, R.id.nav_account
        ).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupUserDataDisplay(TextView usernameTextView, TextView userEmailTextView, ImageView profileImageView) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String userName = (AuthenticationActivity.currentUser != null)
                ? AuthenticationActivity.currentUser.getName()
                : "Guest";
        usernameTextView.setText(userName);

        String userEmail = (user != null && user.getEmail() != null)
                ? user.getEmail()
                : "No email available";
        userEmailTextView.setText(userEmail);

        String profilePictureUrl = (AuthenticationActivity.currentUser != null)
                ? AuthenticationActivity.currentUser.getProfile_picture()
                : null;

        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this)
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.ic_launcher_foreground) // Default
                    .into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.ic_launcher_foreground); // Default
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}