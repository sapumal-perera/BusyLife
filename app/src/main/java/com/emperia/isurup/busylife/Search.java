package com.emperia.isurup.busylife;

import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener{
    /***
     * declare & initialize variables
     */
    private EditText searchET;
    EventDB myDBHandler;
    private EventAdaptor eventAdaptor;
    private ListView listView;
    private ArrayList<Event> listArr = new ArrayList<Event>();
    private List<Event> listMatches;
    private String searchKeywords;

    /**
     * override onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button searchBtn;
        String preKeyword;
        String date;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);
        Intent getWord = getIntent();
        preKeyword = getWord.getStringExtra("keyword");
        searchBtn = (Button) findViewById(R.id.confirmButton);
        searchBtn.setOnClickListener(this);
        searchET = (EditText) findViewById(R.id.searchEditText);
        myDBHandler = new EventDB(this, null, null, 1);
        listArr = myDBHandler.retrieveAllAppointments();
        Date toDay = new Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);



        if(!preKeyword.equals("")) {
            searchET.setText(preKeyword);
        }
        listView = (ListView) findViewById(R.id.searchList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayClickedSearch(eventAdaptor.getItem(position) , view);
            }
        });
        if (!(preKeyword.equals("") || preKeyword.equals(null))) {
            try {
                listMatches = new ArrayList<>();
                searchKeywords = searchET.getText().toString();
                if(month + 1<10){
                    date = ("" + year + "/" +"0"+(month + 1)+ "/" + day).replaceAll(" ","");
                }
                else{ date = ("" + year + "/" +(month + 1)+ "/" + day).replaceAll(" ","");}

                for (Event event : listArr) {
                    String appDate;
                    if(event.getDate().split("/")[1].length()<2){
                        appDate  =  ("" + event.getDate().split("/")[0] + "/" +"0"+ event.getDate().split("/")[1]+ "/" + event.getDate().split("/")[2]).replaceAll(" ","");
                    }else{   appDate =  ("" + event.getDate().split("/")[0] + "/" + event.getDate().split("/")[1]+ "/" + event.getDate().split("/")[2]).replaceAll(" ","");}

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Date date1 = sdf.parse(appDate);
                        Date date2 = sdf.parse(date);
                        if (date1.compareTo(date2) > 0 || date1.compareTo(date2) == 0) {
                            if (event.getTitle().contains(searchKeywords)  || event.getDetails().contains(searchKeywords)) {
                                listMatches.add(event);
                            }
                        }
                }

                eventAdaptor = new EventAdaptor(getBaseContext(), -1, listMatches);
                listView.setAdapter(eventAdaptor);

                if (listMatches.size() == 0) {
                    Toast.makeText(getBaseContext(), "Sorry, No Results Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Sorry, No Results Found", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * Override onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        switch (v.getId()){
            case R.id.confirmButton : {

                try {
                    if (searchET.getText().toString().equals("") || searchET.getText().toString().equals(null)) {

                        searchET.setError("Please input a Keyword");

                    } else {
                        listMatches = new ArrayList<>();
                        searchKeywords = searchET.getText().toString();

                        for (Event event : listArr) {

                            if (event.getTitle().contains(searchKeywords) || event.getDetails().contains(searchKeywords)) {
                                listMatches.add(event);
                            }

                        }


                        eventAdaptor = new EventAdaptor(getBaseContext(), -1, listMatches);
                        listView.setAdapter(eventAdaptor);

                        if(listMatches.size() == 0){
                            Toast.makeText(getBaseContext(),"Sorry, No Results Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Sorry, No Results Found", Toast.LENGTH_SHORT).show();
                }
                searchET.setText("");
                break;
            }
        }
    }

    /**
     * Prepare search result
     * @param event
     * @param v
     */
    public void displayClickedSearch(Event event, View v){
        PopupWindow popupWindow;
        TextView titleView, timeView, detailsView;
        Toast.makeText(getBaseContext(), event.getTitle(), Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = (LayoutInflater) Search.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.search_popup,
                (ViewGroup) findViewById(R.id.searchPopupView));
        titleView = (TextView) layout.findViewById(R.id.searchedTitle) ;
        timeView = (TextView) layout.findViewById(R.id.searchedTime) ;
        detailsView = (TextView) layout.findViewById(R.id.searchedDetails) ;
        popupWindow = new PopupWindow(layout, 1070, 900 ,  true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        titleView.setText(event.getTitle());
        timeView.setText(event.getTime()+" on " + event.getDate());
        detailsView.setText(event.getDetails());

    }

    /**
     * override onBackPressed
     */
    @Override
    public void onBackPressed() {
        Intent goBack = new Intent(this , CalenderView.class);
        startActivity(goBack);
    }
}


