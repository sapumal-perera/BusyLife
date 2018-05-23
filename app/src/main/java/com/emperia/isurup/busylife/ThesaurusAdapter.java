package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ThesaurusAdapter extends ArrayAdapter<Synonym> {
    /***
     * declare & initialize variables
     */
    private TextView categoryView, synonymsView;
    private boolean synonymsOnly;

    /**
     *
     * @param context
     * @param textViewResourceId
     * @param synonyms
     * @param onlySyno
     */
    public ThesaurusAdapter(Context context, int textViewResourceId, List<Synonym> synonyms , boolean onlySyno) {
        super(context, textViewResourceId, synonyms);
        this.synonymsOnly = onlySyno;
    }

    /**
     *
     * @param pos
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        RelativeLayout row = (RelativeLayout) convertView;
        if (null == row) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout) inflater.inflate(R.layout.list_row, null);
        }

        categoryView = (TextView) row.findViewById(R.id.categoryTextView);
        synonymsView = (TextView) row.findViewById(R.id.synonymsTextView);

        if(synonymsOnly){
            synonymsView.setText(getItem(pos).getSynonyms().split(" ")[0]);
        }
        else{
            categoryView.setText(getItem(pos).getCategory());
            synonymsView.setText(getItem(pos).getSynonyms());
        }


        synonymsView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String st = synonymsView.getText().toString();

            }
        });


        return row;


    }

}

