package com.example.googleloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutButton;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);
        auth = FirebaseAuth.getInstance();

        googleSignInClient = GoogleSignIn.getClient(this,
                new com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                        com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
        );

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            welcomeText.setText("Welcome, " + user.getDisplayName() + "!");
        } else {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            });
        });
    }
}
