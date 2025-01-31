package com.example.react_eco_orge;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Historique extends AppCompatActivity {
    int humidty[] = {55,25,14};
    int temp[] = {55,25,14};
    int water_lvl[] = {55,25,14};
    String days[] = {"01/07/2021 09:47","01/07/2021 09:47","01/07/2021 09:47"};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.LVHistory);
        CustomAdap customAdapter = new CustomAdap(getApplicationContext(),humidty,temp,days,water_lvl);
        listView.setAdapter(customAdapter);
    }
}