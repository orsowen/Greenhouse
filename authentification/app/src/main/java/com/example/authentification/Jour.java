package com.example.authentification;

import java.util.ArrayList;

public class Jour {

    public String date ="";
    public ArrayList<Double> tempValueA ,humValueA,tempVAlueW,WaterLevel;

    public Jour(String date) {
        this.date = date;
        this.tempValueA = new ArrayList<Double>();
        this.humValueA = new ArrayList<Double>();
        this.tempVAlueW = new ArrayList<Double>();
        this.WaterLevel = new ArrayList<Double>();

        this.tempValueA.add(25.0);
        this.tempValueA.add(5.5);
        this.tempValueA.add(6.5);
        this.tempValueA.add(48.0);
        this.tempValueA.add(1.0);

        this.humValueA.add(25.0);
        this.humValueA.add(5.5);
        this.humValueA.add(6.5);
        this.humValueA.add(48.0);
        this.humValueA.add(1.5);

        this.tempVAlueW.add(25.0);
        this.tempVAlueW.add(5.5);
        this.tempVAlueW.add(6.5);
        this.tempVAlueW.add(48.0);
        this.tempVAlueW.add(1.5);

        this.WaterLevel.add(25.0);
        this.WaterLevel.add(5.5);
        this.WaterLevel.add(6.5);
        this.WaterLevel.add(48.0);
        this.WaterLevel.add(1.5);
    }

    public Double getTempValueA() {
        Double moy = 0.0;
        for (Double val : this.tempValueA) {
            moy += val;
        }
        return moy / this.tempValueA.size();
    }

    public Double getHumValueA() {
        Double moy = 0.0;
        for (Double val : this.humValueA) {
            moy += val;
        }
        return moy / this.humValueA.size();
    }

    public Double getTempVAlueW() {
        Double moy = 0.0;
        for (Double val : this.tempVAlueW) {
            moy += val;
        }
        return moy / this.tempVAlueW.size();
    }

    public Double getWaterLevel() {
        Double moy = 0.0;
        for (Double val : this.WaterLevel) {
            moy += val;
        }
        return moy / this.WaterLevel.size();
    }
    public String getdate(){
        return this.date;
    }

}
