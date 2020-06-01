package com.example.sau22rane.goals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Expectations extends Fragment implements RecyclerViewAdapter.OnItemClicked, RecyclerViewAdapter.OnDeleteClicked, RecyclerViewAdapter.OnEditClicked {

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> Expectations_list, Expectations_title, Expectations_date, Expectations_time;
    SQLite a;
    RecyclerViewAdapter customAdapter;
    FloatingActionButton fabAdd;
    String goal;
    int previous = -1;

    public Expectations(String par){
        super();
        goal = par;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expectations, container, false);
        Recycle = (RecyclerView) root.findViewById(R.id.ExpectationsRecycle);
        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAddExpectations);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddExpectationsPopup.class);
                Bundle b = new Bundle();
                b.putString("goal",goal);
                i.putExtras(b);
                startActivityForResult(i,1);
                previous = -1;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        Recycle.setLayoutManager(linearLayoutManager);
        Expectations_list = new ArrayList<String>();
        Expectations_date = new ArrayList<String>();
        Expectations_time = new ArrayList<String>();
        Expectations_title = new ArrayList<String>();
        a = new SQLite(getContext(),"DEFAULT","Budgets", 1);
        customAdapter = new RecyclerViewAdapter(getContext(), Expectations_list);
        getData();
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
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
        Cursor res = a.get_data("EXPECTATION");

        Expectations_list.clear();
        Expectations_time.clear();
        Expectations_title.clear();
        Expectations_date.clear();

        if(res.getCount() == 0){
            Expectations_list.add("No Expectations Available. ADD new!");
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()){
                if(goal.equals(res.getString(4))) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("Title : " + res.getString(1));
                    buffer.append(",Time : " + res.getString(2));
                    buffer.append(",Date : " + res.getString(3));
                    Expectations_list.add(String.valueOf(buffer));
                    Expectations_title.add(res.getString(1));
                    Expectations_time.add(res.getString(2));
                    Expectations_date.add(res.getString(3));
                }
            }

        }
        if(Expectations_list.isEmpty()){
            isEmpty = true;
            Expectations_list.add("No Work Done Available. ADD new!");
        }
        customAdapter.setOnDelete((RecyclerViewAdapter.OnDeleteClicked) Expectations.this);
        customAdapter.setOnEdit((RecyclerViewAdapter.OnEditClicked) Expectations.this);
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Expectations.this);
        Recycle.setAdapter(customAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            if(previous == -1 || Expectations_list.size()<2 || previous == position) {
                previous = position;
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                Button view;
                view = (Button) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.view);
                view.setVisibility(View.GONE);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                edit.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                coolo.setVisibility(View.VISIBLE);
            }
            else if(previous!=position){
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                Button view;
                FloatingActionButton editp;
                CoordinatorLayout coolop;
                Button viewp;
                view = (Button) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.view);
                view.setVisibility(View.GONE);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                viewp = (Button) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.view);
                viewp.setVisibility(View.GONE);
                coolop = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.expandable);
                editp = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.edit);
                edit.setVisibility(View.VISIBLE);
                editp.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                coolo.setVisibility(View.VISIBLE);
                coolop.setVisibility(View.GONE);
                previous = position;
            }
        }
    }

    @Override
    public void onDelete(int position) {
        Toast.makeText(getContext(),"Delete Clicked",Toast.LENGTH_LONG).show();
        SQLite a = new SQLite(getContext(),"DEFAULT","BUDGET", 1);
        a.delete_row("EXPECTATION",Expectations_title.get(position));
        getData();
        previous = -1;
    }

    @Override
    public void onEdit(int position) {
        Intent i = new Intent(getContext(), EditExpectationsPopup.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",Expectations_title.get(position));
        bundle.putString("time",Expectations_time.get(position));
        bundle.putString("date",Expectations_date.get(position));
        i.putExtras(bundle);
        startActivityForResult(i,2);
        previous = -1;
    }
}
