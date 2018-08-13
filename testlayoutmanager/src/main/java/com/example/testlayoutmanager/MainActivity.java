package com.example.testlayoutmanager;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> list;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        resetData();
        final RecyclerView recyclerView = findViewById(R.id.rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new FlowLayoutManager());
        recyclerView.setLayoutManager(new CardLayoutManager());
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetData();
                adapter.notifyDataSetChanged();
            }
        },5000);
    }

    private void resetData() {
        list.clear();
        for (int j = 0; j < 10; j++) {
//            int length = (int) (Math.random()*25+3);
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < length; i++) {
//                buffer.append("a");
//            }
//            String content = buffer.append(j).toString();
            list.add("a"+j);
        }
    }

    class H extends RecyclerView.ViewHolder {
        public H(View itemView) {
            super(itemView);
        }
    }

    class Adapter extends RecyclerView.Adapter<H> {

        @NonNull
        @Override
        public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new H(LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull H holder, int position) {
            TextView tv = holder.itemView.findViewById(R.id.tv);
            tv.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}
