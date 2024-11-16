package com.example.authentification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoListAdapter extends BaseAdapter {

    private ArrayList<Jour> jours;
    private LayoutInflater inflater;
    public HistoListAdapter(Context context,ArrayList<Jour> Jours){
        this.jours = Jours;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Object getItem(int i) {
        return jours.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_llst_hisot, null);

            holder.date_text =  (TextView)view.findViewById(R.id.Date);
            holder.tempValueA = (TextView) view.findViewById(R.id.moyTempA);
            holder.humValueA = (TextView) view.findViewById(R.id.moyTempW);
            holder.tempVAlueW = (TextView) view.findViewById(R.id.moyTempW);
            holder.WaterLevel = (TextView) view.findViewById(R.id.moyWaterLevel);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.date_text.setText("12/12/2024");
        holder.WaterLevel.setText(String.valueOf(jours.get(i).getWaterLevel()));
        holder.tempValueA.setText(String.valueOf(jours.get(i).getTempValueA()));
        holder.humValueA.setText(String.valueOf(jours.get(i).getHumValueA()));
        holder.tempVAlueW.setText(String.valueOf(jours.get(i).getTempVAlueW()));

        return view;
    }

    private class ViewHolder{
        TextView tempValueA,humValueA,tempVAlueW,WaterLevel,date_text;
    }
}
