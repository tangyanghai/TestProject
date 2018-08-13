package com.example.testconstraintlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.widget.LockPatternUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/10</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class InfoActivity extends AppCompatActivity {

    TextView info;

    public static void start(Context context, String name, String no) {
        if (TextUtils.isEmpty(no)) {
            Toast.makeText(context, "单号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent in = new Intent(context, InfoActivity.class);
        if (!TextUtils.isEmpty(name)) {
            in.putExtra("name", name);
        }
        in.putExtra("no", no);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        info = findViewById(R.id.tv_info);
        initData();
    }

    private void initData() {
        Intent in = getIntent();
        String name = in.getStringExtra("name");
        String no = in.getStringExtra("no");
        loadInfo(name, no);
    }

    private void loadInfo(String name, String no) {
        new KdniaoTrackQueryAPI("SF", "456778851265", new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                info.setText(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public class KdniaoTrackQueryAPI {

        //电商ID
        private String EBusinessID="1370220";
        //电商加密私钥，快递鸟提供，注意保管，不要泄漏
        private String AppKey="4bd7b1cd-6bc1-465a-a495-a4cb55e6fe89";
        //请求url
        private String ReqURL="http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";


        public KdniaoTrackQueryAPI(String expCode, String expNo,Callback.CommonCallback<String> callback) {
            try {
                getOrderTracesByJson(expCode,expNo,callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Json方式 查询订单物流轨迹
         * @throws Exception
         */
        public void getOrderTracesByJson(String expCode, String expNo,Callback.CommonCallback<String> callback) throws Exception{
            String requestData= "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

            Map<String, String> params = new HashMap<String, String>();
            params.put("RequestData", urlEncoder(requestData, "UTF-8"));
            params.put("EBusinessID", EBusinessID);
            params.put("RequestType", "1002");
            String dataSign=encrypt(requestData, AppKey, "UTF-8");
            params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
            params.put("DataType", "2");


            //根据公司业务处理返回的信息......

            RequestParams entity = new RequestParams(ReqURL);
            entity.addParameter("RequestData", urlEncoder(requestData, "UTF-8"));
            entity.addParameter("EBusinessID", EBusinessID);
            entity.addParameter("RequestType", "1002");
            entity.addParameter("DataSign", urlEncoder(dataSign, "UTF-8"));
            entity.addParameter("DataType", "2");
            x.http().post(entity, callback);
        }

        /**
         * MD5加密
         * @param str 内容
         * @param charset 编码方式
         * @throws Exception
         */
        @SuppressWarnings("unused")
        private String MD5(String str, String charset) throws Exception {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(charset));
            byte[] result = md.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int i = 0; i < result.length; i++) {
                int val = result[i] & 0xff;
                if (val <= 0xf) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString().toLowerCase();
        }

        /**
         * base64编码
         * @param str 内容
         * @param charset 编码方式
         * @throws UnsupportedEncodingException
         */
        private String base64(String str, String charset) throws UnsupportedEncodingException{
            String encoded = base64Encode(str.getBytes(charset));
            return encoded;
        }

        @SuppressWarnings("unused")
        private String urlEncoder(String str, String charset) throws UnsupportedEncodingException{
            String result = URLEncoder.encode(str, charset);
            return result;
        }

        /**
         * 电商Sign签名生成
         * @param content 内容
         * @param keyValue Appkey
         * @param charset 编码方式
         * @throws UnsupportedEncodingException ,Exception
         * @return DataSign签名
         */
        @SuppressWarnings("unused")
        private String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
        {
            if (keyValue != null)
            {
                return base64(MD5(content + keyValue, charset), charset);
            }
            return base64(MD5(content, charset), charset);
        }

        /**
         * 向指定 URL 发送POST方法的请求
         * @param url 发送请求的 URL
         * @param params 请求的参数集合
         * @return 远程资源的响应结果
         */
        @SuppressWarnings("unused")
        private String sendPost(String url, Map<String, String> params) {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            StringBuilder result = new StringBuilder();
            try {
                URL realUrl = new URL(url);
                HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // POST方法
                conn.setRequestMethod("POST");
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.connect();
                // 获取URLConnection对象对应的输出流
                out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                // 发送请求参数
                if (params != null) {
                    StringBuilder param = new StringBuilder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if(param.length()>0){
                            param.append("&");
                        }
                        param.append(entry.getKey());
                        param.append("=");
                        param.append(entry.getValue());
                        //System.out.println(entry.getKey()+":"+entry.getValue());
                    }
                    //System.out.println("param:"+param.toString());
                    out.write(param.toString());
                }
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
            finally{
                try{
                    if(out!=null){
                        out.close();
                    }
                    if(in!=null){
                        in.close();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return result.toString();
        }


        private  char[] base64EncodeChars = new char[] {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9', '+', '/' };

        public  String base64Encode(byte[] data) {
            StringBuffer sb = new StringBuffer();
            int len = data.length;
            int i = 0;
            int b1, b2, b3;
            while (i < len) {
                b1 = data[i++] & 0xff;
                if (i == len)
                {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                    sb.append("==");
                    break;
                }
                b2 = data[i++] & 0xff;
                if (i == len)
                {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                    sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                    sb.append("=");
                    break;
                }
                b3 = data[i++] & 0xff;
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
                sb.append(base64EncodeChars[b3 & 0x3f]);
            }
            return sb.toString();
        }
    }


}
