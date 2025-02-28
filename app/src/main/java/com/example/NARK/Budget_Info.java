package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class Budget_Info extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Budget_Adapter Budget_Adapter;
    private ArrayList<Budget_Data> list = new ArrayList<>();

    int index_num = 1;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_info);

        Toolbar toolbar = findViewById(R.id.budget_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.budget_recyclerView);

        Budget_Adapter = new Budget_Adapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Budget_parsing(index_num, str);

        recyclerView.setAdapter(Budget_Adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    ++index_num;
                    Budget_parsing(index_num, str);

                    recyclerView.post(new Runnable() {
                        public void run() {
                            Budget_Adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_btn, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                str = query;

                Budget_parsing(index_num = 1, str);
                Budget_Adapter.setArrayList(list);
                recyclerView.setAdapter(Budget_Adapter);
                return false;
            }

            @Override
            //검색하는 도중에도 서치
            public boolean onQueryTextChange(String newText) {
                ArrayList<Budget_Data> filterList = new ArrayList<>();
                if(newText.equals("")) {
                    list.clear();

                    Budget_parsing(index_num = 1, str = "");
                    recyclerView.setAdapter(Budget_Adapter);
                }
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void Budget_parsing(int index_num, String searchText) {
        boolean inrow = false, b_REG_DATE = false, b_DEPARTMENT_NAME = false, b_SUBJECT = false, b_LINK_URL = false;

        String REG_DATE = null, DEPARTMENT_NAME = null, SUBJECT = null, LINK_URL = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("https://open.assembly.go.kr/portal/openapi/nxrkedghaikxodlja?"
                    + "Key="
                    + "18ce50f77ea4483ba0862c2d1780d9cc"
                    + "&pIndex=" + index_num
                    + "&pSize=30"
                    + "&Type=xml"
                    + "&SUBJECT=" + searchText
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();


            parser.setInput(url.openStream(), null);
            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("REG_DATE")) { //REG_DATE 만나면 내용을 받을수 있게 하자 -> 발간일
                            b_REG_DATE = true;
                        }
                        if (parser.getName().equals("DEPARTMENT_NAME")) { //DEPARTMENT_NAME 만나면 내용을 받을수 있게 하자 -> 부서 명
                            b_DEPARTMENT_NAME = true;
                        }
                        if (parser.getName().equals("SUBJECT")) { //SUBJECT 만나면 내용을 받을수 있게 하자 -> 보고서 명
                            b_SUBJECT = true;
                        }
                        if (parser.getName().equals("LINK_URL")) { //LINK_URL 만나면 내용을 받을수 있게 하자 -> url 링크
                            b_LINK_URL = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (b_REG_DATE) { //b_REG_DATE가 true일 때 태그의 내용을 저장.
                            REG_DATE = parser.getText();
                            b_REG_DATE = false;
                        }
                        if (b_DEPARTMENT_NAME) { //b_DEPARTMENT_NAME이 true일 때 태그의 내용을 저장.
                            DEPARTMENT_NAME = parser.getText();
                            b_DEPARTMENT_NAME = false;
                        }
                        if (b_SUBJECT) {  //b_SUBJECT가 true일 때 태그의 내용을 저장.
                            SUBJECT = parser.getText();
                            b_SUBJECT = false;
                        }
                        if (b_LINK_URL) {  //b_LINK_URL가 true일 때 태그의 내용을 저장.
                            LINK_URL = parser.getText();
                            b_LINK_URL = false;
                        }
                        break;

                    case XmlPullParser.END_TAG: //row가 위에 아이템들을 감싸고있음
                        if (parser.getName().equals("row")) {
                            list.add(new Budget_Data(REG_DATE, DEPARTMENT_NAME, SUBJECT, LINK_URL, 0));
                            inrow = false;
                        }
                        break;
                }
                parserEvent = parser.next();

            }
        } catch (Exception e) {
            System.out.println("에러");
        }
    }


}


