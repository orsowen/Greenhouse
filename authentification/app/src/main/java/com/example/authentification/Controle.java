package com.example.authentification;

import android.content.Context;
import android.service.controls.Control;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public final class Controle {

    public static String date;
    private static Controle instance = null;
    private static Jour jour;
    private static ArrayList<Jour> jours = new ArrayList<Jour>();
    private static String FileName = "saveddata";

    private Controle(){
        super();
    }

    public static final Controle getInstance(Context context){
        if (Controle.instance == null) {
            Controle.instance = new Controle();
            recupSerialize(context);  // Deserialize saved data
        }
        String currentDate  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Controle.date = currentDate;  // Utiliser la méthode setDate pour stocker la date
        return Controle.instance;
    }


    public void creerjour(Context  context){
        // Create a new Jour object with the current date
        jour = new Jour(Controle.date);
        // Add the new Jour object to the list
        Controle.jours.add(jour);
        // Serialize the list of jours
        Serializer.serialize(FileName, jours, context);
    }

    private static void  recupSerialize(Context context){
        try {
            // Try to deserialize the list of Jour objects
            Controle.jours = (ArrayList<Jour>) Serializer.deserialize(Controle.FileName, context);
            if (!jours.isEmpty()) {
                jour = jours.get(jours.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // If deserialization fails, initialize an empty list of jours
            Controle.jours = new ArrayList<>();
        }
    }

    public Double getTempValueA() {
        return jour.getTempValueA();
    }
    public Double getHumValueA() {

        return jour.getHumValueA();
    }
    public Double getTempVAlueW() {

        return jour.getTempVAlueW();
    }
    public Double getWaterLevel() {

        return jour.getWaterLevel();
    }
    public String getDate() {

        return jour.getdate();
    }
    public ArrayList<Jour> getjours() {
        return Controle.jours;
    }

    public void setWaterLevel(ArrayList<Double> waterLevel) {
        jour.WaterLevel = waterLevel ;
    }
    public void setTempVAlueW(ArrayList<Double> tempVAlueW) {
        jour.tempVAlueW = tempVAlueW;
    }
    public void setHumValueA(ArrayList<Double> humValueA) {
        jour.humValueA =  humValueA;
    }
    public void setTempValueA(ArrayList<Double> tempValueA) {
        jour.tempValueA =  tempValueA;
    }
    public void setDate() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        jour.date = currentDate;
    }

}
