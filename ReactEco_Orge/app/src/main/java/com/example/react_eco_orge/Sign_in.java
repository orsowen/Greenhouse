package com.example.react_eco_orge;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class Sign_in extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Sign_in.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate the user
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Sign_in.this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = auth.getCurrentUser();
                        Intent intent = new Intent(Sign_in.this, Main_interface.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed
                        Toast.makeText(Sign_in.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signUpTextView.setOnClickListener(v -> {
            // Handle sign-up navigation
            Intent intent = new Intent(Sign_in.this, Sign_up.class);
            startActivity(intent);
        });

    }
}