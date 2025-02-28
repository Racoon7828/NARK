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


public class Employ_Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Employ_Adapter Employ_Adapter;
    private ArrayList<Employ_Data> list = new ArrayList<>();

    int index_num = 1;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.employ_main);

        Toolbar toolbar = findViewById(R.id.employ_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.employ_recyclerView);
        Employ_Adapter = new Employ_Adapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Employ_parsing(index_num, str);

        recyclerView.setAdapter(Employ_Adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    ++index_num;
                    Employ_parsing(index_num, str);

                    recyclerView.post(new Runnable() {
                        public void run() {
                            Employ_Adapter.notifyDataSetChanged();
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
            // 검색버튼을 누르면 입력한값을 찾아서 출력
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                str = query;

                Employ_parsing(index_num = 1, str);
                //필터링된 리스트 어댑터에 적용
                Employ_Adapter.setArrayList(list);
                recyclerView.setAdapter(Employ_Adapter);
                return false;
            }

            @Override
            //검색하는 도중에도 서치 -> 공백이면 모든 api 출력
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    list.clear();

                    Employ_parsing(index_num = 1, str = "");
                    recyclerView.setAdapter(Employ_Adapter);
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

    public void Employ_parsing(int index_num, String searchText) {
        boolean inrow = false, e_BLNG_INST_NM = false, e_BRDI_SJ = false, e_BRDI_CN = false, e_RDT = false, e_HOME_URL = false;

        String HOME_URL = null, BLNG_INST_NM = null, BRDI_SJ = null, BRDI_CN = null, RDT = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL("https://open.assembly.go.kr/portal/openapi/nswsyvysaidgdhsch?"
                    + "Key="
                    + "18ce50f77ea4483ba0862c2d1780d9cc"
                    + "&pIndex=" + index_num
                    + "&pSize=25"
                    + "&Type=xml"
                    + "&BRDI_SJ=" + searchText
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();


            parser.setInput(url.openStream(), null);
            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("BLNG_INST_NM")) { //BLNG_INST_NM 만나면 내용을 받을수 있게 하자 -> 기관
                            e_BLNG_INST_NM = true;
                        }
                        if (parser.getName().equals("BRDI_SJ")) { //BRDI_SJ 만나면 내용을 받을수 있게 하자 -> 제목
                            e_BRDI_SJ = true;
                        }
                        if (parser.getName().equals("BRDI_CN")) { //BRDI_CN 만나면 내용을 받을수 있게 하자 -> 내용
                            e_BRDI_CN = true;
                        }
                        if (parser.getName().equals("RDT")) { //RDT 만나면 내용을 받을수 있게 하자 -> 시간
                            e_RDT = true;
                        }
                        if (parser.getName().equals("HOME_URL")) { //HOME_URL 만나면 내용을 받을수 있게 하자 -> url 링크
                            e_HOME_URL = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (e_BLNG_INST_NM) { //e_BLNG_INST_NM이 true일 때 태그의 내용을 저장.
                            BLNG_INST_NM = parser.getText();
                            e_BLNG_INST_NM = false;
                        }
                        if (e_BRDI_SJ) { //e_BRDI_SJ가 true일 때 태그의 내용을 저장.
                            BRDI_SJ = parser.getText();
                            e_BRDI_SJ = false;
                        }
                        if (e_BRDI_CN) {  //e_BRDI_CN이 true일 때 태그의 내용을 저장.
                            BRDI_CN = parser.getText();
                            e_BRDI_CN = false;
                        }
                        if (e_RDT) {  //e_RDT가 true일 때 태그의 내용을 저장.
                            RDT = parser.getText();
                            e_RDT = false;
                        }
                        if (e_HOME_URL) {  //e_HOME_URL이 true일 때 태그의 내용을 저장.
                            HOME_URL = parser.getText();
                            e_HOME_URL = false;
                        }
                        break;
                    case XmlPullParser.END_TAG: //row가 위에 아이템들을 감싸고있음
                        if (parser.getName().equals("row")) {
                            list.add(new Employ_Data(BLNG_INST_NM, BRDI_SJ, BRDI_CN, RDT, HOME_URL, 0));
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

