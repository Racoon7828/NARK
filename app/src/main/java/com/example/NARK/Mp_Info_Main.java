package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mp_Info_Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Mp_Adapter adapter;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private int index_num;
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_info_main);

        Toolbar toolbar = findViewById(R.id.mp_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.mp_info_recyclerView);
        adapter = new Mp_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Mp_parsing(index_num = 1, str);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    Mp_parsing(++index_num, str);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_btn, menu);

        MenuItem menuSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.Mp_clearList();
                str = s;
                Mp_parsing(index_num = 1, str);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    adapter.Mp_clearList();
                    recyclerView.setAdapter(adapter);
                    Mp_parsing(index_num = 1, str = "");
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_gohome:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Mp_parsing(int pageIndex, String searchText) {
        executor.execute(() -> {
            List<String> nameList = new ArrayList<>();
            List<String> ppList = new ArrayList<>();
            List<String> phoneList = new ArrayList<>();
            List<String> emailList = new ArrayList<>();
            List<String> bfList = new ArrayList<>();

            try {
                URL url = new URL("https://open.assembly.go.kr/portal/openapi/nwvrqwxyaytdsfvhu"
                        + "?KEY=" + BuildConfig.MP_API_KEY
                        + "&pIndex=" + pageIndex
                        + "&pSize=20"
                        + "&HG_NM=" + searchText);

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                parser.setInput(url.openStream(), null);

                boolean inHNM = false, inPOLY = false, inTEL = false, inEMAIL = false, inMEM = false;
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            if (tag.equals("HG_NM"))     inHNM = true;
                            else if (tag.equals("POLY_NM"))   inPOLY = true;
                            else if (tag.equals("TEL_NO"))    inTEL = true;
                            else if (tag.equals("E_MAIL"))    inEMAIL = true;
                            else if (tag.equals("MEM_TITLE")) inMEM = true;
                            break;

                        case XmlPullParser.TEXT:
                            String text = parser.getText();
                            if (inHNM)        { nameList.add(text);   inHNM = false; }
                            else if (inPOLY)  { ppList.add(text);     inPOLY = false; }
                            else if (inTEL)   { phoneList.add(text);  inTEL = false; }
                            else if (inEMAIL) { emailList.add(text);  inEMAIL = false; }
                            else if (inMEM)   {
                                String cleaned = text
                                        .replace("&middot;", "·")
                                        .replace("&lsquo;", "")
                                        .replace("&rsquo;", "")
                                        .replace("&#039", "")
                                        .replace("&#39", "")
                                        .replace("&bull;", "")
                                        .replace("&amp;", "")
                                        .replace("&quot;", "");
                                bfList.add(cleaned);
                                inMEM = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(this, "의원 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            List<String> imgList = Mp_ImgParsing(pageIndex, searchText);

            List<Mp_DataModel> results = new ArrayList<>();
            for (int i = 0; i < nameList.size(); i++) {
                Mp_DataModel data = new Mp_DataModel();
                data.setName(nameList.get(i));
                data.setPp(ppList.get(i));
                data.setPhoneNum(phoneList.get(i));
                data.setEmail(emailList.get(i));
                data.setBf(bfList.get(i));
                data.setImgUrl(i < imgList.size() ? imgList.get(i) : "");
                data.setViewType(0);
                results.add(data);
            }

            mainHandler.post(() -> {
                for (Mp_DataModel data : results) {
                    adapter.Mp_addITem(data);
                }
                adapter.notifyDataSetChanged();
            });
        });
    }

    private List<String> Mp_ImgParsing(int pageIndex, String searchText) {
        List<String> imgList = new ArrayList<>();
        try {
            URL url = new URL("https://apis.data.go.kr/9710000/NationalAssemblyInfoService/getMemberNameInfoList?"
                    + "serviceKey=" + BuildConfig.GOVDATA_API_KEY
                    + "&numOfRows=20"
                    + "&pageNo=" + pageIndex
                    + "&hgnm=" + searchText);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            parser.setInput(url.openStream(), null);

            boolean inImg = false;
            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("jpgLink")) inImg = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (inImg) { imgList.add(parser.getText()); inImg = false; }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            // 이미지 로딩 실패는 무시 (텍스트 정보는 표시)
        }
        return imgList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
