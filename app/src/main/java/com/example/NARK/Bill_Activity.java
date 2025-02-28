package com.example.NARK;

import static com.example.NARK.R.id.bill_activity_toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Bill_Activity extends AppCompatActivity{
    RecyclerView recyclerView;
    bill_Adapter bill_Adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity);

        Toolbar toolbar = findViewById(bill_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        Integer num = i.getIntExtra("num",2);
        String date = i.getStringExtra("date");
        String prp = i.getStringExtra("prp");
        String prep = i.getStringExtra("prep");
        String ra= i.getStringExtra("ra");
        String dae= i.getStringExtra("dae");
        String crd= i.getStringExtra("crd");
        String proResult= i.getStringExtra("proResult");
        String proDate= i.getStringExtra("proDate");
        String url= i.getStringExtra("url");
        int view = i.getIntExtra("view", 0);
        view = 2; // ViewHolder3을 사용

        recyclerView = (RecyclerView) findViewById(R.id.bill_activity_recyclerView);
        bill_Adapter = new bill_Adapter();

        recyclerView.setAdapter(bill_Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bill_Data data = new Bill_Data(name, num, date, prp, prep, ra, dae, crd, proResult, proDate, url, view);


        data.setbName(name);
        data.setbNum(num);
        data.setbDate(date);
        data.setbPrp(prp);
        data.setbPrep(prep);
        data.setbRa(ra);
        data.setbDae(dae);
        data.setbCrd(crd);
        data.setbProResult(proResult);
        data.setbProDate(proDate);
        data.setbUrl(url);
        data.setViewType(view);

        bill_Adapter.addItem(data);
        bill_Adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.acustom_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
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
