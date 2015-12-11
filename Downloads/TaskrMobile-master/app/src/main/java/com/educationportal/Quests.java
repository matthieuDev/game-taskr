package com.educationportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Quests extends AppCompatActivity {
    ListView l1;

    // set up firebase reference
    Firebase ref = new Firebase("https://taskrweb.firebaseio.com/users/b230f655-1751-49d2-a74d-f0527d11a837/quests");

    ArrayList<String> quests = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.quests);

        TextView t =new TextView(this);

        t=(TextView)findViewById(R.id.title);
        t.setText("My Quests");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue().toString();
                    quests.add(name);
                }
                l1=(ListView)findViewById(R.id.list);
                l1.setAdapter(new dataListAdapter(quests));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    class dataListAdapter extends BaseAdapter {
        ArrayList<String> tasks;

        dataListAdapter() {
            tasks = null;
        }

        public dataListAdapter(ArrayList<String> t) {
            quests = t;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return quests.size();
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
            title.setText(quests.get(position));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuest(quests.get(POS));
                }
            });

            return (row);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openQuest(String name) {
        System.out.println(name);
        Intent intent = new Intent(this, Tasks.class);
        intent.putExtra("id", name.replaceAll("\\s+", ""));


        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}