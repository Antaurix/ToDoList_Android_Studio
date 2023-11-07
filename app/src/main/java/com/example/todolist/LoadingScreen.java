package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.animation.ObjectAnimator;

public class LoadingScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        ProgressBar progressBar = (ProgressBar) findViewById (R.id.progress_bar);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 100);
        animation.setDuration (5000);
        animation.start ();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingScreen.this,HomeActivity.class));
                finish();
            }
        }, 5000);
    }


}
