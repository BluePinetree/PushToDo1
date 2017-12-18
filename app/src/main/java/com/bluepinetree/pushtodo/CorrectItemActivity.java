package com.bluepinetree.pushtodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Su on 2017-12-02.
 */

public class CorrectItemActivity extends AppCompatActivity{
    EditText subItem;
    EditText conItem;
    Button addBtn;
    Button dateBtn;
    TextView dateView;
    CheckBox checkbox1;
    HashMap<String,String> corItem;
    int checked;
    GregorianCalendar calendar = new GregorianCalendar();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day= calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additem_main);
        subItem = (EditText)findViewById(R.id.item1);
        conItem = (EditText)findViewById(R.id.item2);
        addBtn = (Button)findViewById(R.id.addButton);
        dateBtn = (Button)findViewById(R.id.dateButton);
        dateView = (TextView)findViewById(R.id.dateView);
        TextView titleText = (TextView)findViewById(R.id.titleText);
        titleText.setText("Correct Text");
        addBtn.setText("Set");

        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox1.isChecked()){
                    dateBtn.setVisibility(View.GONE);
                    dateView.setText("always");
                    dateView.setTextSize(30);
                } else{
                    String dateString = year + "/" + (month+1) + "/" + day;
                    dateBtn.setVisibility(View.VISIBLE);
                    dateView.setText(dateString);
                    dateView.setTextSize(15);
                }
            }
        });

        checked = getIntent().getIntExtra("Index",0);
        corItem = (HashMap<String,String>)getIntent().getSerializableExtra("corItem");
        subItem.setText(corItem.get("Subject"));
        conItem.setText(corItem.get("Content"));
        dateView.setText(corItem.get("Date"));


    }
    public void OnClick(View v){
        String date = corItem.get("Date");
        if(!date.equals("always")) {
            StringTokenizer aa = new StringTokenizer(date, "/");
            int dYear = Integer.parseInt(aa.nextToken());
            int dMonth = Integer.parseInt(aa.nextToken());
            int dDay = Integer.parseInt(aa.nextToken());
            new DatePickerDialog(this, dateSetListener, dYear, dMonth - 1, dDay).show();
        } else{
            new DatePickerDialog(this, dateSetListener, year, month , day).show();
        }
    }

    public void ClickAdd(View v){
        HashMap<String,String> mapA = new HashMap<>();
        String subject = subItem.getText().toString();
        String content = conItem.getText().toString();
        String date = dateView.getText().toString();

        if(subject==null || subject.equals("")){
            Toast t = Toast.makeText(this,"subject를 입력하세요",Toast.LENGTH_SHORT);
            t.show();
        } else if(content==null||content.equals("")){
            Toast t = Toast.makeText(this,"content를 입력하세요",Toast.LENGTH_SHORT);
            t.show();
        } else{

            mapA.put("Subject", subject);
            mapA.put("Content", content);
            mapA.put("Date", date);

            Intent intent = new Intent();
            intent.putExtra("corData", mapA);
            intent.putExtra("checkedIndex",checked);
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {



        @Override

        public void onDateSet(DatePicker view, int year, int monthOfYear,

                              int dayOfMonth) {

            // TODO Auto-generated method stub

            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            dateView.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);

        }

    };
}
