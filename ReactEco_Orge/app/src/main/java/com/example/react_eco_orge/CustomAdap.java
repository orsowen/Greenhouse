package com.example.react_eco_orge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdap extends BaseAdapter {

    Object temp[];
    Object humd[];
    Object days[];
    Object waterlevel[];
    Context contx;
    LayoutInflater inflater;
    public CustomAdap(Context ctx, Object[] humd, Object[] temp, Object[] days, Object[] water_level)
    {
        this.temp = temp;
        this.humd = humd;
        this.days = days;
        this.waterlevel= water_level;
        this.contx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int Index_of_Row, View convertView, ViewGroup viewGroup) {
        convertView= inflater.inflate(R.layout.activity_custom_history,null);
        TextView textView_Date = (TextView) convertView.findViewById(R.id.Date);
        TextView textView_humidite = (TextView) convertView.findViewById(R.id.Humidite);
        TextView textView_waterLVL = (TextView) convertView.findViewById(R.id.Water_Level);
        TextView textView_temperature = (TextView) convertView.findViewById(R.id.temperature);
        textView_Date.setText(String.valueOf(days[Index_of_Row]));
        textView_waterLVL.setText(String.valueOf(waterlevel[Index_of_Row]));
        textView_humidite.setText(String.valueOf(humd[Index_of_Row]));
        textView_temperature.setText(String.valueOf(temp[Index_of_Row]));
        return convertView;
    }
}
