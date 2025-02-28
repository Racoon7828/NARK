package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Employ_Activity extends AppCompatActivity {

    private Intent intent;
    private String adapter_title;
    private String adapter_content;
    private String adapter_date;
    private String adapter_ainame;
    private String adapter_url;

    private RecyclerView recyclerView;
    private ArrayList<Employ_Data> Employ_List;
    private Employ_Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employ_activity);

        Toolbar toolbar = findViewById(R.id.employ_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.employ_activity_recyclerView);

        adapter = new Employ_Adapter(Employ_List);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        StrictMode.enableDefaults();

        //전달받은 값 가져오기
        Employ_List = new ArrayList<>();
        intent = getIntent();
        adapter_title = intent.getStringExtra("title");
        adapter_content = intent.getStringExtra("content");
        adapter_ainame = intent.getStringExtra("ainame");
        adapter_date = intent.getStringExtra("date");
        adapter_url = intent.getStringExtra("link");


        Employ_List.add(new Employ_Data(adapter_ainame,adapter_title,stripHtml(adapter_content),adapter_date,adapter_url,1));
        adapter.setArrayList(Employ_List);
        adapter.notifyDataSetChanged();

    }

    //html 태그 적용
    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.acustom_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home: {
                // todo
                onBackPressed();
                return true;
            }
            case R.id.action_gohome: {
                // TODO
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

