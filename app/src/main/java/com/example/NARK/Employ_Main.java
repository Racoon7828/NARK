package com.example.NARK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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

public class Employ_Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Employ_Adapter Employ_Adapter;
    private ArrayList<Employ_Data> list = new ArrayList<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private int index_num = 1;
    private String str = "";

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
        recyclerView.setAdapter(Employ_Adapter);

        Employ_parsing(index_num, str);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    Employ_parsing(++index_num, str);
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
                Employ_parsing(index_num = 1, str);
                Employ_Adapter.setArrayList(list);
                recyclerView.setAdapter(Employ_Adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
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

    private void Employ_parsing(int pageIndex, String searchText) {
        executor.execute(() -> {
            List<Employ_Data> results = new ArrayList<>();
            try {
                URL url = new URL("https://open.assembly.go.kr/portal/openapi/nswsyvysaidgdhsch?"
                        + "Key=" + BuildConfig.EMPLOY_API_KEY
                        + "&pIndex=" + pageIndex
                        + "&pSize=25"
                        + "&Type=xml"
                        + "&BRDI_SJ=" + searchText);

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                parser.setInput(url.openStream(), null);

                String instNm = null, brdiSj = null, brdiCn = null, rdt = null, homeUrl = null;
                boolean inInstNm = false, inBrdiSj = false, inBrdiCn = false, inRdt = false, inHomeUrl = false;

                int parserEvent = parser.getEventType();
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            if (tag.equals("BLNG_INST_NM")) inInstNm = true;
                            else if (tag.equals("BRDI_SJ"))    inBrdiSj = true;
                            else if (tag.equals("BRDI_CN"))    inBrdiCn = true;
                            else if (tag.equals("RDT"))        inRdt = true;
                            else if (tag.equals("HOME_URL"))   inHomeUrl = true;
                            break;

                        case XmlPullParser.TEXT:
                            String text = parser.getText();
                            if (inInstNm)      { instNm = text;  inInstNm = false; }
                            else if (inBrdiSj) { brdiSj = text; inBrdiSj = false; }
                            else if (inBrdiCn) { brdiCn = text; inBrdiCn = false; }
                            else if (inRdt)    { rdt = text;    inRdt = false; }
                            else if (inHomeUrl) { homeUrl = text; inHomeUrl = false; }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("row")) {
                                results.add(new Employ_Data(instNm, brdiSj, brdiCn, rdt, homeUrl, 0));
                                instNm = null; brdiSj = null; brdiCn = null; rdt = null; homeUrl = null;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(this, "채용 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            mainHandler.post(() -> {
                list.addAll(results);
                Employ_Adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
