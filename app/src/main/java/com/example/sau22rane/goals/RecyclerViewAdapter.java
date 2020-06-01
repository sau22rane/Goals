package com.example.sau22rane.goals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EventViewHolder>{


    Context context;
    ArrayList<String> mList;
    private OnItemClicked onClick;
    private OnDeleteClicked onDeleteClick;
    private OnEditClicked onEditClick;
    private OnViewClicked onViewClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }
    public interface OnDeleteClicked {
        void onDelete(int position);
    }
    public interface OnViewClicked {
        void onView(int position);
    }
    public interface OnEditClicked {
        void onEdit(int position);
    }
    public RecyclerViewAdapter(Context context, ArrayList<String> eventList){
        this.context = context;
        this.mList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        EventViewHolder vh = new EventViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        String[] strings;
        strings = mList.get(position).split(",");
        if(strings.length == 3) {
            holder.mTextView1.setText(strings[0]);
            holder.mTextView2.setText(strings[1]);
            holder.mTextView3.setText(strings[2]);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(position);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClick.onEdit(position);
                }
            });
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClick.onView(position);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClick.onDelete(position);
                }
            });
        }
        else if(strings.length==1){
            holder.mTextView2.setText(strings[0]);

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView1,mTextView2,mTextView3;
        FloatingActionButton edit;
        CoordinatorLayout mCoordinate;
        Button view, delete;
        LinearLayout layout;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoordinate = (CoordinatorLayout) itemView.findViewById(R.id.expandable);
            view = (Button) itemView.findViewById(R.id.view);
            delete = (Button) itemView.findViewById(R.id.delete);
            edit = (FloatingActionButton) itemView.findViewById(R.id.edit);
            layout = (LinearLayout) itemView.findViewById(R.id.content_container);
            mTextView1 = (TextView) itemView.findViewById(R.id.title1);
            mTextView2 = (TextView) itemView.findViewById(R.id.title2);
            mTextView3 = (TextView) itemView.findViewById(R.id.title3);

        }
        @SuppressLint("RestrictedApi")
        public void toggle_edit(){
            if(edit.getVisibility()==View.GONE){
                edit.setVisibility(View.VISIBLE);
            }
            else{
                edit.setVisibility(View.GONE);
            }
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
    public void setOnDelete(OnDeleteClicked onDeleteClick)
    {
        this.onDeleteClick=onDeleteClick;
    }
    public void setOnEdit(OnEditClicked onEditClick)
    {
        this.onEditClick=onEditClick;
    }
    public void setOnView(OnViewClicked onViewClick)
    {
        this.onViewClick=onViewClick;
    }
}
