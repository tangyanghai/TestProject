package com.example.lib_upload_qiniu;

import android.text.TextUtils;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class UploadHolder {
    String path;
    UploadEnum status;

    public UploadHolder(String path, UploadEnum status) {
        this.path = path;
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UploadEnum getStatus() {
        return status;
    }

    public void setStatus(UploadEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return TextUtils.equals(path,((UploadHolder)obj).getPath());
    }
}
