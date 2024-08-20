package com.example.hydroponicsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView temperatureText;
    private TextView humidityText;
    private TextView phLevelText;
    private TextView waterLevelText;
    private TextView nutrientLevelText;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TextView elements
        temperatureText = findViewById(R.id.temperatureText);
        humidityText = findViewById(R.id.humidityText);
        phLevelText = findViewById(R.id.phLevelText);
        waterLevelText = findViewById(R.id.waterLevelText);
        nutrientLevelText = findViewById(R.id.nutrientLevelText);

        // Get a reference to the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("hydroponics");

        // Read data from Firebase and update the UI
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve values as strings
                String temperatureStr = dataSnapshot.child("temperature").getValue(String.class);
                String humidityStr = dataSnapshot.child("humidity").getValue(String.class);
                String phLevelStr = dataSnapshot.child("phLevel").getValue(String.class);
                String waterLevelStr = dataSnapshot.child("waterLevel").getValue(String.class);
                String nutrientLevelStr = dataSnapshot.child("nutrientLevel").getValue(String.class);

                // Convert strings to their appropriate types
                Double temperature = temperatureStr != null ? Double.parseDouble(temperatureStr) : null;
                Integer humidity = humidityStr != null ? Integer.parseInt(humidityStr) : null;
                Double phLevel = phLevelStr != null ? Double.parseDouble(phLevelStr) : null;
                Integer waterLevel = waterLevelStr != null ? Integer.parseInt(waterLevelStr) : null;
                Integer nutrientLevel = nutrientLevelStr != null ? Integer.parseInt(nutrientLevelStr) : null;

                // Update the TextViews with the retrieved values
                if (temperature != null) {
                    temperatureText.setText("Temperature: " + temperature + "°C");
                }
                if (humidity != null) {
                    humidityText.setText("Humidity: " + humidity + "%");
                }
                if (phLevel != null) {
                    phLevelText.setText("pH Level: " + phLevel);
                }
                if (waterLevel != null) {
                    waterLevelText.setText("Water Level: " + waterLevel + "%");
                }
                if (nutrientLevel != null) {
                    nutrientLevelText.setText("Nutrient Level: " + nutrientLevel + "%");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log an error message if data retrieval fails
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });
    }
}
