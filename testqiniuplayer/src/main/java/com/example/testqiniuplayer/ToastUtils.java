package com.example.testqiniuplayer;

import android.widget.Toast;



/**
 * <b>项目名：</b><span color=#FF5205>Carsland </span><br>
 * <b>包名：</b><span color=#FF5205>com.carsland.cld.utils </span><br>
 * <b>文件名：</b><span color=#FF5205>ToastUtils </span><br>
 * <b>创建者：</b><span color=#FF5205>wxr </span><br>
 * <b>创建时间：</b><span color=#FF5205>2017/10/13 10:10 </span><br>
 * <b>描述：土司</b>
 */
public class ToastUtils {

    private static String oldMsg;
    protected static Toast toast   = null;
    private static   long  oneTime = 0;
    private static   long  twoTime = 0;

    /**
     * 弹出土司,只有一个土司,多次调用只会更改文字和时间延长
     * @param msg 弹出的文字
     */
    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(BaseApp.getContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 弹出土司,只有一个土司,多次调用只会更改文字和时间延长
     * @param resId 弹出的文字id
     */
    public static void showToast(int resId) {showToast(BaseApp.getContext().getString(resId));}
}
