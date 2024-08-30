package com.example.authentification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Random;

public class NextActivity extends AppCompatActivity {
    private ProgressBar progressBarWaterLevel, progressBarPH, progressBarNutrient;
    private TextView textViewWaterTemp, textViewAirTemp, textViewAirHumidity;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String CHANNEL_ID = "water_level_alert_channel";
    private static final int NOTIFICATION_ID = 001;

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

        // Initialize TextViews
        textViewWaterTemp = findViewById(R.id.wTemp);
        textViewAirTemp = findViewById(R.id.aTemp);
        textViewAirHumidity = findViewById(R.id.aHum);

        // Listen for Firestore document updates in real-time
        listenToFirestoreChanges();
        createNotificationChannel();
    }

    private void listenToFirestoreChanges() {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Log and show error messages
                    textViewWaterTemp.setText("Error: " + e.getMessage());
                    textViewAirTemp.setText("Error: " + e.getMessage());
                    textViewAirHumidity.setText("Error: " + e.getMessage());
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    try {
                        // Retrieve the fields from the document
                        Double waterTemp = documentSnapshot.getDouble("waterTemp");
                        Double airTemp = documentSnapshot.getDouble("airTemp");
                        Double airHumidity = documentSnapshot.getDouble("airHumidity");
                        Double waterLevel = documentSnapshot.getDouble("waterLevel");
                        Double phLevel = documentSnapshot.getDouble("waterPH");
                        Double nutrientLevel = documentSnapshot.getDouble("waterNutrient");

                        // Update TextViews and ProgressBars
                        if (waterTemp != null) {
                            textViewWaterTemp.setText(waterTemp + "°C");
                            if (waterTemp < 17) {
                                sendNotification("Water Temperature Alert", "Temperature is "+waterTemp+" Heat the water.");
                            } else if (waterTemp > 20) {
                                sendNotification("Water Temperature Alert", "Temperature is "+waterTemp+" cool the water.");
                            }

                        }
                        if (airTemp != null) {
                            textViewAirTemp.setText(airTemp + "°C");
                        }
                        if (airHumidity != null) {
                            textViewAirHumidity.setText(airHumidity + "%");
                        }

                        if (waterLevel != null) {
                            progressBarWaterLevel.setProgress(waterLevel.intValue());
                            if (waterLevel < 40) {
                                sendNotification("Water Level Alert", "Water level is below 40! add nutrient ");
                            }
                        }

                        if (phLevel != null) {
                            progressBarPH.setProgress(phLevel.intValue());
                            if (phLevel < 5.5) {
                                sendNotification("pH Level Alert", " Ph level is "+phLevel+" Add base.");
                            }
                            if (phLevel > 6.5) {
                                sendNotification("pH Level Alert", " Ph level is "+phLevel+" Add acid.");
                            }
                        }

                        if (nutrientLevel != null) {
                            progressBarNutrient.setProgress(nutrientLevel.intValue());
                        }

                        // Example: Trigger notifications based on temperature and humidity
                        if (airTemp != null) {
                            if (airTemp < 18) {
                                sendNotification("Air Temperature Alert", "Temperature is "+airTemp+" Heat the air.");
                            } else if (airTemp > 21) {
                                sendNotification("Air Temperature Alert", "Temperature is "+airTemp+" cool the air.");
                            }
                        }

                        if (airHumidity != null) {
                            if (airHumidity < 60) {
                                sendNotification("Air Humidity Alert", "Humidity is "+airHumidity+" Irrigate the plants.");
                            } else if (airHumidity > 70) {
                                sendNotification("Air Humidity Alert", "Humidity is "+airHumidity+" Open the windows.");
                            }
                        }

                    } catch (Exception ex) {
                        // Log and show error messages
                        textViewWaterTemp.setText("Error: " + ex.getMessage());
                        textViewAirTemp.setText("Error: " + ex.getMessage());
                        textViewAirHumidity.setText("Error: " + ex.getMessage());
                    }
                } else {
                    // Handle case where no data is available
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

    private void sendNotification(String msgTitle, String msgContent) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, NextActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        int notificationId = new Random().nextInt(10000); // Random ID for demonstration
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning)  // Replace with your app's icon
                .setContentTitle(msgTitle)
                .setContentText(msgContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alerts";
            String description = "Notifications for water level and environmental alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
