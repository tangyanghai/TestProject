package com.example.testtextviewhtml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String test = "网站直接雇佣师傅、企业服务商，企业服务商直接雇佣师傅，将输入的地址改成分段录入网站直接雇佣师傅、企业服务商，企业服务商直接雇佣师傅，将输入的地址改成分段录入网站直接雇佣师傅、企业服务商，企业服务商直接雇佣师傅，将输入的地址改成分段录入网站直接雇佣师傅、企业服务商，企业服务商直接雇佣师傅，将输入的地址改成分段录入网站直接雇佣师傅、企业服务商，<font color='#ff0000'>企业服务商直接雇佣师傅，将输入</font>";

        TextView black = findViewById(R.id.tv_black);
        black.setText(Html.fromHtml(test));
        TextView gray = findViewById(R.id.tv_gray);
        gray.setText(Html.fromHtml(test));
    }
}
