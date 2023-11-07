package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private DataBaseHelper2 myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

    private Button deleteButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        deleteButton = findViewById(R.id.DeleteBtn);
        myDB = new DataBaseHelper2(HomeActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, HomeActivity.this);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        // Initialize delete button
        deleteButton.setVisibility(View.INVISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog
                showDeleteConfirmationDialog();
            }
        });

        // Handle the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_total) {
                    // Load the TotalTasksFragment
                    TotalTasksFragment totalTasksFragment = new TotalTasksFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, totalTasksFragment);
                    transaction.commit();
                }
                else if (item.getItemId() == R.id.menu_home) {
                    // Check if the TotalTasksFragment is visible, then remove it
                    // and go back to the home activity
                    TotalTasksFragment totalTasksFragment = (TotalTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (totalTasksFragment != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.remove(totalTasksFragment);
                        transaction.commit();
                    }
                }

                return true;
            }
        });

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }

    // Show the Delete button
    public void showDeleteButton() {
        deleteButton.setVisibility(View.VISIBLE);
    }

    // Hide the Delete button
    public void hideDeleteButton() {
        deleteButton.setVisibility(View.INVISIBLE);
    }

    // Show a confirmation dialog before deleting the tasks
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Tasks");
        builder.setMessage("Are you sure you want to delete the selected tasks?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSelectedTasks();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If No is selected, close the dialog and do nothing.
            }
        });
        builder.create().show();
    }

    // Delete selected tasks
    private void deleteSelectedTasks() {
        List<ToDoModel> selectedTasks = adapter.getSelectedTasks();
        for (ToDoModel task : selectedTasks) {
            myDB.deleteTask(task.getId());
            mList.remove(task);
        }
        adapter.notifyDataSetChanged();
        hideDeleteButton();
    }
}
