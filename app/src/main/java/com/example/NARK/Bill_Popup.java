package com.example.NARK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Bill_Popup extends AppCompatActivity {
    RecyclerView recyclerView;
    bill_Adapter bill_Adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // Bill_Activity랑 다를거 없음
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bill_popup);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String date = i.getStringExtra("date");
        String prp = i.getStringExtra("prp");
        String url = i.getStringExtra("url");
        int view = i.getIntExtra("view", 0);
        view = 1; // ViewHolder2를 사용

        recyclerView = (RecyclerView) findViewById(R.id.bill_popup_recyclerView);
        bill_Adapter = new bill_Adapter();

        recyclerView.setAdapter(bill_Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bill_Data data = new Bill_Data(name,date,prp,url);
        data.setbName(name);
        data.setbDate(date);
        data.setbPrp(prp);
        data.setbUrl(url);
        data.setViewType(view);

        bill_Adapter.addItem(data);
        bill_Adapter.notifyDataSetChanged();

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 1); //Display 사이즈의 90% 각자 원하는 사이즈로 설정하여 사용

        getWindow().getAttributes().width = width;
    }

    //확인 버튼 클릭
    public void btnPopupClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게 함
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
