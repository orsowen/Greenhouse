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
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main_interface extends AppCompatActivity {
    private TextView textViewSoilMoisture, textViewTemperature;
    private ImageView waterlevelImg;
    private LinearLayout histozone;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String CHANNEL_ID = "sensor_alerts";
    private List<Double> historyHumd, historyTemp;
    private List<String> date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        // Initialisation des vues
        textViewSoilMoisture = findViewById(R.id.wLevel);
        textViewTemperature = findViewById(R.id.aTemp);
        waterlevelImg = findViewById(R.id.water_level_img);
        histozone = findViewById(R.id.HistoriqueLayout);

        // Initialisation Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            listenToFirestoreChanges();
            createNotificationChannel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData(); // Force le rafraîchissement à chaque retour sur l'activité
    }

    // Ajoutez un menu ou bouton pour rafraîchir manuellement
    public void onRefreshClick(View view) {
        refreshData();
        Toast.makeText(this, "Actualisation...", Toast.LENGTH_SHORT).show();
    }

    private void refreshData() {
        DocumentReference docRef = db.collection("users").document(userId);

        // Désactive le cache pour cette requête
        docRef.get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Double temperature = document.getDouble("temperature");
                            Log.d("RefreshDebug", "Valeur serveur temperature: " + temperature);
                            // Mettre à jour l'UI...
                        }
                    } else {
                        Log.e("RefreshError", "Erreur rafraîchissement", task.getException());
                    }
                });
    }

    private void listenToFirestoreChanges() {
        DocumentReference docRef = db.collection("users").document(userId);

        // Ajoutez ce log pour vérifier quel utilisateur est concerné
        Log.d("FirestoreDebug", "Écoute des changements pour user: " + userId);

        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("FirestoreError", "Écoute failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Ajoutez ce log pour voir les métadonnées
                    Log.d("FirestoreDebug", "Données reçues. Depuis le cache? " +
                            documentSnapshot.getMetadata().isFromCache());

                    try {
                        Double soilMoisture = documentSnapshot.getDouble("soil_moisture");
                        Double temperature = documentSnapshot.getDouble("temperature");

                        // Log des valeurs reçues
                        Log.d("FirestoreDebug", "Valeurs reçues - soil_moisture: " + soilMoisture +
                                ", temperature: " + temperature);

                        // Mise à jour UI
                        runOnUiThread(() -> {
                            if (soilMoisture != null) {
                                textViewSoilMoisture.setText(String.format("%.0f%%", soilMoisture));
                                waterlevelImg.setScaleY(soilMoisture.floatValue() / 100f);
                            }
                            if (temperature != null) {
                                textViewTemperature.setText(String.format("%.1f°C", temperature));
                            }
                        });

                    } catch (Exception ex) {
                        Log.e("FirestoreError", "Erreur traitement données", ex);
                    }
                }
            }
        });
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(new Random().nextInt(), builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sensor Alerts",
                    NotificationManager.IMPORTANCE_HIGH);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }
}