package com.example.authentification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlertActivity extends Activity {
    private Button btnStopAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        btnStopAlert = findViewById(R.id.btnStopAlert);

        btnStopAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlert();
            }
        });
    }

    private void stopAlert() {
        // Implement logic to stop the alert, such as stopping the ringtone or notification
        Intent intent = new Intent(this, NextActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();  // Close this activity
    }
}
