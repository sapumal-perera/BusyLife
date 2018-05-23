package com.emperia.isurup.busylife;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class View extends AppCompatActivity {
    /***
     * declare & initialize variables
     */
    EventDB handleDB;
    TextView viewHead, inform, viewTopic, viewAll;
    EditText selectEvent;
    Button btnSelect;
    private String date, header;
    private  static int selection = 404;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_events);


        Intent getData = getIntent();
        date = getData.getStringExtra("date");
        header = getData.getStringExtra("option");
        handleDB = new EventDB(this, null, null, 1);
        viewAll = (TextView) findViewById(R.id.viewAppointment);
        viewHead = (TextView) findViewById(R.id.viewHead);
        inform = (TextView) findViewById(R.id.informText);
        btnSelect = (Button) findViewById(R.id.btnSelectAppointment);
        selectEvent = (EditText) findViewById(R.id.selectAppoinment);
        viewTopic = (TextView) findViewById(R.id.viewTopic);


        showAppointment();
        viewHead.setText("\nAppointments on " + date);
        switch (header) {
            case "view":
                viewTopic.setText("\n      View/Edit Event");
                viewEdit();
                break;
            case "delete":
                viewTopic.setText("\n      Delete Event");
                deleteSelect();
                break;
            case "move":
                viewTopic.setText("\n      Move Event");
                moveSelect();
                break;

        }

    }

    /**
     *  select Action
     * @param view
     */
    public void selectAction(android.view.View view) {
        String getID = selectEvent.getText().toString();
        if (getID.equals("") || getID == null) {
            Toast.makeText(getApplicationContext(), R.string.plsSelectApp, Toast.LENGTH_LONG).show();
        } else {
            selection = Integer.parseInt(getID);
            switch (header) {
                case "view":
                    if (selection == 404) {
                        Toast.makeText(getApplicationContext(), R.string.plsSelectApp, Toast.LENGTH_LONG).show();
                    } else {

                        String preData[] = handleDB.getSelect(date, selection, true);
                        Intent viewEditSelect = new Intent(this, Create.class);
                        viewEditSelect.putExtra("title", preData[0]);
                        viewEditSelect.putExtra("desc", preData[1]);
                        viewEditSelect.putExtra("date", date);
                        startActivity(viewEditSelect);

                    }

                    break;
                case "delete":

                    final AlertDialog.Builder mbuild = new AlertDialog.Builder(View.this);
                    final android.view.View mView = getLayoutInflater().inflate(R.layout.confirm_delete, null);
                    Button confirmNo = (Button) mView.findViewById(R.id.confirmNo);
                    Button confirmYes = (Button) mView.findViewById(R.id.confirmYes);
                    TextView confirmMsg = (TextView) mView.findViewById(R.id.confirmDeleteText);
                    String confirmDel = handleDB.deleteSelect(date, selection, true);
                    if (confirmDel.equalsIgnoreCase("NoN")) {
                        Toast.makeText(getApplicationContext(), "INVALID APPOINTMENT ID ", Toast.LENGTH_LONG).show();
                    } else {
                        confirmMsg.setText("Would you like to delete event \" " + confirmDel + " \"");
                        mbuild.setView(mView);
                        final AlertDialog dialog = mbuild.create();
                        confirmYes.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View view) {
                                handleDB.deleteSelect(date, selection, false);
                                showAppointment();
                                selectEvent.setText("");
                                Intent cont = new Intent(View.this, View.class);
                                cont.putExtra("date", date);
                                cont.putExtra("option", header);
                                dialog.dismiss();
                            }
                        });


                        confirmNo.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View view) {
                                showAppointment();
                                selectEvent.setText("");
                                Intent cont = new Intent(View.this, View.class);
                                cont.putExtra("date", date);
                                cont.putExtra("option", header);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }


                    break;
                case "move":
                    if (selection == 404) {
                        Toast.makeText(getApplicationContext(), R.string.plsSelectApp, Toast.LENGTH_LONG).show();
                    } else {
                        String title = handleDB.deleteSelect(date, selection, true);
                        if (title.equalsIgnoreCase("NoN")) {
                            Toast.makeText(getApplicationContext(), "INVALID APPOINTMENT ID ", Toast.LENGTH_LONG).show();
                        } else {
                            Intent moveAppointment = new Intent(this, Move.class);
                            moveAppointment.putExtra("title", title);
                            moveAppointment.putExtra("date", date);
                            Toast.makeText(getApplicationContext(), "date: " + date + " tittle: " + title, Toast.LENGTH_LONG).show();
                            startActivity(moveAppointment);
                        }
                    }

                    break;
            }
        }

    }

    /**
     *  show apps
     */
    public void showAppointment() {
        String text = (handleDB.showAppointment(date));
        viewAll.setText(text);
    }

    /**
     * when select show apps
     */
    public void viewEdit() {
        inform.setText(R.string.enterIdtoView);
        btnSelect.setText("View/Edit");
    }
    /**
     * when select Delete apps
     */
    public void deleteSelect() {
        inform.setText(R.string.enterIdtoDel);
        btnSelect.setText("Delete");
    }
    /**
     * when select Move apps
     */
    public void moveSelect() {
        inform.setText(R.string.enterIdtoMove);
        btnSelect.setText("Move");
    }

    /**
     * Override onBackPressed
     */
    @Override
    public void onBackPressed() {
        Intent goBack = new Intent(this, CalenderView.class);
        startActivity(goBack);
    }
}
