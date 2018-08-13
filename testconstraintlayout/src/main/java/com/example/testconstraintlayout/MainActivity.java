package com.example.testconstraintlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText mEtName;
    EditText mEtNo;
    Button mBtQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtName = findViewById(R.id.et_name);
        mEtNo= findViewById(R.id.et_no);
        mBtQuery = findViewById(R.id.bt_query);

        mBtQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_query:
                InfoActivity.start(this,mEtName.getText().toString(),mEtNo.getText().toString());
                break;
        }
    }
}
