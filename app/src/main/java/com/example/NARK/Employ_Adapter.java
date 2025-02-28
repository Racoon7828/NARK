package com.example.NARK;

import android.content.Context;
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


public class Employ_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Employ_Data> list;

    public Employ_Adapter(ArrayList<Employ_Data> itmes) {
        this.list = itmes;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    } //어댑터1개 액티비티2개 사용하기위해 viewtype 선언


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { //viewType = 0 이면 Employ_main 에 리사이클러뷰 나타남
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.employ_recycler, parent, false));
        } else  //viewType = 1 이면 Employ_Activity 에 리사이클러뷰 나타남
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.employ_activity_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0) { // viewType이 0이면 ViewHolder1를 사용하여 내용을 채움
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.onBind1(list.get(position));
        }
        else { // viewType이 1이면 ViewHolder2를 사용하여 내용을 채움
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.onBind2(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView title, date;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.employ_title_text);
            date = (TextView) itemView.findViewById(R.id.employ_year_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPos = getAdapterPosition(); // click pos 가져옴

                    Intent intent = new Intent(view.getContext(), Employ_Activity.class);
                    intent.putExtra("title", list.get(currentPos).getBRDI_SJ());
                    intent.putExtra("date", list.get(currentPos).getRDT());
                    intent.putExtra("content", list.get(currentPos).getBRDI_CN());
                    intent.putExtra("ainame", list.get(currentPos).getBLNG_INST_NM());
                    intent.putExtra("link", list.get(currentPos).getHOME_URL());
                    intent.putExtra("view", list.get(currentPos).getViewType());

                    view.getContext().startActivity(intent);
                }
            });

        }

        void onBind1(Employ_Data item) {
            title.setText(item.getBRDI_SJ());
            date.setText(item.getRDT());
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView title,date,content,ainame;
        Button link;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.employ_activity_Title);
            ainame = (TextView) itemView.findViewById(R.id.employ_activity_aiName);
            content = (TextView) itemView.findViewById(R.id.employ_activity_content);
            link = (Button) itemView.findViewById(R.id.employ_activity_recyclerBtn);
            date = (TextView) itemView.findViewById(R.id.employ_activity_dateInfo);

        }

        void onBind2(Employ_Data item) {
            title.setText(item.getBRDI_SJ());
            content.setText(item.getBRDI_CN());
            ainame.setText(item.getBLNG_INST_NM());
            date.setText(item.getRDT());
            link.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getHOME_URL()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setArrayList(ArrayList<Employ_Data> list){
        this.list = list;
        notifyDataSetChanged();
    }
}