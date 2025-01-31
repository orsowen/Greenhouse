package com.example.react_eco_orge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Historique extends AppCompatActivity {
    //for test
    /*int humidty[] = {55,25,14};
    int temp[] = {55,25,14};
    int water_lvl[] = {55,25,14};
    String days[] = {"01/07/2021 09:47","01/07/2021 09:47","01/07/2021 09:47"};*/

    List<Double> historyHumd,historyTemp,historyWaterTemp;
    List<String> days;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get the data passed via the Intent
        Intent intent = getIntent();

        // If you passed lists as Serializable or Parcelable, retrieve them here
        historyHumd = (List<Double>) intent.getSerializableExtra("historyHumd");
        historyTemp = (List<Double>) intent.getSerializableExtra("historyTemp");
        historyWaterTemp = (List<Double>) intent.getSerializableExtra("historyWaterTemp");
        days = (List<String>) intent.getSerializableExtra("date");
        // Log or use the data (optional)
        Log.d("Historique", "History Humidity: " + historyHumd);
        Log.d("Historique", "History Temp: " + historyTemp);
        Log.d("Historique", "History Water Temp: " + historyWaterTemp);

        listView = (ListView) findViewById(R.id.LVHistory);
        CustomAdap customAdapter = new CustomAdap(getApplicationContext(),historyHumd.toArray(),historyTemp.toArray(),days.toArray(),historyWaterTemp.toArray());
        listView.setAdapter(customAdapter);
    }
}