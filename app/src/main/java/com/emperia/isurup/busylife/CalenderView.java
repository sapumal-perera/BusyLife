package com.emperia.isurup.busylife;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class CalenderView extends AppCompatActivity implements android.view.View.OnClickListener {
    /***
     * declare & initialize variables
     */
    static int calDay;
    static int calMonth;
    static int calYear;
    static String date = "0";
    Button createNew, search;
    Button viewEdit;
    Button move;
    Button delete;
    Button deleteAll;
    Button delSelected;
    CalendarView datePicker;
    EditText searchWord;
    EventDB eveDB;

    /**
     * Override oncreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Date toDay = new Date(); // to date
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        date = "" + year + " / " + (month + 1) + " / " + day;

        createNew = (Button) findViewById(R.id.create);
        viewEdit = (Button) findViewById(R.id.viewedit);
        move = (Button) findViewById(R.id.move);
        delete = (Button) findViewById(R.id.delete);
        datePicker = (CalendarView) findViewById(R.id.pickdate);
        deleteAll = (Button) findViewById(R.id.delAll);
        delSelected = (Button) findViewById(R.id.delSel);
        search = (Button) findViewById(R.id.searchBtn);
        searchWord = (EditText) findViewById(R.id.searchET);


        createNew.setOnClickListener(this);
        viewEdit.setOnClickListener(this);
        move.setOnClickListener(this);
        delete.setOnClickListener(this);
        search.setOnClickListener(this);


        eveDB = new EventDB(this, null, null, 1);
        // long millis = System.currentTimeMillis();
        // datePicker.setDate(millis);

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(
                    CalendarView view,
                    int year,
                    int month,
                    int dayOfMonth) {
                calDay = dayOfMonth;
                calMonth = month;
                calYear = year;
                Toast.makeText(getApplicationContext(), "" + year + " / " + (month + 1) + " / " + dayOfMonth + " selected", Toast.LENGTH_SHORT).show();
                date = "" + year + " / " + (month + 1) + " / " + dayOfMonth;
            }


        });

    }

    /**
     * set onclick listener action
     *
     * @param v
     */
    public void onClick(android.view.View v) {

        switch (v.getId()) {

            case R.id.create:
                if (date.equals("0")) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT A DATE ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent createAppoint = new Intent(this, Create.class);
                    createAppoint.putExtra("year", calYear);
                    createAppoint.putExtra("month", calMonth);
                    createAppoint.putExtra("day", calDay);
                    createAppoint.putExtra("date", date);
                    createAppoint.putExtra("title", "");


                    startActivity(createAppoint);
                }

                break;
            case R.id.move:
                if (date.equals("0")) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT A DATE", Toast.LENGTH_SHORT).show();
                } else {
                    Intent move = new Intent(this, View.class);
                    move.putExtra("date", date);
                    move.putExtra("option", "move");
                    startActivity(move);
                }

                break;

            case R.id.viewedit:
                if (date.equals("0")) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT A DATE", Toast.LENGTH_SHORT).show();
                } else {
                    Intent viewEdit = new Intent(this, View.class);
                    viewEdit.putExtra("date", date);
                    viewEdit.putExtra("option", "view");
                    startActivity(viewEdit);
                }


                break;

            case R.id.delete:

                if (date.equals("0")) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT A DATE FIRST", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder mbuild = new AlertDialog.Builder(CalenderView.this);
                    android.view.View mView = getLayoutInflater().inflate(R.layout.delete_events, null);
                    final Button delAll = (Button) mView.findViewById(R.id.delAll);
                    final Button delSel = (Button) mView.findViewById(R.id.delSel);
                    mbuild.setView(mView);
                    final AlertDialog dialog = mbuild.create();
                    delAll.setOnClickListener(new android.view.View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View view) {
                            Toast.makeText(getApplicationContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                            eveDB.deleteAll(date);
                            dialog.dismiss();
                        }
                    });


                    delSel.setOnClickListener(new android.view.View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View view) {

                            Toast.makeText(getApplicationContext(), "Select an event to delete", Toast.LENGTH_SHORT).show();
                            Intent deleteOne = new Intent(CalenderView.this, View.class);
                            deleteOne.putExtra("date", date);
                            deleteOne.putExtra("option", "delete");
                            startActivity(deleteOne);
                        }
                    });

                    dialog.show();
                }

                break;


            case R.id.searchBtn:

                Intent searchView = new Intent(CalenderView.this, Search.class);
                searchView.putExtra("keyword", searchWord.getText().toString());
                startActivity(searchView);

                break;

        }

    }


}
