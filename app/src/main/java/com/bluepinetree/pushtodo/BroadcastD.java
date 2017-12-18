package com.bluepinetree.pushtodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import static java.lang.Thread.sleep;

/**
 * Created by Su on 2017-12-18.
 */

public class BroadcastD extends BroadcastReceiver {
//    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    ArrayList<Integer> dDayItem;
    ArrayList<HashMap<String,String>> items;


    public ArrayList<Integer> checkData(ArrayList<HashMap<String,String>> items){
        ArrayList<Integer> Item = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int size = items.size();
//        if(size>0){
            for(int i=0; i<size; i++){
                String checkDate = items.get(i).get("Date");
                String today = year+"/"+month+"/"+day;
                if(checkDate.equals("always") || checkDate.equals(today)){
                    Item.add(i);
                }
            }
//        }
        return Item;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        items = (ArrayList<HashMap<String,String>>)intent.getSerializableExtra("alarmItem");
        dDayItem = checkData(items);
        int size = dDayItem.size();
        if(size>0){
            for(int i=0; i<size; i++){
                HashMap<String,String> alarmItem =items.get(dDayItem.get(i));
                NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, i, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle(alarmItem.get("Subject"))
                        .setContentText(alarmItem.get("Content"))
                        .setTicker("안녕안녕")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL);

                notificationmanager.notify(i, builder.build());
            }
        }
    }
}
