package com.emperia.isurup.busylife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Move extends AppCompatActivity {
    /***
     * declare & initialize variables
     */
    static String title;
    Button moveBtn;
    CalendarView moveCalendarView;
    static String preDate;
    String date = "0";


    EventDB moveDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_mover);

        Intent getData = getIntent();

        preDate = getData.getStringExtra("date");
        title = getData.getStringExtra("title");
        Date toDay = new Date(); // to date
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        date = "" + year + " / " + (month + 1) + " / " + day;
        moveBtn = (Button) findViewById(R.id.movebtn);
        moveCalendarView =(CalendarView) findViewById(R.id.calendarUpdate);

        moveDB = new EventDB(this, null, null, 1);

        moveCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override public void onSelectedDayChange(
                    CalendarView view,
                    int          year,
                    int          month,
                    int          dayOfMonth ) {
                Toast.makeText(getApplicationContext(), ""+year+ " / " + (month+1) + " / " + dayOfMonth+" selected", Toast.LENGTH_SHORT).show();
                date = ""+year+ " / " + (month+1) + " / " + dayOfMonth ;
            }


        });


        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(date.equals("0")){
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT A DATE FIRST", Toast.LENGTH_SHORT).show();
                }else {
                    boolean moveResult = moveDB.MoveAppointment(preDate, title, date);
                    if (moveResult) {
                        Toast.makeText(getApplicationContext(), "Move the " + title + " appointment from: " + preDate + " to: " + date, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " Moving Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent goBack = new Intent(this , CalenderView.class);
        startActivity(goBack);
    }
}
