package com.example.lib_upload_qiniu;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class Uploader {
    private Configuration config;

    private volatile Map<String, UploadHolder> uploadCache;

    private Uploader() {
        uploadCache = new HashMap<>();
        //断点上传
        String dirPath = "/storage/emulated/0/Download";
        Recorder recorder = null;
        try {
            File f = File.createTempFile("qiniu_xxxx", ".tmp");
            Log.d("qiniu", f.getAbsolutePath().toString());
            dirPath = f.getParent();
            recorder = new FileRecorder(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String dirPath1 = dirPath;
        //默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
        //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
        KeyGenerator keyGen = new KeyGenerator() {
            public String gen(String key, File file) {
                // 不必使用url_safe_base64转换，uploadManager内部会处理
                // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                String path = key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                Log.d("qiniu", path);
                File f = new File(dirPath1, UrlSafeBase64.encodeToString(path));
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(f));
                    String tempString = null;
                    int line = 1;
                    try {
                        while ((tempString = reader.readLine()) != null) {
                            Log.d("qiniu", "line " + line + ": " + tempString);
                            line++;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return path;
            }
        };

        // recorder 分片上传时，已上传片记录器
        // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
        config = new Configuration.Builder()
                // recorder 分片上传时，已上传片记录器
                // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .recorder(recorder, keyGen)
                .build();
    }

    private static final class Holder {
        private static final Uploader instance = new Uploader();
    }

    public static Uploader getInstance(){
        return Holder.instance;
    }

    /**
     * 取消所有上传
     */
    public void cancleAll(){
        Set<String> paths = uploadCache.keySet();
        for (String path : paths) {
            cancle(path);
        }
    }

    /**
     * 取消上传
     * @param path 上传文件的路径
     */
    private void cancle(String path) {
        UploadHolder uploadHolder = Holder.instance.uploadCache.get(path);
        if (uploadHolder != null) {
            uploadHolder.setStatus(UploadEnum.CANCELED);
        }
    }

    /**
     * 上传 -- 已经实现了批量上传-->每次上传,都单独实现一个UploadManager
     *
     * @param path  上传文件的路径
     * @param token 七牛token
     * @param key   文件的key--产生的文件的名称,
     *              如果服务器在产生token的时候对文件进行了命名,
     *              就需要将命名一起传入进来,
     */
    public UploadHolder upload(final String path, String token, String key, final Map<String, String> extra, final UploadListener listener) {
        final UploadHolder holder = new UploadHolder(path, UploadEnum.UPLOADING);
        putUploadHolder(path, holder);
        UploadManager uploadManager = new UploadManager(config);
        uploadManager
                .put(path,
                        key,
                        token,
                        new UpCompletionHandler() {
                            public void complete(String key,
                                                 ResponseInfo info, JSONObject res) {
                                if (info.isOK() == true) {
                                    //上传完成
                                    if (listener != null) {
                                        listener.onComplete(path, info, res);
                                    }
                                } else {
                                    //上传失败
                                    listener.onError(info.statusCode, info.error);
                                }
                                Holder.instance.removeUploadHolder(path);
                            }
                        },
                        new UploadOptions(extra, null, false,

                                new UpProgressHandler() {
                                    //更新进度
                                    public void progress(String key, double percent) {
                                        int progress = (int) (percent * 100);
                                        if (listener != null) {
                                            listener.onProgress(path, progress);
                                        }
                                    }
                                },

                                new UpCancellationSignal() {
                                    @Override
                                    public boolean isCancelled() {
                                        boolean cancled = holder.getStatus() == UploadEnum.CANCELED;
                                        if (cancled) {
                                            //是否被取消
                                            if (listener != null) {
                                                listener.onCancled(path);
                                            }
                                            removeUploadHolder(path);
                                        }
                                        return cancled;
                                    }
                                }));

        return holder;
    }



    private void putUploadHolder(String path, UploadHolder holder) {
        Holder.instance.uploadCache.put(path, holder);
    }

    private void removeUploadHolder(String path) {
        Holder.instance.uploadCache.remove(path);
    }


}
