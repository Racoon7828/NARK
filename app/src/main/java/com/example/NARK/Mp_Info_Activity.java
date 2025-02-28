package com.example.NARK;

import static com.example.NARK.R.id.mp_info_activity_toolbar;

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

public class Mp_Info_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    Mp_Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //API 파싱한 데이터를 Intent로 받아오는 역활

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String pp = i.getStringExtra("pp");
        String phoneNum = i.getStringExtra("phoneNum");
        String email = i.getStringExtra("email");
        String bf = i.getStringExtra("bf");
        String imgUrl= i.getStringExtra("img");
        int view = i.getIntExtra("view", 0);
        view = 1;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_info_activity);

        Toolbar toolbar = findViewById(mp_info_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.mp_info_activity_recyclerView);
        adapter = new Mp_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //받아온 Intent데이터를 activity_recyclerView에 뿌려주기위해서 Mp_Adapter로 넘겨주는 역활
        Mp_DataModel data = new Mp_DataModel();
        data.setName(name);
        data.setPp(pp);
        data.setPhoneNum(phoneNum);
        data.setEmail(email);
        data.setBf(bf);
        data.setImgUrl(imgUrl);
        data.setViewType(view);

        adapter.Mp_addITem(data);
        adapter.notifyDataSetChanged();
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
