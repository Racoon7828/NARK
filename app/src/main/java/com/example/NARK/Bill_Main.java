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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bill_Main extends AppCompatActivity {

    private ArrayList<Bill_Data> billList = new ArrayList<>();
    private RecyclerView recyclerView;
    private bill_Adapter bill_Adapter;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private int index_num;
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_main);

        Toolbar toolbar = findViewById(R.id.bill_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.bill_recyclerView);
        bill_Adapter = new bill_Adapter();

        recyclerView.setAdapter(bill_Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parsing(index_num = 1, str);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    parsing(++index_num, str);
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
                bill_Adapter.Bill_clearList();
                billList.clear();
                str = "&BILL_NAME=" + s;
                parsing(index_num = 1, str);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    bill_Adapter.Bill_clearList();
                    billList.clear();
                    parsing(index_num = 1, str = "");
                    recyclerView.setAdapter(bill_Adapter);
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

    private void parsing(int pageIndex, String searchText) {
        executor.execute(() -> {
            List<Bill_Data> results = new ArrayList<>();
            try {
                URL url = new URL("https://open.assembly.go.kr/portal/openapi/TVBPMBILL11?"
                        + "KEY=" + BuildConfig.ASSEMBLY_API_KEY
                        + "&pIndex=" + pageIndex
                        + "&pSize=20"
                        + "&Type=xml"
                        + searchText);

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                parser.setInput(url.openStream(), null);

                String billName = null, proposeDt = null, proposer = null, proposeKind = null;
                String currComittee = null, age = null, commiteeDt = null;
                String procResultCd = null, procDt = null, linkUrl = null;
                Integer billNo = null;

                boolean inBillName = false, inBillNo = false, inProposeDt = false, inProposer = false;
                boolean inProposeKind = false, inCurrComittee = false, inAge = false;
                boolean inCommiteeDt = false, inProcResultCd = false, inProcDt = false, inLinkUrl = false;

                int parserEvent = parser.getEventType();
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            if (tag.equals("BILL_NAME"))      inBillName = true;
                            else if (tag.equals("BILL_NO"))        inBillNo = true;
                            else if (tag.equals("PROPOSE_DT"))     inProposeDt = true;
                            else if (tag.equals("PROPOSER"))       inProposer = true;
                            else if (tag.equals("PROPOSER_KIND"))  inProposeKind = true;
                            else if (tag.equals("CURR_COMMITTEE")) inCurrComittee = true;
                            else if (tag.equals("AGE"))            inAge = true;
                            else if (tag.equals("COMMITTEE_DT"))   inCommiteeDt = true;
                            else if (tag.equals("PROC_RESULT_CD")) inProcResultCd = true;
                            else if (tag.equals("PROC_DT"))        inProcDt = true;
                            else if (tag.equals("LINK_URL"))       inLinkUrl = true;
                            break;

                        case XmlPullParser.TEXT:
                            String text = parser.getText();
                            if (inBillName)      { billName = text;                      inBillName = false; }
                            else if (inBillNo)        { billNo = Integer.parseInt(text);      inBillNo = false; }
                            else if (inProposeDt)     { proposeDt = text;                    inProposeDt = false; }
                            else if (inProposer)      { proposer = text;                     inProposer = false; }
                            else if (inProposeKind)   { proposeKind = text;                  inProposeKind = false; }
                            else if (inCurrComittee)  { currComittee = text;                 inCurrComittee = false; }
                            else if (inAge)           { age = text;                          inAge = false; }
                            else if (inCommiteeDt)    { commiteeDt = text;                   inCommiteeDt = false; }
                            else if (inProcResultCd)  { procResultCd = text;                 inProcResultCd = false; }
                            else if (inProcDt)        { procDt = text;                       inProcDt = false; }
                            else if (inLinkUrl)       { linkUrl = text;                      inLinkUrl = false; }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("row")) {
                                results.add(new Bill_Data(billName, billNo, proposeDt, proposer,
                                        proposeKind, currComittee, age, commiteeDt,
                                        procResultCd, procDt, linkUrl, 0));
                                billName = null; billNo = null; proposeDt = null;
                                proposer = null; proposeKind = null; currComittee = null;
                                age = null; commiteeDt = null; procResultCd = null;
                                procDt = null; linkUrl = null;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            Collections.sort(results, (o1, o2) -> o2.getbNum() - o1.getbNum());

            mainHandler.post(() -> {
                for (Bill_Data data : results) {
                    bill_Adapter.addItem(data);
                }
                bill_Adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
