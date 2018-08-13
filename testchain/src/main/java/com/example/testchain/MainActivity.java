package com.example.testchain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtNo;
    Button mBtQuery;
    private Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtNo = findViewById(R.id.et_no);
        mBtQuery = findViewById(R.id.bt_query);
        mBtQuery.setOnClickListener(this);

        staff = new Staff();
        staff.addInterCepter(new Zuzhang());
        staff.addInterCepter(new PartManager());
        staff.addInterCepter(new ComManager());
    }

    private void qingjia(Staff staff, int day) {
        Response response = staff.qingjia(day);
        if (response != null) {
            if (response.real) {
                log(response.master + "批准了请假");
                showToast(response.master + "批准了请假");
                return;
            }
        }

        log("请假未批准");
        showToast("请假未批准");
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String s = mEtNo.getText().toString();
        try {
            int day = Integer.parseInt(s);
            if (day > 0) {
                qingjia(staff, day);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 员工
     */
    class Staff {

        Chain chain;

        public Staff() {
            chain = new Chain();
        }

        public Response qingjia(int day) {
            Request request = new Request();
            request.day = day;
            return chain.start(request);
        }

        /**
         * 添加拦截器
         */
        public void addInterCepter(InterCepter interCepter) {
            if (interCepter == null) {
                return;
            }
            chain.add(interCepter);
        }
    }


    /**
     * 拦截器
     */
    abstract class InterCepter {
        InterCepter nextInterCepter;

        public abstract Response chain(Chain chain);

        public void setNextInterCepter(InterCepter interCepter) {
            if (nextInterCepter == null) {
                nextInterCepter = interCepter;
            } else {
                nextInterCepter.setNextInterCepter(interCepter);
            }
        }
    }

    /**
     * 部门经理
     */
    public class PartManager extends InterCepter {
        @Override
        public Response chain(Chain chain) {
            Request request = chain.getRequest();
            if (request.day < 4) {
                return new Response("部门经理", true);
            }else {
                log("部门经理没有权限,交给下一个领导处理");
            }

            return chain.process(request);
        }
    }

    /**
     * 总经理
     */
    public class ComManager extends InterCepter {
        @Override
        public Response chain(Chain chain) {
            Request request = chain.getRequest();
            if (request.day <30) {
                return new Response("总经理", true);
            }else {
                log("总经理让你滚蛋");
            }

            return chain.process(request);
        }
    }

    /**
     * 组长
     */
    public class Zuzhang extends InterCepter {

        @Override
        public Response chain(Chain chain) {
            Request request = chain.getRequest();
            if (request.day < 2) {
                return new Response("组长", true);
            }else {
                log("组长没有权限,交给下一个领导处理");
            }

            return chain.process(request);
        }
    }

    public void log(String msg) {
        Log.e("===", msg);
    }

    /**
     * 责任链
     */
    class Chain {
        /**
         * 请求内容
         */
        Request request;

        /**
         * 所有第一个拦截器
         * 这里一直记录着第一个拦截器,
         * 每次重新开始请求的时候,都从第一个拦截器开始
         */
        InterCepter firstInterCepter;

        /**
         * 当前拦截器
         * 作为一个临时变量,
         * 每次请求的时候,
         * 都要重新赋值,一层一层传递
         */
        InterCepter currentInterCepter;

        public Request getRequest() {
            return request;
        }

        public Response start(Request request) {
            currentInterCepter = firstInterCepter;
            return getResponse(request);
        }


        /**
         * 将请求传递给下一个拦截器
         */
        public Response process(Request request) {
            if (currentInterCepter == null) {
                return null;
            }
            currentInterCepter = currentInterCepter.nextInterCepter;
            return getResponse(request);
        }

        @Nullable
        private Response getResponse(Request request) {
            this.request = request;
            if (currentInterCepter == null) {
                return null;
            }
            return currentInterCepter.chain(this);
        }

        public void add(InterCepter interCepter) {
            if (this.firstInterCepter == null) {
                this.firstInterCepter = interCepter;
                return;
            }

            this.firstInterCepter.setNextInterCepter(interCepter);
        }
    }

    /**
     * 请求--请求
     */
    class Request {
        //请假天数
        int day;
    }

    /**
     * 回应--批准或拒绝
     */
    class Response {
        boolean real;
        String master;

        public Response(String master, boolean real) {
            this.master = master;
            this.real = real;
        }
    }

}
