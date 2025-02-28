package com.example.NARK;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Mp_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Mp_DataModel> humanList = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        return humanList.get(position).getViewType();
    } //어댑터1개 액티비티2개 사용하기위해 viewtype 선언

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {//viewType = 0 이면 mp_info_main 에 리사이클러뷰 나타남
            return new  Mp_ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_info_recycler, parent, false));
        }
        else //viewType = 1 이면 mp_info_Activity 에 리사이클러뷰 나타남
            return new Mp_ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_info_activity_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0) { // viewType이 0이면 Mp_ViewHolder1를 사용하여 내용을 채움
            Mp_ViewHolder1 viewHolder1 = (Mp_ViewHolder1) holder;
            viewHolder1.Mp_OnBind1(humanList.get(position));
        }
        else { // viewType이 1이면 Mp_ViewHolder2를 사용하여 내용을 채움\
            Mp_ViewHolder2 viewHolder2 = (Mp_ViewHolder2) holder;
            viewHolder2.Mp_OnBind2(humanList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return humanList.size();
    }

    class Mp_ViewHolder1 extends RecyclerView.ViewHolder { // mp_info_recycler.xml 있는 요소들
        ImageView imgUrl;
        TextView name, pp;

        public Mp_ViewHolder1(@NonNull View itemView) {
            super(itemView);

            imgUrl = (ImageView) itemView.findViewById(R.id.mp_image);
            name = (TextView) itemView.findViewById(R.id.mp_info_sub_text);
            pp = (TextView) itemView.findViewById(R.id.mp_info_sub_text2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPos = getAdapterPosition(); // click pos 가져옴 -> 클릭하면 현재 위치를 가져옴  EX) 강대식을 누르면 강대식은 1번에있다

                    // intent를 사용하여 다른 액티비티로 값을 넘기는 역활 
                    Intent intent = new Intent(view.getContext(), Mp_Info_Activity.class);
                    intent.putExtra("name", humanList.get(currentPos).getName());
                    intent.putExtra("pp", humanList.get(currentPos).getPp());
                    intent.putExtra("phoneNum", humanList.get(currentPos).getPhoneNum());
                    intent.putExtra("email", humanList.get(currentPos).getEmail());
                    intent.putExtra("bf", humanList.get(currentPos).getBf());
                    intent.putExtra("img", humanList.get(currentPos).getImgUrl());
                    intent.putExtra("view", humanList.get(currentPos).getViewType());

                    view.getContext().startActivity(intent);
                }
            });

        }

        void Mp_OnBind1(Mp_DataModel item) { //데이터 표시 메소드
            Glide.with(itemView.getContext()).load(item.getImgUrl()).override(300,250).into(imgUrl);
            name.setText(item.getName());
            pp.setText(item.getPp());
        }
    }

    class Mp_ViewHolder2 extends RecyclerView.ViewHolder { // mp_info_activity_recycler.xml 있는 요소들
        ImageView imgUrl;
        TextView name, pp, phoneNum, email, bf;

        public Mp_ViewHolder2(@NonNull View itemView) {
            super(itemView);

            imgUrl = (ImageView) itemView.findViewById(R.id.mp_activity_image);
            name = (TextView) itemView.findViewById(R.id.mp_activity_name);
            pp = (TextView) itemView.findViewById(R.id.mp_activity_PP);
            phoneNum = (TextView) itemView.findViewById(R.id.mp_activity_phoneNum);
            email = (TextView) itemView.findViewById(R.id.mp_activity_email);
            bf = (TextView) itemView.findViewById(R.id.mp_activity_BF);

        }

        void Mp_OnBind2(Mp_DataModel item) { // 데이터 표시 메소드
            Glide.with(itemView.getContext()).load(item.getImgUrl()).preload();
            Glide.with(itemView.getContext()).load(item.getImgUrl()).into(imgUrl);
            name.setText(item.getName());
            pp.setText(item.getPp());
            phoneNum.setText(item.getPhoneNum());
            email.setText(item.getEmail());
            bf.setText(item.getBf());
        }
    }

    public void Mp_addITem(Mp_DataModel Data) {
        humanList.add(Data);
    } //파싱한 데이터를 humanlist에 저장

    //검색용 필터
    public void  Mp_filterList(Mp_DataModel Data) {
        humanList.add(Data);
        notifyDataSetChanged();
    }

    //리사이클러뷰 humanList를 초기화 그냥 날려버림 -> 만든이유 검색할때 text입력하면 무조건 작동됨 따라서 humanList를 초기화 안해주면 값이 계속 누적됨
    public void Mp_clearList() {
        humanList.clear();
    }

}
