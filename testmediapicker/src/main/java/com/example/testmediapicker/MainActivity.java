package com.example.testmediapicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioDataSource;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioFolder;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioItem;
import com.lzy.imagepicker.mediapicker.loader.video.VideoDataSource;
import com.lzy.imagepicker.mediapicker.loader.video.VideoFolder;
import com.lzy.imagepicker.mediapicker.loader.video.VideoItem;
import com.lzy.imagepicker.mediapicker.ui.MediaBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chooseVideo(View view) {
       MediaBaseActivity.start(this,MediaBaseActivity.TYPE_VIDEO);
    }

    public void chooseAudio(View view) {
        MediaBaseActivity.start(this,MediaBaseActivity.TYPE_AUDIO);
    }
}
