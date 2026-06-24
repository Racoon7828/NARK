package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.Bill_btn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Bill_Main.class));
        });

        findViewById(R.id.MP_btn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Mp_Info_Main.class));
        });

        findViewById(R.id.Budget_btn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Budget_Info.class));
        });

        findViewById(R.id.Employ_btn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Employ_Main.class));
        });
    }
}