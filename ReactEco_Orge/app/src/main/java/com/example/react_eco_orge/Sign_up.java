package com.example.react_eco_orge;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText name;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private FirebaseFirestore fstore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.loginTextView);
        name = findViewById(R.id.nom);
        signupButton.setOnClickListener(v -> registerUser());

        loginTextView.setOnClickListener(v -> {
            // Navigate to login page
            Intent intent = new Intent(Sign_up.this, Sign_in.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String namef = name.getText().toString();

        String waterLevel = "0.0";
        String waterPH = "0.0";
        String waterNutrient = "0.0";
        String waterTemp = "0.0";
        String airTemp = "26.0";
        String airHumidity = "0.0";
        List<String> date = new ArrayList<>();
        List<Double> historyHumd = new ArrayList<>();
        List<Double> historytemp = new ArrayList<>();
        List<Double> historywaterTemp = new ArrayList<>();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, navigate to next activity or main screen
                            Toast.makeText(Sign_up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            userId = auth.getCurrentUser().getUid();

                            DocumentReference documentReference = fstore.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", namef);
                            user.put("waterLevel", Double.parseDouble(waterLevel));
                            user.put("waterPH", Double.parseDouble(waterPH));
                            user.put("waterNutrient", Double.parseDouble(waterNutrient));
                            user.put("waterTemp", Double.parseDouble(waterTemp));
                            user.put("airTemp", Double.parseDouble(airTemp));
                            user.put("airHumidity", Double.parseDouble(airHumidity));
                            user.put("date", date);
                            user.put("historyHumd", historyHumd); // Storing humidity data in a list
                            user.put("historytemp", historytemp); // Storing temperature data in a list
                            user.put("historywaterTemp", historywaterTemp); // Storing water temperature in a list

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", userId);

                                }
                            });

                            Intent intent = new Intent(Sign_up.this, Main_interface.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(Sign_up.this, "Registration Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}