package com.emperia.isurup.busylife;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Create extends AppCompatActivity {
    /***
     * declare & initialize variables
     */
    static double mathTime;
    static int hour;
    static int minute;

    Button thesaurusAllBtn, thesaurusSynoBtn;

    ThesaurusAdapter thesaurusAdapter;
    ListView synonymsList;
    PopupWindow popupWindow;

    private String enteredWord;
    public static final String THESAURUS_KEY = "udQK8gBDZkLHWmCt4LHD";
    private TimePicker time;
    private EditText details, thesaurusWord;
    private EventDB appDB;
    private String selectTime;
    private EditText title;
    private String selectDate;
    private String preTitle;
    /**
     * Override onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button saveBtn;
        String preDesc;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_event_creator);
        Intent getDate = getIntent();
        selectDate = getDate.getStringExtra("date");
        preTitle = getDate.getStringExtra("title");
        preDesc = getDate.getStringExtra("desc");
        title = (EditText) findViewById(R.id.titleedit);
        time = (TimePicker) findViewById(R.id.timeedit);
        details = (EditText) findViewById(R.id.editdetail);
        saveBtn = (Button) findViewById(R.id.savebtn);
        thesaurusWord =  (EditText) findViewById(R.id.thesaurusWord);
        thesaurusAllBtn = (Button) findViewById(R.id.thesaurusbtn);
        thesaurusSynoBtn = (Button) findViewById(R.id.thesaurusbtn2);
        appDB = new EventDB(this, null, null, 1);
        title.setText(preTitle);
        details.setText(preDesc);
        boolean fromthesuBtn1;
        boolean fromthesuBtn2;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (preTitle.equals("")){
                    hour = time.getCurrentHour();
                    minute = time.getCurrentMinute();
                    mathTime = (hour * 60) + minute;
                    selectTime = "" + hour + ":" + minute;
                    Event newAppointment = new Event(title.getText().toString(), details.getText().toString(), selectDate, selectTime, mathTime);

                    if (appDB.checkTitle(newAppointment)) {
                        String addedTittle = appDB.createEvent(newAppointment);
                        Toast.makeText(getApplicationContext(), addedTittle, Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(Create.this, CalenderView.class);
                        startActivity(home);

                    } else {
                        Toast.makeText(getApplicationContext(), title.getText().toString() + " That Title is Already Exists, Please enter another Title", Toast.LENGTH_LONG).show();
                    }
                }else{
                    hour = time.getCurrentHour();
                    minute = time.getCurrentMinute();
                    mathTime = (hour * 60) + minute;
                    selectTime = "" + hour + ":" + minute;
                    boolean updateResult = appDB.updateAppointment(selectDate,preTitle ,title.getText().toString() , selectTime, details.getText().toString(),mathTime);
                    Toast.makeText(getApplicationContext(), selectDate, Toast.LENGTH_LONG).show();
                    if(updateResult){
                        Toast.makeText(getApplicationContext(), "Update the appointment on " + preTitle + "  SUCCESSFUL", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), " something went WRONG", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        thesaurusAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                enteredWord = thesaurusWord.getText().toString();
                if(enteredWord.split(" ").length>1){
                    Toast.makeText(getBaseContext()," Please enter a single word",Toast.LENGTH_SHORT).show();
                } else{
                if(enteredWord.equals(null) || enteredWord.equals("")){
                    Toast.makeText(getBaseContext(),"Please enter and select a valid word",Toast.LENGTH_SHORT).show();
                    thesaurusWord.setError("Please enter a word and press the button");
                } else{
                    Toast.makeText(getBaseContext(),"You selected the word \"" + enteredWord + "\" Please Wait ",Toast.LENGTH_SHORT).show();
                    synonymsPopUp(v,false);
                } }
                thesaurusWord.setText("");

            }
        });

        thesaurusSynoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                int startSelection= details.getSelectionStart();
                int endSelection= details.getSelectionEnd();
                String selectedText = details.getText().toString().substring(startSelection, endSelection);
                enteredWord = selectedText;
                if(enteredWord.equals(null) || enteredWord.equals("")){
                    Toast.makeText(getBaseContext(),"Please enter and select a valid word",Toast.LENGTH_SHORT).show();
                    details.setError("Please select a word and press the button");
                } else{
                    Toast.makeText(getBaseContext(),"You selected the word \"" + selectedText + "\" Please Wait ",Toast.LENGTH_SHORT).show();
                    synonymsPopUp(v,true);
                }

            }
        });

    }

    /**
     * Check network availability
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class SitesDownloadTask extends AsyncTask<Void, Void, Void> {
        boolean onlysyno;
        String lang = "en_US";
        SitesDownloadTask(boolean syno){
            this.onlysyno =syno;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                DownloadFromWeb("http://thesaurus.altervista.org/thesaurus/v1?word=" + enteredWord +
                                "&language="+ lang +"&%20key="+ THESAURUS_KEY +"&output=xml",
                        openFileOutput("synonyms.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            thesaurusAdapter = new ThesaurusAdapter(Create.this, -1, XMLParser.getSynonymsFromFile(Create.this),onlysyno);
            synonymsList.setAdapter(thesaurusAdapter);

        }
    }

    /**
     * Download response from thesaurus web
     * @param URL
     * @param fos
     */
    public static void DownloadFromWeb(String URL, FileOutputStream fos) {
        try {

            java.net.URL url = new URL(URL);
            URLConnection connection = url.openConnection();
            InputStream iStream = connection.getInputStream();
            BufferedInputStream bIStream = new BufferedInputStream(iStream);
            BufferedOutputStream bOStream = new BufferedOutputStream(fos);
            byte data[] = new byte[1024];
            int count;
            while ((count = bIStream.read(data)) != -1) {
                bOStream.write(data, 0, count);
            }
            bOStream.flush();
            bOStream.close();
        } catch (IOException e) {
        }
    }

    /**
     * Create synonyms popup
     * @param v
     * @param synonyms
     */
    public void synonymsPopUp(View v, boolean synonyms) {
      boolean synoOnly = synonyms;
        try {

            LayoutInflater inflater = (LayoutInflater) Create.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.list_view_thesaurus,
                    (ViewGroup) findViewById(R.id.popUpList));
            popupWindow = new PopupWindow(layout, 1000, 1500 ,  true);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            synonymsList = (ListView) layout.findViewById(R.id.synonymList);
            if(isNetworkAvailable()){
                SitesDownloadTask download = new SitesDownloadTask(synoOnly);
                download.execute();
            }else{
                Toast.makeText(getBaseContext() , "Problem with Internet Connection. Please check an retry." , Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * override onBackPress
     */
    @Override
    public void onBackPressed() {
        Intent goBack = new Intent(this , CalenderView.class);
        startActivity(goBack);
    }

    public void setSynonym(String Synonym){

    }

}
