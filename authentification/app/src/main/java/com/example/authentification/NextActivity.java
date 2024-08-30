package com.example.authentification;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class NextActivity extends AppCompatActivity {
    private ProgressBar progressBarWaterLevel, progressBarPH, progressBarNutrient;
    private TextView textViewWaterTemp, textViewAirTemp, textViewAirHumidity;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // Initialize the ProgressBar and TextView instances
        progressBarWaterLevel = findViewById(R.id.progressBar);
        progressBarPH = findViewById(R.id.progressBar2);
        progressBarNutrient = findViewById(R.id.progressBar3);


        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get current user's ID
        userId = auth.getCurrentUser().getUid();

        // Reference to the user's document in Firestore
        DocumentReference docRef = db.collection("users").document(userId);

        // Listen for Firestore document updates

        textViewWaterTemp = findViewById(R.id.wTemp);
        textViewAirTemp = findViewById(R.id.aTemp);
        textViewAirHumidity = findViewById(R.id.aHum);

        // Listen for Firestore document updates in real-time
        listenToFirestoreChanges();

    }
    private void listenToFirestoreChanges() {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    textViewWaterTemp.setText("Error: " + e.getMessage());
                    textViewAirTemp.setText("Error: " + e.getMessage());
                    textViewAirHumidity.setText("Error: " + e.getMessage());
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve the fields from the document
                    String waterTemp = documentSnapshot.getString("waterTemp");
                    String airTemp = documentSnapshot.getString("airTemp");
                    String airHumidity = documentSnapshot.getString("airHumidity");

                    // Assuming the progress bar values are stored as integers or doubles
                    Double waterLevel = documentSnapshot.getDouble("waterLevel");
                    Double phLevel = documentSnapshot.getDouble("waterPH");
                    Double nutrientLevel = documentSnapshot.getDouble("waterNutrient");

                    // Set the data to the corresponding TextViews
                    textViewWaterTemp.setText(waterTemp);
                    textViewAirTemp.setText(airTemp);
                    textViewAirHumidity.setText(airHumidity);

                    // Set the data to the corresponding ProgressBars
                    if (waterLevel != null) {
                        progressBarWaterLevel.setProgress(waterLevel.intValue());
                    }

                    if (phLevel != null) {
                        progressBarPH.setProgress(phLevel.intValue());
                    }

                    if (nutrientLevel != null) {
                        progressBarNutrient.setProgress(nutrientLevel.intValue());
                    }
                } else {
                    textViewWaterTemp.setText("No data");
                    textViewAirTemp.setText("No data");
                    textViewAirHumidity.setText("No data");

                    progressBarWaterLevel.setProgress(0);
                    progressBarPH.setProgress(0);
                    progressBarNutrient.setProgress(0);
                }
            }
        });
    }
    }



