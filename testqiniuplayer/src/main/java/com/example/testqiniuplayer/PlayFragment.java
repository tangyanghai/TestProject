package com.example.testqiniuplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.pili.pldroid.player.PLMediaPlayer.ERROR_CODE_IO_ERROR;
import static com.pili.pldroid.player.PLMediaPlayer.ERROR_CODE_OPEN_FAILED;
import static com.pili.pldroid.player.PLMediaPlayer.ERROR_CODE_SEEK_FAILED;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/23</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class PlayFragment extends BaseFramgent implements PLMediaPlayer.OnBufferingUpdateListener,
        View.OnSystemUiVisibilityChangeListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnErrorListener,
        PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener,
        PLMediaPlayer.OnSeekCompleteListener {
    public static final String TAG = PlayFragment.class.getName();

    public static final String KEY_VIDEO_URL = "url";

    /**
     * 付款成功
     */
    public static String KEY_PAY_SUCCESS = "pay_success";

    /**
     * 隐藏控制view
     */
    public static final int CODE_HANDLE_HIDE_CONTROLLER = 0;
    /**
     * 显示控制view
     */
    public static final int CODE_HANDLE_SHOW_CONTROLLER = 1;
    /**
     * 更新时间
     */
    public static final int CODE_HANDLE_UPDATE_TIME = 2;

    //    String url = "http://airclass.oss-cn-shenzhen.aliyuncs.com/upload/hotvideo/20171216/15134204412940.mp4";
    String url = null;
    @BindView(R.id.PLVideoView)
    PLVideoTextureView mVideoView;
    @BindView(R.id.img_video_start)
    CheckBox mStart;//开始/暂停
    @BindView(R.id.tv_current_duration)
    TextView mCurrentDuration;//当前时间
    @BindView(R.id.tv_total_duration)
    TextView mTotalDuration;//总时间
    @BindView(R.id.video_seek_bar)
    SeekBar mSeekBar;//进度条
    @BindView(R.id.img_video_fullscreen)
    CheckBox mFullscreen;//全屏/还原
    @BindView(R.id.fragment_play_control)
    RelativeLayout mLayoutOnPlayer;//控制部分所在布局
    @BindView(R.id.control_hider)
    View mControlHider;//遮挡底部控制布局的view
    @BindView(R.id.control)
    LinearLayout control;

    private long duration;
    private AVOptions avOptions;

    /**
     * 进度是否是被用户改变的
     */
    private boolean changedByUser = false;
    /**
     * 传递到activity的接口
     */
    private VideoListener mVideoLister;
    /**
     * 播放位置
     */
    private long playDuration;

    /**
     * 用于更新进度的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_HANDLE_HIDE_CONTROLLER://隐藏控制view
                    if (mLayoutOnPlayer.isShown()) {
                        mLayoutOnPlayer.setVisibility(View.GONE);
                    }
                    break;
                case CODE_HANDLE_SHOW_CONTROLLER://显示控制view  在准备完成
                    removeMessages(CODE_HANDLE_HIDE_CONTROLLER);
                    if (!mLayoutOnPlayer.isShown()) {
                        mLayoutOnPlayer.setVisibility(View.VISIBLE);
                    }
                    sendEmptyMessageDelayed(CODE_HANDLE_HIDE_CONTROLLER, 3000);//显示后,调用隐藏,3秒钟内没有操作就自动再次自动隐藏
                    break;
                case CODE_HANDLE_UPDATE_TIME://更新当前播放时间
                    removeMessages(CODE_HANDLE_UPDATE_TIME);
                    if (mSeekBar != null && !changedByUser) {
                        mSeekBar.setProgress((int) playDuration);
                        mCurrentDuration.setText(DateUtils.formatMS(playDuration));
                        LogUtils.e(TAG + "更新播放时间   ", "更新");
                    }
                    sendEmptyMessageDelayed(CODE_HANDLE_UPDATE_TIME, 1000);
                    break;
            }
        }
    };
    /**
     * 播放是否完成标记 在prepared之后设置为false,完成后设置为true
     */
    private boolean isCompeltion = false;

    private boolean isPortrait = true;//横竖屏切换
    private WindowManager windowManager;
    private int screenWidth;
    private int screenHeight;
    private int widthMultiple;//获取视频尺寸的宽高比
    private int heightMultiple;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_qiniu_player;
    }

    public static Bundle getBundel(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_VIDEO_URL, url);
        return bundle;
    }

    @Override
    public void initData(View view) {
        Bundle arguments = getArguments();
        url = arguments.getString(KEY_VIDEO_URL);

        afterPay();
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
    }

    @Override
    public void initEvent(View view) {
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showController();
                return false;
            }
        });
        mFullscreen.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back://返回键
                if (isPortrait) {
                    mActivity.finish();
                } else {
                    isPortrait = true;
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    //当切换横竖屏时回调
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isPortrait) {
            ((QiniuPlayActivity) mActivity).setVideoPlaySize(true, widthMultiple, heightMultiple);
        } else {
            ((QiniuPlayActivity) mActivity).setVideoPlaySize(false, screenHeight, screenWidth);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.img_video_start://开始/暂停
                if (isChecked) {
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        mVideoView.pause();
                    }
                } else {
                    if (mVideoView != null && !mVideoView.isPlaying()) {
                        if (errorNoNet) {
                            errorNoNet = false;
                            mVideoView.setVideoPath(url);
                        }
                        mVideoView.start();
                    }
                }
                showController();
                break;
            case R.id.img_video_fullscreen://全屏/半屏
                if (!isChecked && mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    //切至竖屏
                    isPortrait = true;
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //切至横屏
                    isPortrait = false;
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                showController();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        mVideoView.stopPlayback();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }


    /**
     * 显示控制view(先调用显示,3秒后自动隐藏)
     * 1.准备完成后使用
     * 2.用户操作后使用:暂停/开始,滑动进度条,切换屏幕
     * 3.视频view被点击
     */
    private void showController() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(CODE_HANDLE_SHOW_CONTROLLER);
        }
    }

    /**
     * 更新当前播放进度
     */
    private void updateCurrentTime() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(CODE_HANDLE_UPDATE_TIME, 1000);
        }
    }

    /**
     * 付款之后,直接设置视屏播放
     */
    private void afterPay() {
        url =  "https://vs.ntd.tv/2018/0705/0b33032d-f939-4b76-4aa4-b638c9e2aec5/video_instant.mp4?_=1";
        mControlHider.setVisibility(View.GONE);
        mVideoView.setVideoPath(url);
        View loadingView = View.inflate(getActivity(), R.layout.dialog_loading, null);
        mVideoView.setBufferingIndicator(loadingView);
        mVideoView.setScreenOnWhilePlaying(true);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnSystemUiVisibilityChangeListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnSeekCompleteListener(this);
        //mVideoView.se tLooping(true);//循环播放
        mVideoView.setOnVideoSizeChangedListener(this);
        avOptions = new AVOptions();
        //设置缓存
        avOptions.setString(AVOptions.KEY_CACHE_DIR, getActivity().getCacheDir().getAbsolutePath() + "vcache");
        mVideoView.setAVOptions(avOptions);
        mStart.setOnCheckedChangeListener(this);
        mVideoView.start();
    }

    //------start -- 视屏相关回调--------------------

    /**
     * 缓冲进度回调
     *
     * @param i 进度
     */
    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {
        LogUtils.e(TAG, "onBufferingUpdate   " + i);
        if (mVideoView != null && !mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    public void onSystemUiVisibilityChange(int i) {
        LogUtils.e(TAG, "onSystemUiVisibilityChange  " + i);
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        LogUtils.e(TAG, "onCompletion");
        mHandler.removeCallbacksAndMessages(null);
        mCurrentDuration.setText(DateUtils.formatMS(mSeekBar.getMax()));
        mStart.setChecked(true);
        mVideoView.stopPlayback();
        isCompeltion = true;
    }

    private boolean errorNoNet = false;

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        LogUtils.e(TAG, "onError  " + i);
        switch (i) {
            case ERROR_CODE_SEEK_FAILED://拖动失败
                LogUtils.e(TAG, "onError  拖动失败");
                break;
            case ERROR_CODE_OPEN_FAILED:
                ToastUtils.showToast("打开播放器失败");
                break;
            case ERROR_CODE_IO_ERROR:
                ToastUtils.showToast("网络错误");
                errorNoNet = true;
                break;
            default:
                mStart.setChecked(true);
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
        if (isCompeltion) {
            //播放完成,重新开始的情况
            isCompeltion = false;
            playDuration = mSeekBar.getProgress();
            mVideoView.seekTo(playDuration);
            mStart.setChecked(false);
            LogUtils.e(TAG, "重新播放  播放位置: " + playDuration);
//            updateCurrentTime();
        } else {
            duration = plMediaPlayer.getDuration();
            if (playDuration < duration) {
                mVideoView.seekTo(playDuration);
                LogUtils.e(TAG, "播放位置: " + playDuration);
            }
            initSeekBar(duration);
            if (mStart.isChecked()) {
                mVideoView.pause();
            }

        }
        showController();
        LogUtils.e(TAG, "准备结束  总时长" + duration);
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        LogUtils.e(TAG, "onInfo  i=" + i + "  i1=" + i1);
        switch (i) {
            case 10005:
                LogUtils.e(TAG, "onInfo  更新进度=" + i1);
                playDuration = i1;
                break;
        }
        return false;
    }

    /**
     * 用于监听当前播放的视频流的尺寸信息，
     * 在 SDK 解析出视频的尺寸信息后，会触发该回调，开发者可以在该回调中调整 UI 的视图尺寸
     *
     * @param plMediaPlayer 播放器
     * @param i             宽
     * @param i1            高
     */
    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {
        widthMultiple = i;
        heightMultiple = i1;
        LogUtils.e(TAG, "onVideoSizeChanged  宽=" + i + "  高=" + i1);
        if (mVideoLister != null) {
            mVideoLister.onVideoSizeChanged(i, i1);
        }
    }

    /**
     * 拖动进度完成
     */
    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        updateCurrentTime();
    }
    //------end -- 视屏相关回调--------------------

    //----------start -- 进度条-------------

    private void initSeekBar(long duration) {
        //已经初始化过的,不再进行初始化
        if (mSeekBar.getMax() == duration) {
            return;
        }
        LogUtils.e(TAG, "初始化进度条");
        mSeekBar.setMax((int) duration);
        mTotalDuration.setText(DateUtils.formatMS(duration));
        mSeekBar.setOnSeekBarChangeListener(this);
        updateCurrentTime();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        changedByUser = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (changedByUser) {
            seekBar.setTag((long) i);
            mCurrentDuration.setText(DateUtils.formatMS(i));
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (changedByUser) {
            if (isCompeltion) {
                mVideoView.setVideoPath(url);
                mVideoView.start();
            } else {
                mHandler.removeMessages(CODE_HANDLE_UPDATE_TIME);
                if (mStart.isChecked()) {
                    mStart.setChecked(false);
                }
                mVideoView.seekTo((Long) seekBar.getTag());
            }
            changedByUser = false;
        }
        showController();
    }
    //----------end -- 进度条-------------

    /**
     * 设置与activity通信回调
     */
    public void setVideoLister(VideoListener mVideoLister) {
        this.mVideoLister = mVideoLister;
    }

    /**
     * 传递到activity的接口
     */
    public interface VideoListener {
        /**
         * 视频资源尺寸
         *
         * @param width  宽
         * @param height 高
         */
        void onVideoSizeChanged(int width, int height);
    }
}