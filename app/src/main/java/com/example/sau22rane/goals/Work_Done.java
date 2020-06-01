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

public class Work_Done extends Fragment implements RecyclerViewAdapter.OnItemClicked, RecyclerViewAdapter.OnDeleteClicked, RecyclerViewAdapter.OnEditClicked {

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> WorkDone_list, WorkDone_title, WorkDone_date, WorkDone_time;
    SQLite a;
    RecyclerViewAdapter customAdapter;
    FloatingActionButton fabAdd;
    String goal;
    int previous = -1;

    public Work_Done(String par){
        super();
        goal = par;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_work_done, container, false);
        Recycle = (RecyclerView) root.findViewById(R.id.WorkDoneRecycle);
        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAddWorkDone);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddWorkDonePopup.class);
                Bundle b = new Bundle();
                b.putString("goal",goal);
                i.putExtras(b);
                startActivityForResult(i,1);
                previous = -1;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        Recycle.setLayoutManager(linearLayoutManager);
        WorkDone_list = new ArrayList<String>();
        WorkDone_date = new ArrayList<String>();
        WorkDone_time = new ArrayList<String>();
        WorkDone_title = new ArrayList<String>();
        a = new SQLite(getContext(),"DEFAULT","Budgets", 1);
        customAdapter = new RecyclerViewAdapter(getContext(), WorkDone_list);
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
        Cursor res = a.get_data("WORKDONE");

        WorkDone_list.clear();
        WorkDone_time.clear();
        WorkDone_title.clear();
        WorkDone_date.clear();

        if(res.getCount() == 0){
            WorkDone_list.add("No Work Done Available. ADD new!");
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()) {
                if (goal.equals(res.getString(4))) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("Title : " + res.getString(1));
                    buffer.append(",Time : " + res.getString(2));
                    buffer.append(",Date : " + res.getString(3));
                    WorkDone_list.add(String.valueOf(buffer));
                    WorkDone_title.add(res.getString(1));
                    WorkDone_time.add(res.getString(2));
                    WorkDone_date.add(res.getString(3));
                }
            }

        }
        if(WorkDone_list.isEmpty()){
            isEmpty = true;
            WorkDone_list.add("No Work Done Available. ADD new!");
        }
        customAdapter.setOnDelete((RecyclerViewAdapter.OnDeleteClicked) Work_Done.this);
        customAdapter.setOnEdit((RecyclerViewAdapter.OnEditClicked) Work_Done.this);
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Work_Done.this);
        Recycle.setAdapter(customAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            if(previous == -1 || WorkDone_list.size()<2 || previous == position) {
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
        a.delete_row("WORKDONE",WorkDone_title.get(position));
        getData();
        previous = -1;
    }

    @Override
    public void onEdit(int position) {
        Toast.makeText(getContext(),"Edit Clicked",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getContext(), EditWorkDonePopup.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",WorkDone_title.get(position));
        bundle.putString("time",WorkDone_time.get(position));
        bundle.putString("date",WorkDone_date.get(position));
        i.putExtras(bundle);
        startActivityForResult(i,2);
        previous = -1;
    }
}
