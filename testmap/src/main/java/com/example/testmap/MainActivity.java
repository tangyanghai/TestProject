package com.example.testmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.example.testmap.adapter.MapAdapter;
import com.example.testmap.map.MapBean;
import com.example.testmap.map.MapManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MapBean> mLstMap;
    EditText mEtAddress;
    RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtAddress = findViewById(R.id.et_address);
        mLstMap = MapManager.getInstance().getInstalledMaps(this);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new MapAdapter(mLstMap, new IAddressGetter() {
            @Override
            public String getAddress() {
                Editable text = mEtAddress.getText();
                if (text == null) {
                    return null;
                }
                return text.toString();
            }
        }));
    }

    public interface IAddressGetter {
        String getAddress();
    }

}
