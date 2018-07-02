package com.example.lib_upload_qiniu;

import com.qiniu.android.http.ResponseInfo;

import org.json.JSONObject;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public interface UploadListener {
    void onComplete(String path, ResponseInfo info, JSONObject res);
    void onProgress(String path, int progress);
    void onCancled(String path);

    void onError(int statusCode, String error);
}
