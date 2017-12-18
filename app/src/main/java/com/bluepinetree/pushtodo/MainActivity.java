package com.bluepinetree.pushtodo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity{

    ArrayList<HashMap<String,String>> items;
    ListView itemList;
    HashMap<String,String> alarmList = null;
    ItemAdapter itemAdapter = null;
    int RESULT_ACT = 1;
    int RESULT_ACT2 = 2;
    private static RemoteData remoteData = new RemoteData();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == RESULT_ACT){
           if(resultCode == RESULT_OK){
               alarmList = (HashMap<String,String>)data.getSerializableExtra("Data");
               if(alarmList != null) {
                   items.add(alarmList);
                   remoteData.remoteDataStructure= items;
                   remoteData.commit();
                   itemAdapter.notifyDataSetChanged();
               }
           }
       } else if(requestCode == RESULT_ACT2){
           if(resultCode == RESULT_OK){
               alarmList = (HashMap<String,String>)data.getSerializableExtra("corData");
               int checked = data.getIntExtra("checkedIndex",0);
               items.set(checked,alarmList);
               remoteData.remoteDataStructure = items;
               remoteData.commit();
               itemAdapter.notifyDataSetChanged();
           }
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        itemList = (ListView)findViewById(R.id.itemList);
        FloatingActionButton rmButton = (FloatingActionButton)findViewById(R.id.rmButton);
        FloatingActionButton mdButton = (FloatingActionButton)findViewById(R.id.mdButton);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(remoteData.remoteDataStructure != null)
                    items = remoteData.remoteDataStructure;
                else
                    items = new ArrayList<>();
                itemAdapter = new ItemAdapter(MainActivity.this,R.layout.itemlist_main,items);
                itemList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                itemList.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
                new AlarmHATT(getApplicationContext()).Alarm();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (remoteData.remoteDataStructure != null)
            items = remoteData.remoteDataStructure;
        else
            items = new ArrayList<>();
        itemAdapter = new ItemAdapter(MainActivity.this, R.layout.itemlist_main, items);
        itemList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        itemList.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
//        new AlarmHATT(getApplicationContext()).Alarm();

        final Intent intentA = new Intent(this, AddItemActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                startActivityForResult(intentA,RESULT_ACT);
            }
        });

        rmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final int count, checked ;
                count = itemAdapter.getCount();
                if(count > 0){
                    checked = itemList.getCheckedItemPosition();
                    if(checked > -1 && checked < count){
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(items.get(checked).get("Subject"))
                                .setIcon(android.R.drawable.ic_menu_save)
                                .setMessage("삭제 하시겠습니까?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        items.remove(checked);
                                        itemList.clearChoices();
                                        remoteData.remoteDataStructure = items;
                                        remoteData.commit();
                                        itemAdapter.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(MainActivity.this, "취소했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                }
            }
        });

        mdButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int count, checked;
                if(itemAdapter == null)
                    count = 0;
                else
                    count = itemAdapter.getCount();
                if(count>0){
                    checked = itemList.getCheckedItemPosition();
                    if(checked>-1 && checked < count){
                        HashMap<String,String> corItem = items.get(checked);
                        Intent intentB = new Intent(MainActivity.this,CorrectItemActivity.class);
                        intentB.putExtra("corItem",corItem);
                        intentB.putExtra("Index",checked);
                        startActivityForResult(intentB,RESULT_ACT2);
                    }
                }
            }
        });

    }

    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);
            intent.putExtra("alarmItem",items);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 2, 10, 0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}
