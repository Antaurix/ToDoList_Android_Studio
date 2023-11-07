package com.example.todolist.Adapter;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.HomeActivity;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHelper2;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private HomeActivity activity;
    private DataBaseHelper2 myDB;
    private List<ToDoModel> selectedTasks = new ArrayList<>();

    public ToDoAdapter(DataBaseHelper2 myDB, HomeActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedTasks.add(item); // Add the task to the selectedTasks list
                    activity.showDeleteButton();
                } else {
                    selectedTasks.remove(item); // Remove the task from the selectedTasks list
                    if (selectedTasks.isEmpty()) {
                        activity.hideDeleteButton();
                    }
                }
            }
        });


        String priority = item.getPriority();


        if (priority != null) {
            switch (priority) {
                case "urgent":
                    holder.card.setCardBackgroundColor(Color.RED);
                    break;
                case "normal":
                    holder.card.setCardBackgroundColor(Color.YELLOW);
                    break;
                case "negligible":
                    holder.card.setCardBackgroundColor(Color.GREEN);
                    break;
            }
        }

    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public List<ToDoModel> getSelectedTasks() {
        return selectedTasks;
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        selectedTasks.remove(item);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("priority", item.getPriority());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            card = itemView.findViewById(R.id.myCardview);
        }
    }
}
