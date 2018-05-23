package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class EventAdaptor extends ArrayAdapter<Event> {

    public EventAdaptor(Context context, int textViewResourceId, List<Event> events) {
        super(context, textViewResourceId, events);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        TextView titleView, detailsView;
        RelativeLayout row = (RelativeLayout)convertView;
        if(null == row){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.list_row_events, null);
        }

        titleView = (TextView)row.findViewById(R.id.titleTextView);
        detailsView = (TextView)row.findViewById(R.id.detailsTextView);

        titleView.setText(getItem(pos).getTitle());
        detailsView.setText(getItem(pos).getDetails());

        return row;


    }

}