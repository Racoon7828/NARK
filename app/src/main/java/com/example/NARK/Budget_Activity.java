package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Budget_Activity extends AppCompatActivity {

    private Intent intent;
    private String adapter_title;
    private String adapter_date;
    private String adapter_ainame;
    private String adapter_url;

    private RecyclerView recyclerView;
    private ArrayList<Budget_Data> Budget_List;
    private Budget_Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_info_activity);

        Toolbar toolbar = findViewById(R.id.budget_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.budget_activity_recyclerView);

        adapter = new Budget_Adapter(Budget_List);

        recyclerView.setAdapter(adapter);

        ///////////추가된 부분////////////

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        StrictMode.enableDefaults();

        Budget_List = new ArrayList<>();

        //전달받은 값 가져오기
        intent = getIntent();
        adapter_title = intent.getStringExtra("title");
        adapter_ainame = intent.getStringExtra("ainame");
        adapter_date = intent.getStringExtra("date");
        adapter_url = intent.getStringExtra("link");


        Budget_List.add(new Budget_Data(adapter_date, adapter_ainame, adapter_title, adapter_url,1));

        adapter.setArrayList(Budget_List);

        adapter.notifyDataSetChanged();

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
