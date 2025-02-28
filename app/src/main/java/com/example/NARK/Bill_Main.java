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
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class Bill_Main extends AppCompatActivity {
    static List<String> listBillName = new ArrayList<>();
    static List<Integer> listBillNo = new ArrayList<>();
    static List<String> listProposeDt = new ArrayList<>();
    static List<String> listProposeKind = new ArrayList<>();
    static List<String> listProposer = new ArrayList<>();
    static List<String> listCurrComittee = new ArrayList<>();
    static List<String> listAge = new ArrayList<>();
    static List<String> listCommiteeDt = new ArrayList<>();
    static List<String> listProcResultCd = new ArrayList<>();
    static List<String> listProcDt = new ArrayList<>();
    static List<String> listURL = new ArrayList<>();
    static ArrayList<Integer> listViewTypeE = new ArrayList<Integer>();

    ArrayList<Bill_Data> userList = new ArrayList<>();

    RecyclerView recyclerView;
    bill_Adapter bill_Adapter;

    int index_num;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listBillName.clear();
        listBillNo.clear();
        listProposeDt.clear();
        listProposeKind.clear();
        listProposer.clear();
        listCurrComittee.clear();
        listAge.clear();
        listCommiteeDt.clear();
        listProcResultCd.clear();
        listProcDt.clear();
        listURL.clear();
        listViewTypeE.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_main);

        Toolbar toolbar = findViewById(R.id.bill_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.bill_recyclerView);
        bill_Adapter = new bill_Adapter();

        recyclerView.setAdapter(bill_Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parsing(bill_Adapter,index_num = 1, str);

        bill_Adapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    listBillName.clear();
                    listBillNo.clear();
                    listProposeDt.clear();
                    listProposeKind.clear();
                    listProposer.clear();
                    listCurrComittee.clear();
                    listAge.clear();
                    listCommiteeDt.clear();
                    listProcResultCd.clear();
                    listProcDt.clear();
                    listURL.clear();
                    listViewTypeE.clear();

                    ++index_num;
                    parsing(bill_Adapter, index_num, str);

                    recyclerView.post(new Runnable() {
                        public void run() {
                            bill_Adapter.notifyDataSetChanged();
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

        MenuItem menuSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listBillName.clear();
                listBillNo.clear();
                listProposeDt.clear();
                listProposeKind.clear();
                listProposer.clear();
                listCurrComittee.clear();
                listAge.clear();
                listCommiteeDt.clear();
                listProcResultCd.clear();
                listProcDt.clear();
                listURL.clear();
                listViewTypeE.clear();
                bill_Adapter.Bill_clearList();
                str = "&BILL_NAME=" + s;

                parsing(bill_Adapter,index_num = 1, str);
                recyclerView.setAdapter(bill_Adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    listBillName.clear();
                    listBillNo.clear();
                    listProposeDt.clear();
                    listProposeKind.clear();
                    listProposer.clear();
                    listCurrComittee.clear();
                    listAge.clear();
                    listCommiteeDt.clear();
                    listProcResultCd.clear();
                    listProcDt.clear();
                    listURL.clear();
                    listViewTypeE.clear();
                    bill_Adapter.Bill_clearList();

                    parsing(bill_Adapter,index_num = 1, str = "");
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

    public void parsing(bill_Adapter adapter, int index_num,String searchText) { // 파싱 및 데이터 추가 함수
        // 법안명, 법안 번호, 제안일, 제안자, 제안자 구분, 소관위(담당기관), 대, 위원회 회부일, 처리결과, 처리일
        boolean inrow = false, billName = false, billNo = false, proposeDt = false, proposer = false, proposeKind = false, currComittee = false, age = false, commiteeDt = false, procResultCd = false, procDt = false, linkUrl = false;
        String BILL_NAME = null, BILL_NO = null, PROPOSE_DT = null, PROPOSER = null, PROPOSER_KIND = null, CURR_COMMITTEE = null, AGE = null, COMMITTEE_DT = null, PROC_RESULT_CD = null, PROC_DT = null, LINK_URL = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("https://open.assembly.go.kr/portal/openapi/TVBPMBILL11?"
                    + "KEY="
                    + "8081b2b1c98c4e6d83be3454f1dfd506" // 키값
                    + "&pIndex=" + index_num
                    + "&pSize=20"
                    + "&Type=xml"
                    + searchText
            );
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("BILL_NAME")) { //BILL_NAME 만나면 내용을 받을수 있게 하자 -> 법안명
                            billName = true;
                        }
                        if (parser.getName().equals("BILL_NO")) { //BILL_NO 만나면 내용을 받을수 있게 하자 -> 법안번호
                            billNo = true;
                        }
                        if (parser.getName().equals("PROPOSE_DT")) { //PROPOSE_DT 만나면 내용을 받을수 있게 하자 -> 제안일
                            proposeDt = true;
                        }
                        if (parser.getName().equals("PROPOSER")) { //PROPOSER 만나면 내용을 받을수 있게 하자 -> 제안자
                            proposer = true;
                        }
                        if (parser.getName().equals("PROPOSER_KIND")) { //PROPOSER_KIND 만나면 내용을 받을수 있게 하자 -> 제안자구분
                            proposeKind = true;
                        }
                        if (parser.getName().equals("CURR_COMMITTEE")) { //CURR_COMMITTEE 만나면 내용을 받을수 있게 하자 -> 소관위(담당기관)
                            currComittee = true;
                        }
                        if (parser.getName().equals("AGE")) { //AGE 만나면 내용을 받을수 있게 하자 -> 대
                            age = true;
                        }
                        if (parser.getName().equals("COMMITTEE_DT")) { //COMMITTEE_DT 만나면 내용을 받을수 있게 하자 -> 위원회 회부일
                            commiteeDt = true;
                        }
                        if (parser.getName().equals("PROC_RESULT_CD")) { //PROC_RESULT_CD 만나면 내용을 받을수 있게 하자 -> 처리결과
                            procResultCd = true;
                        }
                        if (parser.getName().equals("PROC_DT")) { //PROC_DT 만나면 내용을 받을수 있게 하자 -> 처리일
                            procDt = true;
                        }
                        if (parser.getName().equals("LINK_URL")) { //PROC_DT 만나면 내용을 받을수 있게 하자 -> 처리일
                            linkUrl = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (billName) { // billName이 true일 때 태그의 내용을 저장.
                            BILL_NAME = parser.getText();
                            listBillName.add(BILL_NAME);
                            listViewTypeE.add(0);
                            billName = false;
                        }
                        if (billNo) { // billNo true일 때 태그의 내용을 저장.
                            BILL_NO = parser.getText();
                            int INT_BILL_NO = Integer.parseInt(BILL_NO);
                            listBillNo.add(INT_BILL_NO);
                            billNo = false;
                        }
                        if (proposeDt) { // proposeDt true일 때 태그의 내용을 저장.
                            PROPOSE_DT = parser.getText();
                            listProposeDt.add(PROPOSE_DT);
                            proposeDt = false;
                        }
                        if (proposer) { // proposer true일 때 태그의 내용을 저장.
                            PROPOSER = parser.getText();
                            listProposer.add(PROPOSER);
                            proposer = false;
                        }
                        if (proposeKind) { // proposeKind true일 때 태그의 내용을 저장.
                            PROPOSER_KIND = parser.getText();
                            listProposeKind.add(PROPOSER_KIND);
                            proposeKind = false;
                        }
                        if (currComittee) {// currComittee true일 때 태그의 내용을 저장.
                            CURR_COMMITTEE = parser.getText();
                            listCurrComittee.add(CURR_COMMITTEE);
                            currComittee = false;
                        }
                        if (age) { // age true일 때 태그의 내용을 저장.
                            AGE = parser.getText();
                            listAge.add(AGE);
                            age = false;
                        }
                        if (commiteeDt) { // commiteeDt true일 때 태그의 내용을 저장.
                            COMMITTEE_DT = parser.getText();
                            listCommiteeDt.add(COMMITTEE_DT);
                            commiteeDt = false;
                        }
                        if (procResultCd) { // procResultCd true일 때 태그의 내용을 저장.
                            PROC_RESULT_CD = parser.getText();
                            listProcResultCd.add(PROC_RESULT_CD);
                            procResultCd = false;
                        }
                        if (procDt) { // procDt true일 때 태그의 내용을 저장.
                            PROC_DT = parser.getText();
                            listProcDt.add(PROC_DT);
                            procDt = false;
                        }
                        if (linkUrl) { // linkUrl true일 때 태그의 내용을 저장.
                            LINK_URL = parser.getText();
                            listURL.add(LINK_URL);
                            linkUrl = false;
                        }
                        ;
                        break;
                    case XmlPullParser.END_TAG:
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
        ArrayList<Bill_Data> userList = new ArrayList<>();
        int i;
        for (i = 0; i < listBillName.size(); i++) {
            userList.add(new Bill_Data(listBillName.get(i), listBillNo.get(i), listProposeDt.get(i)
                    , listProposer.get(i), listProposeKind.get(i), listCurrComittee.get(i), listAge.get(i)
                    , listCommiteeDt.get(i), listProcResultCd.get(i), listProcDt.get(i), listURL.get(i), listViewTypeE.get(i)));

            userList.get(i).setbName(listBillName.get(i));
            userList.get(i).setbNum(listBillNo.get(i));
            userList.get(i).setbDate(listProposeDt.get(i));
            userList.get(i).setbPrp(listProposer.get(i));
            userList.get(i).setbPrep(listProposeKind.get(i));
            userList.get(i).setbRa(listCurrComittee.get(i));
            userList.get(i).setbDae(listAge.get(i));
            userList.get(i).setbCrd(listCommiteeDt.get(i));
            userList.get(i).setbProResult(listProcResultCd.get(i));
            userList.get(i).setbProDate(listProcDt.get(i));
            userList.get(i).setbUrl(listURL.get(i));
            userList.get(i).setViewType(listViewTypeE.get(i));

            //정렬
            Collections.sort(userList, new Comparator<Bill_Data>() {
                @Override
                public int compare(Bill_Data o1, Bill_Data o2) {
                    return o2.getbNum() - o1.getbNum();
                }
            });
        }
        //어댑터 추가
        for(int j = 0; j < i; j++)
        {
            adapter.addItem(userList.get(j));
        }
    }
}
