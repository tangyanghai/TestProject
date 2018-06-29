package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @author : tyh
 * @time : 2018/06/13
 * @for :
 */
public class TestDialogActivity extends AppCompatActivity {
    private static final String TAG = "==TestDialogActivity==";
    private Dialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
               /* Log.i(TAG, "handleMessage: 关闭dialog");
                dialog.dismiss();*/
                if (dialog.getContext() instanceof Activity) {
                    if (!((Activity) dialog.getContext()).isFinishing()) {
                        Log.i(TAG, "handleMessage: 关闭dialog");
                        dialog.dismiss();
                    }else {
                        Log.i(TAG, "handleMessage: 延时关闭dialog时,页面已经关闭");
                    }
                }
            } else if (msg.what == 3) {
                Log.i(TAG, "handleMessage: 延时关闭了页面");
                finish();
            } else {
                showDialog();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);
    }

    public void showDialog(View view) {
        showDialog();
    }

    private void showDialog() {
        if (!isFinishing()) {
            dialog = new Dialog(this);
            dialog.setTitle("测试dialog");
            dialog.setContentView(R.layout.dialog_test);
            Button btn = dialog.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    handler.sendEmptyMessageDelayed(0, 1_000);
                }
            });
            Log.i(TAG, "showDialog: 打开dialog");
            dialog.show();
        } else {
            Log.i(TAG, "showDialog: 页面已经关闭了,不能再打开了");
        }
    }

    public void showDialogAfterFinish(View view) {
        finish();
        handler.sendEmptyMessageDelayed(1, 1_000);
    }

    public void showDialogAfterOpenNewOne(View view) {
        startActivity(new Intent(this, TestDialogActivity.class));
        handler.sendEmptyMessageDelayed(2, 1_000);
        handler.sendEmptyMessageDelayed(3, 4_000);
    }

    public void startNewListActivity(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}
