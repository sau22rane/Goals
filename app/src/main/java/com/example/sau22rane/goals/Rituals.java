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

import java.sql.RowId;
import java.util.ArrayList;

public class Rituals extends Fragment implements RecyclerViewAdapter.OnItemClicked, RecyclerViewAdapter.OnDeleteClicked, RecyclerViewAdapter.OnEditClicked {

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> Ritual_list, Ritual_title, Ritual_date, Ritual_time, Ritual_rating;
    SQLite a;
    RecyclerViewAdapter customAdapter;
    FloatingActionButton fabAdd;
    String goal;
    int previous = -1;

    public Rituals(String par){
        super();
        goal = par;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rituals, container, false);
        Recycle = (RecyclerView) root.findViewById(R.id.RitualRecycle);
        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAddRitual);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddRitualsPopup.class);
                Bundle b = new Bundle();
                b.putString("goal",goal);
                i.putExtras(b);
                startActivityForResult(i,1);
                previous = -1;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        Recycle.setLayoutManager(linearLayoutManager);
        Ritual_list = new ArrayList<String>();
        Ritual_date = new ArrayList<String>();
        Ritual_time = new ArrayList<String>();
        Ritual_title = new ArrayList<String>();
        Ritual_rating = new ArrayList<>();
        a = new SQLite(getContext(),"DEFAULT","Budgets", 1);
        customAdapter = new RecyclerViewAdapter(getContext(), Ritual_list);
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
        Cursor res = a.get_data("RITUAL");

        Ritual_list.clear();
        Ritual_time.clear();
        Ritual_title.clear();
        Ritual_date.clear();
        Ritual_rating.clear();

        if(res.getCount() == 0){
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()){
                if(goal.equals(res.getString(5))) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("Title : " + res.getString(1));
                    buffer.append(",Time : " + res.getString(2));
                    buffer.append("  Date : " + res.getString(3));
                    buffer.append(",Rating : " + res.getString(4));
                    Ritual_list.add(String.valueOf(buffer));
                    Ritual_title.add(res.getString(1));
                    Ritual_time.add(res.getString(2));
                    Ritual_date.add(res.getString(3));
                    Ritual_rating.add(res.getString(4));
                }
            }

        }
        if(Ritual_list.isEmpty()){
            isEmpty = true;
            Ritual_list.add("No Work Done Available. ADD new!");
        }
        customAdapter.setOnDelete((RecyclerViewAdapter.OnDeleteClicked) Rituals.this);
        customAdapter.setOnEdit((RecyclerViewAdapter.OnEditClicked) Rituals.this);
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Rituals.this);
        Recycle.setAdapter(customAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            if(previous == -1 || Ritual_list.size()<2 || previous == position) {
                previous = position;
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                Button view;
                view = (Button) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.view);
                view.setVisibility(View.GONE);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);                    edit.setVisibility(View.VISIBLE);

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
        a.delete_row("RITUAL",Ritual_title.get(position));
        getData();
        previous = -1;
    }


    @Override
    public void onEdit(int position) {
        Toast.makeText(getContext(),"Edit Clicked",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getContext(), EditRitualsPopup.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",Ritual_title.get(position));
        bundle.putString("time",Ritual_time.get(position));
        bundle.putString("date",Ritual_date.get(position));
        bundle.putString("rating",Ritual_rating.get(position));
        i.putExtras(bundle);
        startActivityForResult(i,2);
        previous = -1;
    }
}
