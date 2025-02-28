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

public class Budget_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Budget_Data> list = new ArrayList<>();

    public Budget_Adapter(ArrayList<Budget_Data> items){
        this.list = items;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    } //어댑터1개 액티비티2개 사용하기위해 viewtype 선언

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { //viewType = 0 이면 budget_info 에 리사이클러뷰 나타남
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_info_recycler, parent, false));
        } else //viewType = 1 이면 budget_info_Activity 에 리사이클러뷰 나타남
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_info_activity_recycler, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) { // viewType이 0이면 ViewHolder1를 사용하여 내용을 채움
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.onBind1(list.get(position));

        } else { // viewType이 1이면 ViewHolder2를 사용하여 내용을 채움
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.onBind2(list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    class ViewHolder1 extends RecyclerView.ViewHolder { // budget_info_recycler.xml 있는 요소들
        TextView title;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.budget_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPos = getAdapterPosition(); // click pos 가져옴

                    Intent intent = new Intent(view.getContext(), Budget_Activity.class);
                    intent.putExtra("title", list.get(currentPos).getSUBJECT());
                    intent.putExtra("date", list.get(currentPos).getREG_DATE());
                    intent.putExtra("ainame", list.get(currentPos).getDEPARTMENT_NAME());
                    intent.putExtra("link", list.get(currentPos).getLINK_URL());
                    intent.putExtra("view", list.get(currentPos).getViewType());

                    view.getContext().startActivity(intent);
                }
            });

        }

        void onBind1(Budget_Data item) { //데이터 표시 메소드
            title.setText(item.getSUBJECT());
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder { // budget_info_activity_recycler.xml 있는 요소들
        TextView title, date, ainame;
        Button link;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.budget_activity_Title);
            ainame = (TextView) itemView.findViewById(R.id.budget_activity_aiName);
            link = (Button) itemView.findViewById(R.id.budget_activity_recyclerBtn);
            date = (TextView) itemView.findViewById(R.id.budget_activity_dateInfo);

        }

        void onBind2(Budget_Data item) {
            title.setText(item.getSUBJECT());
            ainame.setText(item.getDEPARTMENT_NAME());
            date.setText(item.getREG_DATE());
            link.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLINK_URL()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }

    public void setArrayList(ArrayList<Budget_Data> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
