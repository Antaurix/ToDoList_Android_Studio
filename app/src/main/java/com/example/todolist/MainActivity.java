package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Login(View view) {
        EditText username = (EditText) findViewById(R.id.userLogin);
        EditText password = (EditText) findViewById(R.id.userPassword);

        String enteredUsername = username.getText().toString().trim();
        String enteredPassword = password.getText().toString().trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter your username and password", Toast.LENGTH_LONG).show();
            return;
        }

        // Query the database to check if the user exists with the entered username and password
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean loginSuccessful = databaseHelper.checkLogin(enteredUsername, enteredPassword);

        if (loginSuccessful) {
            // Login successful
            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            // Redirect to the user's dashboard
            Intent intent = new Intent(MainActivity.this,LoadingScreen.class);
            startActivity(intent);
        } else {
            // Login failed
            Toast.makeText(MainActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();
        }
    }

    public void Register1(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
