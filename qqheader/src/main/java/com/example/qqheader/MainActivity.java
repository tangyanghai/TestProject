package com.example.qqheader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    QQHeaderLayout listView;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        List<Map<String,Object>> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("text","第"+i+"条");
            datas.add(map);
        }
        View header = LayoutInflater.from(this).inflate(R.layout.head_view,null);
        img = header.findViewById(R.id.img);
        listView.setImgView(img);
        listView.addHeaderView(header);
        listView.setAdapter(new SimpleAdapter(this,datas,R.layout.item_list,new String[]{"text"},new int[]{R.id.text}));
    }
}
