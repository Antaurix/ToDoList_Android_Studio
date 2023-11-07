package com.example.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Utils.DataBaseHelper2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TotalTasksFragment extends Fragment {

    private TextView totalTasksText;
    private DataBaseHelper2 myDB;

    private TextView currentDateText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_tasks, container, false);


        totalTasksText = view.findViewById(R.id.total_tasks_text);

        currentDateText = view.findViewById(R.id.current_date_text);

        myDB = new DataBaseHelper2(requireContext());


        int totalTasks = calculateTotalTasks();
        totalTasksText.setText("Total Tasks: " + totalTasks);

        String currentDate = getCurrentDate();
        currentDateText.setText("Due Today: " + currentDate);

        return view;
    }

    // Calculate the total tasks
    private int calculateTotalTasks() {

        return myDB.getTotalTasksCount();
    }

    // Get the current date.
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
