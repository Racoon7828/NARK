package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Mp_Info_Main extends AppCompatActivity {
    // api받아온거 저장되는곳 전역변수로 만들었음 이유 api 받아오는거 함수로만들어서 전역변수아니면 안됨
    static List<String> listName = new ArrayList<>();
    static List<String> listPp = new ArrayList<>();
    static List<String> listPhoneNum = new ArrayList<>();
    static List<String> listEmail = new ArrayList<>();
    static List<String> listBf = new ArrayList<>();
    static List<String> listImg = new ArrayList<>();
    static ArrayList<Integer> listViewType = new ArrayList<Integer>();

    int index_num;
    String str = "";

    RecyclerView recyclerView;
    Mp_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //뒤로가기하고 다시실행시키면 중복되는현상이발생 따라서 처음불러올때 api받아온변수를 초기화하는역활
        listName.clear();
        listPp.clear();
        listPhoneNum.clear();
        listEmail.clear();
        listBf.clear();
        listImg.clear();
        listViewType.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_info_main);

        Toolbar toolbar = findViewById(R.id.mp_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.mp_info_recyclerView);
        adapter = new Mp_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Mp_parsing(adapter, index_num = 1, str); // api 파싱하는 함수 밑에 구현되어있음

        adapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    listName.clear();
                    listPp.clear();
                    listPhoneNum.clear();
                    listEmail.clear();
                    listBf.clear();
                    listImg.clear();
                    listViewType.clear();
                    ++index_num;

                    Mp_parsing(adapter, index_num, str);

                    recyclerView.post(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_btn, menu);

        //서치뷰 옵션들(검색)
        MenuItem menuSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //검색 리스너
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //이건 검색버튼눌러야지 활성화되는곳 그래서 사용 안함
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.Mp_clearList();
                listName.clear();
                listPp.clear();
                listPhoneNum.clear();
                listEmail.clear();
                listBf.clear();
                listImg.clear();
                listViewType.clear();
                str = s;

                Mp_parsing(adapter, index_num = 1, str);

                return false;
            }

            //이건 텍스트입력시 바로 활성화하는곳 그래서 사용함
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    adapter.Mp_clearList();
                    listName.clear();
                    listPp.clear();
                    listPhoneNum.clear();
                    listEmail.clear();
                    listBf.clear();
                    listImg.clear();
                    listViewType.clear();

                    recyclerView.setAdapter(adapter);
                    Mp_parsing(adapter, index_num = 1, str = "");
                }

                return false;
            }
        });
        return true;
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

    // 파싱 및 데이터 추가 함수
    public void Mp_parsing(Mp_Adapter adapter, int index_num, String searchText) {
        boolean inrow = false, HNM = false, POLY = false, TEL = false, EMAIL = false, MEM = false;

        String HG_NM = null, POLY_NM = null, TEL_NO = null, E_MAIL = null, MEM_TITLE = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            URL url = new URL("https://open.assembly.go.kr/portal/openapi/nwvrqwxyaytdsfvhu" +
                    "?KEY=" +
                    "07120b87e9564c569a9d6098e15fae3a&" +
                    "&pIndex=" + index_num +
                    "&pSize=20"+
                    "&HG_NM=" + searchText);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("HG_NM")) { //HG_NM 만나면 내용을 받을수 있게 하자 -> 이름
                            HNM = true;
                        }
                        if (parser.getName().equals("POLY_NM")) { //POLY_NM 만나면 내용을 받을수 있게 하자 -> 정당
                            POLY = true;
                        }
                        if (parser.getName().equals("TEL_NO")) { //TEL_NO 만나면 내용을 받을수 있게 하자 -> 핸드폰번호
                            TEL = true;
                        }
                        if (parser.getName().equals("E_MAIL")) { //E_MAIL 만나면 내용을 받을수 있게 하자 -> 이메일
                            EMAIL = true;
                        }
                        if (parser.getName().equals("MEM_TITLE")) { //MEM_TITLE 만나면 내용을 받을수 있게 하자 -> 약력
                            MEM = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (HNM) { //HNM이 true일 때 태그의 내용을 저장.
                            HG_NM = parser.getText();
                            listName.add(HG_NM);
                            listViewType.add(0);
                            HNM = false;
                        }
                        if (POLY) { //POLY이 true일 때 태그의 내용을 저장.
                            POLY_NM = parser.getText();
                            listPp.add(POLY_NM);
                            POLY = false;
                        }
                        if (TEL) {
                            TEL_NO = parser.getText();
                            listPhoneNum.add(TEL_NO);
                            TEL = false;
                        }
                        if (EMAIL) {
                            E_MAIL = parser.getText();
                            listEmail.add(E_MAIL);
                            EMAIL = false;
                        }
                        if (MEM) {
                            MEM_TITLE = parser.getText();
                            MEM_TITLE = MEM_TITLE.replace("&middot;", "·");
                            MEM_TITLE = MEM_TITLE.replace("&lsquo;", "");
                            MEM_TITLE = MEM_TITLE.replace("&rsquo;", "");
                            MEM_TITLE = MEM_TITLE.replace("&#039", "");
                            MEM_TITLE = MEM_TITLE.replace("&#39", "");
                            MEM_TITLE = MEM_TITLE.replace("&bull;", "");
                            MEM_TITLE = MEM_TITLE.replace("&amp;", "");
                            MEM_TITLE = MEM_TITLE.replace("&quot;", "");
                            listBf.add(MEM_TITLE);
                            MEM = false;
                        }
                        break;
                    case XmlPullParser.END_TAG: //row가 위에 아이템들을 감싸고있음
                        if (parser.getName().equals("row")) {
                            inrow = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            System.out.println("에러");
        }

        //사진을 가져오기위해 urlParsing 함수 사용
        Mp_ImgParsing(listImg, index_num, searchText);

        for (int i = 0; i < listName.size(); i++) {
            Mp_DataModel data = new Mp_DataModel();
            data.setName(listName.get(i));
            data.setPp(listPp.get(i));
            data.setPhoneNum(listPhoneNum.get(i));
            data.setEmail(listEmail.get(i));
            data.setBf(listBf.get(i));
            data.setImgUrl(listImg.get(i));
            data.setViewType(listViewType.get(i));

            adapter.Mp_addITem(data);
        }
    }

    // url파싱 함수
    public static void Mp_ImgParsing(List<String> listImg, int index_num, String searchText) {
        boolean item = false, img = false;

        String jpgLink = null;

        try {
            URL url2 = new URL("https://apis.data.go.kr/9710000/NationalAssemblyInfoService/getMemberNameInfoList?" +
                    "serviceKey=" +
                    "USurYD2%2FDHJBLlHeua%2FpryqROLKA3A9B1t5sfSC3HKk800e6YlUToqxPUPs7Rlu3CYL%2Fheex7kImTTWKhHPzEA%3D%3D" +
                    "&numOfRows=20" +
                    "&pageNo=" + index_num +
                    "&&hgnm=" + searchText);

            XmlPullParserFactory parserCreator2 = XmlPullParserFactory.newInstance();
            XmlPullParser parser2 = parserCreator2.newPullParser();

            parser2.setInput(url2.openStream(), null);

            int parserEvent2 = parser2.getEventType();

            while (parserEvent2 != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent2) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser2.getName().equals("jpgLink")) { //jpgLink 만나면 내용을 받을수 있게 하자 -> jpgLink가 사진 url로 되있음.
                            img = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (img) {
                            jpgLink = parser2.getText();
                            listImg.add(jpgLink);
                            img = false;
                        }
                        break;

                    case XmlPullParser.END_TAG: // item이 위에 아이템들을 감싸고있음
                        if (parser2.getName().equals("item")) {
                            item = false;
                        }
                        break;
                }
                parserEvent2 = parser2.next();
            }

        } catch (Exception e) {
            System.out.println("에러");
        }

    }
}
