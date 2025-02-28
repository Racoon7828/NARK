package com.example.NARK;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class bill_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Bill_Data> billList = new ArrayList<>();

    public int getItemViewType(int position) {
        return billList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) { //viewType = 0 이면 bill_recycler 에 리사이클러뷰 나타남
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_recycler, parent, false));
        }
        else if(viewType == 1) //viewType = 1 이면 bill_popup_recycler 에 리사이클러뷰 나타남
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_popup_recycler, parent, false));
        else //viewType = 나머지는 bill_activity_recycler 에 리사이클러뷰 나타남
            return new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_activity_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0) { // viewType이 0이면 ViewHolder1를 사용하여 내용을 채움
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.onBind1(billList.get(position));
        }
        else if (getItemViewType(position) == 1) { // viewType이 1이면 ViewHolder2를 사용하여 내용을 채움
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.onBind2(billList.get(position));
        }
        else  { // 나머지는 ViewHolder3를 사용하여 내용을 채움
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            viewHolder3.onBind3(billList.get(position));
        }
    }

    @Override
    public int getItemCount() { return billList.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder  { //
        TextView name, bNum;
        Button btn_popup; // 간단정보 버튼

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bill_recyclerText);
            btn_popup = (Button) itemView.findViewById(R.id.bill_recyclerBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int currentPos = getAdapterPosition(); // click pos 가져옴

                    Intent intent = new Intent(view.getContext(), Bill_Activity.class);
                    intent.putExtra("name", billList.get(currentPos).getbName());
                    intent.putExtra("num", billList.get(currentPos).getbNum());
                    intent.putExtra("date", billList.get(currentPos).getbDate());
                    intent.putExtra("prp", billList.get(currentPos).getbPrp());
                    intent.putExtra("prep", billList.get(currentPos).getbPrep());
                    intent.putExtra("ra", billList.get(currentPos).getbRa());
                    intent.putExtra("dae", billList.get(currentPos).getbDae());
                    intent.putExtra("crd", billList.get(currentPos).getbCrd());
                    intent.putExtra("proResult", billList.get(currentPos).getbProResult());
                    intent.putExtra("proDate", billList.get(currentPos).getbProDate());
                    intent.putExtra("url", billList.get(currentPos).getbUrl());
                    intent.putExtra("view", billList.get(currentPos).getViewType());
                    view.getContext().startActivity(intent);
                }
            });
        }

        void onBind1(Bill_Data item) { //데이터 표시 메소드
            name.setText(item.getbName());
            btn_popup.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View view) { // 간단정보 버튼 클릭시 가져옴
                    int currentPos = getAdapterPosition(); // click pos 가져옴
                    Intent intent = new Intent(view.getContext(), Bill_Popup.class);
                    intent.putExtra("name", billList.get(currentPos).getbName());
                    intent.putExtra("date", billList.get(currentPos).getbDate());
                    intent.putExtra("prp", billList.get(currentPos).getbPrp());
                    intent.putExtra("url", billList.get(currentPos).getbUrl());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView name, date, prp;
        Button url;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bill_popup_rname);
            date = (TextView) itemView.findViewById(R.id.bill_popup_date);
            prp = (TextView) itemView.findViewById(R.id.bill_popup_prp);
            url = (Button) itemView.findViewById(R.id.bill_popup_process_btn);
        }

        void onBind2(Bill_Data item) { //데이터 표시 메소드
            name.setText(item.getbName());
            date.setText(item.getbDate());
            prp.setText(item.getbPrp());
            url.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getbUrl()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    class ViewHolder3 extends RecyclerView.ViewHolder {
        TextView name,num,date,prp,prep,ra,dae,crd,proResult,proDate;
        Button url;

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bill_activity_rname);
            num = (TextView) itemView.findViewById(R.id.bill_activity_num);
            date = (TextView) itemView.findViewById(R.id.bill_activity_date);
            prp = (TextView) itemView.findViewById(R.id.bill_activity_prp);
            prep = (TextView) itemView.findViewById(R.id.bill_activity_prpsep);
            ra = (TextView) itemView.findViewById(R.id.bill_activity_RA);
            dae = (TextView) itemView.findViewById(R.id.bill_activity_dae);
            crd = (TextView) itemView.findViewById(R.id.bill_activity_CRD);
            proResult = (TextView) itemView.findViewById(R.id.bill_activity_proResult);
            proDate = (TextView) itemView.findViewById(R.id.bill_activity_proDate);
            url = (Button) itemView.findViewById(R.id.bill_activity_btn);
        }

        void onBind3(Bill_Data item) { //데이터 표시 메소드
            name.setText(item.getbName());
            num.setText(item.getbNum().toString()); // Num번호가 Integer라서 setText가 안먹힘
            date.setText(item.getbDate());          // 따라서 문자열로 출력되도록 바꿧음
            prp.setText(item.getbPrp());
            prep.setText(item.getbPrep());
            ra.setText(item.getbRa());
            dae.setText(item.getbDae());
            crd.setText(item.getbCrd());
            proResult.setText(item.getbProResult());
            proDate.setText(item.getbProDate());
            url.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getbUrl()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public void addItem(Bill_Data Data) {billList.add(Data);}

    public void Bill_clearList() {
        billList.clear();
    }
}
