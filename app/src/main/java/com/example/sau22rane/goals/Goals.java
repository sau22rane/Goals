package com.example.sau22rane.goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Goals extends AppCompatActivity implements RecyclerViewAdapter.OnItemClicked, RecyclerViewAdapter.OnDeleteClicked, RecyclerViewAdapter.OnEditClicked, RecyclerViewAdapter.OnViewClicked{

    boolean isEmpty = false;
    int previous = -1;
    RecyclerView Recycle;
    ArrayList<String> Goal;
    ArrayList<String> Goal_title, Goal_date, Goal_disc;
    SQLite a;
    RecyclerViewAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        FloatingActionButton fab = findViewById(R.id.fabAddGoal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddGoalsPopup.class);
                startActivityForResult(i, 1);
                previous = -1;
            }
        });

        Recycle = (RecyclerView) findViewById(R.id.GoalRecycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        Recycle.setLayoutManager(linearLayoutManager);
        Goal = new ArrayList<String>();
        Goal_title = new ArrayList<String>();
        Goal_date = new ArrayList<String>();
        Goal_disc = new ArrayList<String>();
        customAdapter = new RecyclerViewAdapter(getApplicationContext(), Goal);

        a = new SQLite(getApplicationContext(),"DEFAULT","Goal", 1);
        a.create_goal_table();
        getData();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            getData();
        }
        else if(requestCode==2)
        {
            getData();
        }
    }


    private void getData(){
        Cursor res = a.get_data("GOAL");

        Goal.clear();
        Goal_title.clear();
        Goal_date.clear();
        Goal_disc.clear();
        if(res.getCount() == 0){
            Goal.add("No Goals Available. ADD new!");
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()){
                StringBuffer buffer = new StringBuffer();
                buffer.append(" "+res.getString(1));
                buffer.append(",Description : "+res.getString(2));
                buffer.append(",Date : "+res.getString(3));
                Goal_title.add(res.getString(1));
                Goal_disc.add(res.getString(2));
                Goal_date.add(res.getString(3));
                Goal.add(String.valueOf(buffer));
            }

        }
        customAdapter.setOnView((RecyclerViewAdapter.OnViewClicked) Goals.this);
        customAdapter.setOnDelete((RecyclerViewAdapter.OnDeleteClicked) Goals.this);
        customAdapter.setOnEdit((RecyclerViewAdapter.OnEditClicked) Goals.this);
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Goals.this);
        Recycle.setAdapter(customAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            if(previous == -1) {
                previous = position;
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                if (edit.getVisibility() == View.GONE) {
                    edit.setVisibility(View.VISIBLE);
                } else {
                    edit.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                if (coolo.getVisibility() == View.GONE)
                    coolo.setVisibility(View.VISIBLE);
                else
                    coolo.setVisibility(View.GONE);
            }
            else if(previous != position){
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                FloatingActionButton editp;
                CoordinatorLayout coolop;
                coolop = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.expandable);
                editp = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.edit);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                if (edit.getVisibility() == View.GONE) {
                    edit.setVisibility(View.VISIBLE);
                }
                if (editp.getVisibility() == View.VISIBLE) {
                    editp.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                if (coolo.getVisibility() == View.GONE)
                    coolo.setVisibility(View.VISIBLE);
                if(coolop.getVisibility() == View.VISIBLE)
                    coolop.setVisibility(View.GONE);
                previous = position;
            }
        }
    }

    @Override
    public void onDelete(int position) {
        Toast.makeText(getApplicationContext(),"Delete Clicked",Toast.LENGTH_LONG).show();
        SQLite a = new SQLite(getApplicationContext(),"DEFAULT","BUDGET", 1);
        a.delete_goal(Goal_title.get(position));
        getData();
        previous = -1;
    }

    @Override
    public void onView(int position) {
        Toast.makeText(getApplicationContext(),"View Clicked",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        Bundle b = new Bundle();
        b.putString("goal", Goal_title.get(position));
        i.putExtras(b);
        startActivityForResult(i,2);
        previous = -1;
    }

    @Override
    public void onEdit(int position) {
        Toast.makeText(getApplicationContext(),"Edit Clicked",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), EditGoalsPopup.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",Goal_title.get(position));
        bundle.putString("description",Goal_disc.get(position));
        bundle.putString("date",Goal_date.get(position));
        i.putExtras(bundle);
        startActivityForResult(i,2);
        previous = -1;
    }
}
