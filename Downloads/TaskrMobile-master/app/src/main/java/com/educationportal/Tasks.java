package com.educationportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
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

public class Tasks extends AppCompatActivity {
    ListView l1;
    ListView l2;

    Firebase ref = null;

    ArrayList<MultipleChoiceTask> mcTasks = new ArrayList<MultipleChoiceTask>();
    ArrayList<MultipleChoiceTask> pictureTasks = new ArrayList<MultipleChoiceTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.tasks);

        TextView t =new TextView(this);

        t=(TextView)findViewById(R.id.title);
        t.setText("Tasks");

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");

        // set up firebase reference
        Firebase ref = new Firebase("https://taskrweb.firebaseio.com/users/b230f655-1751-49d2-a74d-f0527d11a837/quests/" + id + "/tasks");

        System.out.println(ref);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot questSnap : snapshot.getChildren()) {
                    System.out.println(questSnap);
                    String question = questSnap.child("question").getValue().toString();
                    String option1 = questSnap.child("option1").getValue().toString();
                    String option2 = questSnap.child("option2").getValue().toString();
                    String option3 = questSnap.child("option3").getValue().toString();
                    String option4 = questSnap.child("option4").getValue().toString();

                    String[] options = new String[4];
                    options[0] = option1;
                    options[1] = option2;
                    options[2] = option3;
                    options[3] = option4;

                    int ans = 3;

                    MultipleChoiceTask task = new MultipleChoiceTask(question, options, ans);

                    mcTasks.add(task);
                }
                l1 = (ListView) findViewById(R.id.list);
                l1.setAdapter(new dataListAdapter(mcTasks));


                String question = "What molecule is shown?";
                String[] options = new String[4];
                options[0] = "Hydrogen sulfide";
                options[1] = "Water";
                options[2] = "Phosphine";
                options[3] = "Ferrous chloride";

                int ans = 3;

                MultipleChoiceTask task = new MultipleChoiceTask(question, options, ans);

                pictureTasks.add(task);

                l2 = (ListView) findViewById(R.id.list2);
                l2.setAdapter(new pictureTaskListAdapter(pictureTasks));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    class dataListAdapter extends BaseAdapter {
        ArrayList<MultipleChoiceTask> tasks;

        dataListAdapter() {
            tasks = null;
        }

        public dataListAdapter(ArrayList<MultipleChoiceTask> t) {
            tasks = t;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tasks.size();
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
            row = inflater.inflate(R.layout.task_row_layout, parent, false);
            TextView title;
            title = (TextView) row.findViewById(R.id.taskTitle);
            title.setText(tasks.get(position).question);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToTask(v, tasks.get(POS));
                }
            });

            return (row);
        }
    }


    class pictureTaskListAdapter extends BaseAdapter {
        ArrayList<MultipleChoiceTask> tasks;

        pictureTaskListAdapter() {
            tasks = null;
        }

        public pictureTaskListAdapter(ArrayList<MultipleChoiceTask> t) {
            tasks = t;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tasks.size();
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
            row = inflater.inflate(R.layout.picture_task_row_layout, parent, false);
            TextView title;
            title = (TextView) row.findViewById(R.id.taskTitle);
            title.setText(tasks.get(position).question);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPictureTask(v, tasks.get(POS));
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

    public void goToTask(View view, MultipleChoiceTask task) {
        Intent intent = new Intent(this, MultipleChoiceTaskActivity.class);
        intent.putExtra("question", task.question);
        intent.putExtra("options", task.options);
        intent.putExtra("answer", task.answer);


        startActivityForResult(intent, 1);
    }

    public void goToPictureTask(View view, MultipleChoiceTask task) {
        Intent intent = new Intent(this, PictureTaskActivity.class);
        intent.putExtra("question", task.question);
        intent.putExtra("options", task.options);
        intent.putExtra("answer", task.answer);


        startActivityForResult(intent, 1);
    }
}