package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EventDB extends SQLiteOpenHelper {
    /***
     * declare & initialize variables
     */

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "events.db";

    private static final String TABLE_MY_EVENTS = "myEvents";

    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_DATE = "eventDate";

    private static final String COLUMN_TIME = "eventTime";

    private static final String COLUMN_TITLE = "eventTitle";

    private static final String COLUMN_DETAILS = "eventDetails";

    private static final String COLUMN_CAL_TIME = "eventCalTime";

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public EventDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_MY_EVENTS + "(" +

                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " DATETIME, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_CAL_TIME + " DATETIME " +
                ");";
        db.execSQL(query);

    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MY_EVENTS);
        onCreate(db);
    }

    /**
     * Check title availability
     * @param appointment
     * @return
     */
    public Boolean checkTitle(Event appointment) {
        SQLiteDatabase mydb = getWritableDatabase();
        String query = " SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " +
                COLUMN_TITLE + "=\'" + appointment.getTitle() + "\'" + " AND " +
                COLUMN_DATE + "=\'" + appointment.getDate() + "\'";

        Cursor pointer = mydb.rawQuery(query, null);

        if (pointer == null || !pointer.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * add appointment
     * @param appointment
     * @return
     */
    public String createEvent(Event appointment) {

        ContentValues conValues = new ContentValues();
        conValues.put(COLUMN_DATE, appointment.getDate());
        conValues.put(COLUMN_TIME, appointment.getTime());
        conValues.put(COLUMN_TITLE, appointment.getTitle());
        conValues.put(COLUMN_DETAILS, appointment.getDetails());
        conValues.put(COLUMN_CAL_TIME, appointment.getCalTime());


        SQLiteDatabase mydb = getWritableDatabase();
        mydb.insert(TABLE_MY_EVENTS, null, conValues);
        mydb.close();

        return appointment.getTitle() + " added";

    }

    /**
     * delete all apps
     * @param date
     */
    public void deleteAll(String date) {

        SQLiteDatabase mydb = getWritableDatabase();
        mydb.execSQL("DELETE FROM " + TABLE_MY_EVENTS + " WHERE " + COLUMN_DATE + "=\"" + date + "\";");
        mydb.close();


    }

    /**
     *  delete selected apps
     * @param date
     * @param userSelected
     * @param titleOnly
     * @return
     */
    public String deleteSelect(String date, int userSelected, boolean titleOnly) {

        SQLiteDatabase mydb = getWritableDatabase();
        int index = 1;
        String title = "";

        String query = "SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " + COLUMN_DATE + " = \"" + date + "\" ORDER BY " + COLUMN_CAL_TIME + " ASC";

        Cursor pointer = mydb.rawQuery(query, null);
        pointer.moveToFirst();

        while (!pointer.isAfterLast()) {

            String tempTitle = pointer.getString(pointer.getColumnIndex("eventTitle"));
            if (index == userSelected) {
                if (titleOnly) {
                    title = tempTitle;
                } else {
                    mydb.execSQL("DELETE FROM " + TABLE_MY_EVENTS + " WHERE " + COLUMN_TITLE + "=\"" + tempTitle + "\";");
                }
                break;
            } else {
                index++;
            }
            pointer.moveToNext();
        }

        mydb.close();
        if (pointer.isAfterLast()) {
            return "NoN";
        } else {
            return title;
        }
    }

    /**
     * show apps
     * @param date
     * @return
     */
    public String showAppointment(String date) {
        String viewString = "";
        int index = 0;
        SQLiteDatabase mydb = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " +
                COLUMN_DATE + " = \"" + date + "\" ORDER BY " + COLUMN_CAL_TIME + " ASC";
        Cursor pointer = mydb.rawQuery(query, null);
        pointer.moveToFirst();

        while (!pointer.isAfterLast()) {
            index++;
            String tempTime = pointer.getString(pointer.getColumnIndex("eventTime"));
            String tempTitle = pointer.getString(pointer.getColumnIndex("eventTitle"));
            viewString += index + ".  " + tempTime + "  " + tempTitle;
            viewString += "\n";
            pointer.moveToNext();
        }
        mydb.close();
        return viewString;

    }

    /**
     * update apps
     * @param date
     * @param preTitle
     * @param title
     * @param time
     * @param discription
     * @param mathTime
     * @return
     */
    public boolean updateAppointment(String date, String preTitle, String title, String time, String discription, double mathTime) {

        SQLiteDatabase mydb = getWritableDatabase();
        String query = " SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " +
                COLUMN_TITLE + "=\'" + preTitle + "\'" + " AND " +
                COLUMN_DATE + "=\'" + date + "\'";
        Cursor pointer = mydb.rawQuery(query, null);

        if (pointer == null || !pointer.moveToFirst()) {
            return false;
        } else {

            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_TIME, time);
            values.put(COLUMN_DETAILS, discription);
            values.put(COLUMN_CAL_TIME, mathTime);

            mydb.update(TABLE_MY_EVENTS, values, COLUMN_TITLE + "=\'" + preTitle + "\'" + " AND " +
                    COLUMN_DATE + "=\'" + date + "\'", null);

            mydb.close();
            pointer.close();

            return true;
        }


    }

    /**
     * move apps
     * @param preDate
     * @param title
     * @param newDate
     * @return
     */
    public boolean MoveAppointment(String preDate, String title, String newDate) {

        SQLiteDatabase mydb = getWritableDatabase();
        String query = " SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " +
                COLUMN_TITLE + "=\'" + title + "\'" + " AND " +
                COLUMN_DATE + "=\'" + preDate + "\'";
        Cursor pointer = mydb.rawQuery(query, null);
        if (pointer == null || !pointer.moveToFirst()) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, newDate);
            mydb.update(TABLE_MY_EVENTS, values, COLUMN_TITLE + "=\'" + title + "\'" + " AND " +
                    COLUMN_DATE + "=\'" + preDate + "\'", null);
            mydb.close();
            pointer.close();
            return true;
        }
    }

    /**
     * get all apps foe search apps
     * @return
     */
    public ArrayList<Event> retrieveAllAppointments() {

        ArrayList<Event> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MY_EVENTS + ";";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndex("eventTitle")) != null) {


                Event event = new Event(cursor.getString(cursor.getColumnIndex("eventTitle")),
                        cursor.getString(cursor.getColumnIndex("eventDetails")),
                        cursor.getString(cursor.getColumnIndex("eventDate")),
                        cursor.getString(cursor.getColumnIndex("eventTime")),
                        cursor.getDouble(cursor.getColumnIndex("eventCalTime")));

                list.add(event);
            }
            cursor.moveToNext();
        }
        db.close();
        return list;
    }

    /**
     * get app title for update view
     * @param date
     * @param userSelected
     * @param titleOnly
     * @return
     */
    public String[] getSelect(String date, int userSelected, boolean titleOnly) {
        String[] dbArr = new String[2];
        SQLiteDatabase mydb = getWritableDatabase();
        int index = 1;
        String query = "SELECT * FROM " + TABLE_MY_EVENTS + " WHERE " +
                COLUMN_DATE + " = \"" + date + "\" ORDER BY " + COLUMN_CAL_TIME + " ASC";

        Cursor pointer = mydb.rawQuery(query, null);
        pointer.moveToFirst();

        while (!pointer.isAfterLast()) {
            String tempTitle = pointer.getString(pointer.getColumnIndex("eventTitle"));
            String tempDesc = pointer.getString(pointer.getColumnIndex("eventDetails"));
            if (index == userSelected) {
                    dbArr[0] = tempTitle;
                    dbArr[1] = tempDesc;
                break;
            } else {
                index++;
                dbArr[0] = "Please add a title..";
                dbArr[1] = "Please add a details";
            }
            pointer.moveToNext();
        }
        mydb.close();
            return  dbArr;



    }


}
