package com.bluepinetree.pushtodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Su on 2017-12-02.
 */

public class ItemAdapter extends ArrayAdapter{
    Activity acti = null;
    ArrayList<HashMap<String,String>> alarmList = null;

    public ItemAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String,String>> alarmList) {
        super(context, resource);
        this.acti = (Activity)context;
        this.alarmList = alarmList;
    }

    @Override
    public int getCount() {
        if(alarmList == null)
            return 0;
        else
            return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = acti.getLayoutInflater().inflate(R.layout.itemlist_main, viewGroup, false);
        }
//        LinearLayout listItem = (LinearLayout)view.findViewById(R.id.list_item);
//        listItem.setBackgroundColor(Color.BLUE);
        TextView tvA = (TextView)view.findViewById(R.id.dataItem1);
        tvA.setText(alarmList.get(i).get("Subject"));
        TextView tvB = (TextView)view.findViewById(R.id.dataItem2);
        tvB.setText(alarmList.get(i).get("Content"));
        TextView tvC = (TextView)view.findViewById(R.id.dataItem3);
        tvC.setText(alarmList.get(i).get("Date"));

        return view;
    }
}
