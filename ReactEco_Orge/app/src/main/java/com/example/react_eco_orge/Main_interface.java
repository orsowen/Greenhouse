package com.example.react_eco_orge;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main_interface extends AppCompatActivity {

    private ImageView waterlevelImg;
    private TextView textViewWaterLevel,textViewWaterTemp, textViewAirTemp, textViewAirHumidity,textViewPH,textViewNutrient;
    private LinearLayout histozone;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String CHANNEL_ID = "water_level_alert_channel";
    private static final int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        // Initialize the ProgressBar and TextView instances
        textViewWaterLevel = findViewById(R.id.wLevel);
        textViewNutrient = findViewById(R.id.Nutrient);

        // Initialize TextViews
        textViewWaterTemp = findViewById(R.id.wTemp);
        textViewPH = findViewById(R.id.PH);
        textViewAirTemp = findViewById(R.id.aTemp);
        textViewAirHumidity = findViewById(R.id.aHum);
        histozone = findViewById(R.id.HistoriqueLayout);



        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user!= null) {
            // Get current user's ID
            userId = auth.getCurrentUser().getUid();
            listenToFirestoreChanges();
            createNotificationChannel();
        }
        // Listen for Firestore document updates in real-time
        eventlistener(!(user == null));

    }

    private void eventlistener(boolean UserExist){
        histozone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à réaliser lors du clic
                if (UserExist)
                {
                Intent intent = new Intent(Main_interface.this, Historique.class);
                startActivity(intent);
                }
                else {
                    Toast.makeText(Main_interface.this, "aucun base de donnee trouver s'il vous voyez verifier si vous ete connecter", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void listenToFirestoreChanges() {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
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
                            textViewAirTemp.setText(String.format("%.2f°C", airTemp));

                        } else {
                            textViewAirTemp.setText("N/A"); // Handle the case where airTemp is null
                    }
                        if (airHumidity != null) {
                            textViewAirHumidity.setText(airHumidity + "%");
                        }else {
                            textViewAirHumidity.setText("N/A"); // Handle the case where airTemp is null
                    }
                        if (waterLevel != null) {
                            waterlevelImg = (ImageView) findViewById(R.id.water_level_img);
                            waterlevelImg.setPivotY(waterlevelImg.getHeight());
                            waterlevelImg.animate().scaleY(waterLevel.floatValue() / 100f).setDuration(1000);
                            textViewWaterLevel.setText(waterLevel.intValue()+"%");
                            if (waterLevel < 40) {
                                sendNotification("Water Level Alert", "Water level is below 40! add nutrient ");
                            }
                        }
                        if (phLevel != null) {
                            textViewPH.setText(phLevel+"");
                            if (phLevel < 5.5) {
                                sendNotification("pH Level Alert", " Ph level is "+phLevel+" Add base.");
                            }
                            if (phLevel > 6.5) {
                                sendNotification("pH Level Alert", " Ph level is "+phLevel+" Add acid.");
                            }
                        }

                        if (nutrientLevel != null) {
                            textViewNutrient.setText(String.valueOf(nutrientLevel.intValue()));
                        } else {
                            textViewNutrient.setText("N/A"); // Handle the case where airTemp is null
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
                        Log.e("FirestoreError", "Error retrieving data", ex);
                        Toast.makeText(Main_interface.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle case where no data is available
                    textViewWaterTemp.setText("No data");
                    textViewAirTemp.setText("No data");
                    textViewAirHumidity.setText("No data");
                    textViewPH.setText("No data");
                    textViewNutrient.setText("No data");
                    textViewWaterLevel.setText("No data");
                }
            }
        });
    }

    private void sendNotification(String msgTitle, String msgContent) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Main_interface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        int notificationId = new Random().nextInt(10000); // Random ID for demonstration
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning)  // Replace with your app's icon
                .setContentTitle(msgTitle)
                .setContentText(msgContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
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