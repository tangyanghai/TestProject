package com.example.comprocessvedio;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

/**
 * @author : Administrator
 * @time : 13:31
 * @for : 小米推送时,声音播放
 */
public class AudioManager {
    public static final int TYPE_NORMAL = 0;//正常声音
    public static final int TYPE_STANDARD = 1;//一口价声音

    private static AudioManager instance = null;
    //播放器  每次播放完成之后自动回收
    private MediaPlayer mp;
    //handler  将播放转换到主线程中去,不然不会播放出声音
    private Handler handler;
    private static final String TAG = ">>========<<";

    private AudioManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    private static AudioManager getInstance() {
        if (instance == null) {
            synchronized (AudioManager.class) {
                if (instance == null) {
                    instance = new AudioManager();
                }
            }
        }
        return instance;
    }

    //    public static void play(int type) {
//        getInstance();
//        instance.mediaplayer(type);
//    }
    public static void play(String path) {
        getInstance();
        instance.mediaplayer(path);
    }

    //    public void mediaplayer(final int rawId) {
    private void mediaplayer(final String path) {
        //播放短音乐
        try {
            release();
            handler.removeCallbacksAndMessages(null);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mp = new MediaPlayer();
//                    AssetFileDescriptor file = App.appContext.getResources().openRawResourceFd(rawId);
                    try {
//                        mp.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
//                                file.getLength());
                        mp.setDataSource(path);

                        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer player, int what, int extra) {
                                release();
                                return false;
                            }
                        });
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer player) {
                                release();
                            }
                        });
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer player) {
                                try {
                                    mp.start();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        mp.setVolume(1.0f, 1.0f);
                        mp.prepareAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //                        file.close();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        if (mp != null) {
            synchronized (AudioManager.class) {
                try {
                    if (mp != null) {
                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        mp.release();
                        mp = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
