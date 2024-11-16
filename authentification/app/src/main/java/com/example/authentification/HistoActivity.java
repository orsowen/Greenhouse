package com.example.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class HistoActivity extends AppCompatActivity {

    private Button returnMenu;
    private Controle controle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_histo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        returnMenu = findViewById(R.id.button);
        // Listen for Firestore document updates in real-time
        eventlistener();
    }

    private void eventlistener(){
        returnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à réaliser lors du clic
                Intent intent = new Intent(HistoActivity.this, NextActivity.class);
                startActivity(intent);
            }
        });
    }

    private void creerListe(){
        ArrayList<Jour> Jours = controle.getjours() ;
        if (Jours !=null){
            ListView lstHisto = (ListView) findViewById(R.id.LS_stat);
            HistoListAdapter adapter = new HistoListAdapter(this, Jours);
            lstHisto.setAdapter(adapter);
        }
    }
}