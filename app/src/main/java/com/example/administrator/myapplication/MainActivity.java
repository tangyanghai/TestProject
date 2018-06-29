package com.example.administrator.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "====MainActivity===";
    RecyclerView mRv1;
    RecyclerView mRv2;
    List<Integer> list1;
    List<Integer> list2;
    Adapter adapter1;
    Adapter adapter2;
    Handler handler;
    int count = 11111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list1.add(i);
            list2.add(i);
        }
        adapter1 = new Adapter(list1, "第一个");
        adapter2 = new Adapter(list2, "第二个");
        mRv1 = findViewById(R.id.rv1);
        mRv2 = findViewById(R.id.rv2);
        mRv1.setLayoutManager(new LinearLayoutManager(this));
        mRv2.setLayoutManager(new LinearLayoutManager(this));
        mRv1.setAdapter(adapter1);
        mRv2.setAdapter(adapter2);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v instanceof TextView) {
//                    showDialog(((TextView) v).getText().toString(), true);
//                }
                startActivity(new Intent(MainActivity.this,TestDialogActivity.class));
            }
        };
        adapter1.setOnClickListener(onClickListener);
        adapter2.setOnClickListener(onClickListener);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Log.i(TAG, "handleMessage: 接收到消息" + (count - 11111));
                    list1.set(10, count);
                    list2.set(10, count);
                    RecyclerView.ViewHolder holder1 = mRv1.findViewHolderForLayoutPosition(10);
                    TextView tv1 = holder1.itemView.findViewById(R.id.tv);
                    tv1.setText("" + count);
                    RecyclerView.ViewHolder holder2 = mRv2.findViewHolderForLayoutPosition(10);
                    TextView tv2 = holder2.itemView.findViewById(R.id.tv);
                    tv2.setText("" + count);
                    count++;
                    handler.sendEmptyMessageDelayed(1, 2500);
                } else if (msg.what == 3){
                    showDialog("新开的dialog");
                }else {
                    dialog1.dismiss();
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 2500);
    }


    class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Integer> list;
        String mark;

        public Adapter(List<Integer> list, String mark) {
            this.list = list;
            this.mark = mark;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView mTv = holder.itemView.findViewById(R.id.tv);
            mTv.setText(list.get(position) + " " + mark);
            mTv.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        View.OnClickListener onClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }


    public void showDialog(String title) {
        showDialog(title, false);
    }

    Dialog dialog1;
    Dialog dialog2;

    public void showDialog(String title, boolean sendMsg) {

        if (sendMsg) {
            dialog1 = new Dialog(this);
            dialog1.setTitle(title);
            dialog1.setContentView(R.layout.dialog_test);
            dialog1.show();
        }else {
            dialog2 = new Dialog(this);
            dialog2.setTitle(title);
            dialog2.setContentView(R.layout.dialog_test);
            dialog2.show();
            handler.sendEmptyMessageDelayed(4, 2_000);

        }
        if (sendMsg) {
            handler.sendEmptyMessageDelayed(3, 2_000);
        }
    }

}
