package com.educationportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by johnearle on 9/27/15.
 */
public class PictureTaskActivity extends AppCompatActivity {
    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_task_layout);

        Intent intent = getIntent();

        String question = "What type of molecule is shown?";

        TextView t =new TextView(this);

        t=(TextView)findViewById(R.id.title);
        t.setText(question);

        String[] options = {"Hydrogen sulfide", "Water", "Phosphine", "Ferrous chloride"};

        l1=(ListView)findViewById(R.id.list);
        l1.setAdapter(new dataListAdapter(new ArrayList<String>(Arrays.asList(options))));
    }

    class dataListAdapter extends BaseAdapter {
        ArrayList<String> options;

        dataListAdapter() {
            options = null;
        }

        public dataListAdapter(ArrayList<String> t) {
            options = t;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return options.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int POS = position;
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.quest_row_layout, parent, false);
            TextView title;
            title = (TextView) row.findViewById(R.id.name);
            title.setText(options.get(position));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            return (row);
        }
    }
}
