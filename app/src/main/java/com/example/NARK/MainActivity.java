package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bill_btn = (Button) findViewById(R.id.Bill_btn);
        bill_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Bill_Main.class);
            startActivity(intent);
        });

        Button MP_btn = (Button) findViewById(R.id.MP_btn);
        MP_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Mp_Info_Main.class);
            startActivity(intent);
        });

        Button Budget_btn = (Button) findViewById(R.id.Budget_btn);
        Budget_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Budget_Info.class);
            startActivity(intent);
        });

        Button Employ_btn = (Button) findViewById(R.id.Employ_btn);
        Employ_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Employ_Main.class);
            startActivity(intent);
        });
    }
}