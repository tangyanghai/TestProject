package com.lzy.imagepicker.mediapicker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.R;
import com.lzy.imagepicker.mediapicker.loader.AbsDataSource;
import com.lzy.imagepicker.mediapicker.loader.AbsMediaFolder;
import com.lzy.imagepicker.mediapicker.loader.AbsMediaItem;
import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;
import com.lzy.imagepicker.ui.ImageBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/03</p>
 * <p>@for : 文件选择基础activity</p>
 * <p>注意,在进入该页面之前,一定要先确认已经获取权限,不给权限就不让进来了,没意义</p>
 */
public abstract class MediaBaseActivity<T extends AbsMediaItem, P extends AbsMediaFolder<T>, N extends AbsDataSource<T, P>> extends ImageBaseActivity implements View.OnClickListener {

    protected static final String KEY_SELECTED_ITME = "selectedItem";
    //要查找的文件夹的路径
    protected static final String KEY_SELECTED_FOLDER_PAHT = "folderPath";


    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_VIDEO = 2;

    /**
     * @param fileType {@link #TYPE_AUDIO,#TYPE_VIDEO}
     */
    public static void start(Context context, int fileType) {
        Intent intent = new Intent();
        Class cls;
        switch (fileType) {
            case 1://音频
                cls = AudioActivity.class;
                break;
            case 2://视频
                cls = VideoActivity.class;
                break;
            default:
                return;
        }
        if (cls != null) {
            intent.setClass(context, cls);
            context.startActivity(intent);
        }
    }

    TextView mTvTitle;//文件类型名称
    ImageView mImgBack;//返回按钮
    Button mBtnOk;//完成按钮
    Button mBtnDir;//文件夹选择按钮
    Button mBtnPreview;//预览按钮-->在图片类型的时候要显示,其他类型的时候要隐藏
    RecyclerView mRvMedias;//文件列表


    List<String> mSelectedPathsComeIn;//外界传入的,已经选中的列表
    volatile List<T> mSelectedItems;//已经选中的文件列表
    List<P> mAllFolders;//所有文件夹列表
    P mCurrentFolders;//当前显示的文件夹
    N mDataSource;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initView();
        initData();
    }

    private void initData() {
        //获取外界传入的已经选择的数据
        mSelectedPathsComeIn = (List<String>) getIntent().getSerializableExtra(KEY_SELECTED_ITME);
        mSelectedItems = new ArrayList<>();
        String path = getIntent().getStringExtra(KEY_SELECTED_FOLDER_PAHT);
        mDataSource = initDataSource(path, new OnMediaLoadedListener<P>() {
            @Override
            public void onMediaLoaded(List<P> folders) {
                if (folders != null && folders.size() > 0) {
                    //得到了所有文件
                    checkFolderHasSelectedItem(folders);
                    //将数据设置到界面上去
                    mAllFolders = folders;
                    mCurrentFolders = folders.get(0);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 初始化数据源
     *
     * @param onMediaLoadedListener
     */
    protected abstract N initDataSource(String path, OnMediaLoadedListener<P> onMediaLoadedListener);

    /**
     * 将外界传入的列表,加入到已经选中的文件列表中
     */
    private void checkFolderHasSelectedItem(List<P> list) {
        if (mSelectedPathsComeIn == null || list == null) {
            return;
        }

        for (P p : list) {
            ArrayList<T> medias = p.getMedias();
            if (medias != null) {
                for (T media : medias) {
                    for (String s : mSelectedPathsComeIn) {
                        if (TextUtils.equals(s, media.path)) {
                            mSelectedItems.add(media);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_des);
        mImgBack = findViewById(R.id.btn_back);
        mBtnOk = findViewById(R.id.btn_ok);
        mBtnDir = findViewById(R.id.btn_dir);
        mBtnPreview = findViewById(R.id.btn_preview);
        mRvMedias = findViewById(R.id.rv_medias);
        mImgBack.setOnClickListener(this);
        mRvMedias.setLayoutManager(new LinearLayoutManager(this));
//        mRvMedias.setLayoutManager(new GridLayoutManager(this, getImageCols()));
        mRvMedias.addItemDecoration(new MyItemDecoration());
        mAdapter = new Adapter();
        mRvMedias.setAdapter(mAdapter);
    }

    /**
     * 获取图片列数,最小三列
     */
    public int getImageCols() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int densityDpi = getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        return cols = cols < 3 ? 3 : cols;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {//返回键
            finish();
        }
    }


    class Adapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_list_item_vertical, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            try {
                T t = mCurrentFolders.getMedias().get(position);
                setItemIcon(t, holder.mImg);
                holder.mTitle.setText(t.name);
                holder.mAddTime.setText(t.addTime + "");
                holder.mSize.setText(t.size + "");
            } catch (Exception e) {
                //
            }
        }

        @Override
        public int getItemCount() {
            return mCurrentFolders == null ? 0 : mCurrentFolders.getMedias().size();
        }
    }

    protected abstract void setItemIcon(T t, ImageView mImg);


    class Holder extends RecyclerView.ViewHolder {

        ImageView mImg;
        CheckBox mCB;
        TextView mTitle;
        TextView mAddTime;
        TextView mSize;


        public Holder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.iv_thumb);
            mCB = itemView.findViewById(R.id.cb_check);
            mTitle = itemView.findViewById(R.id.tv_name);
            mAddTime = itemView.findViewById(R.id.tv_add_time);
            mSize = itemView.findViewById(R.id.tv_size);
        }
    }


    class MyItemDecoration extends RecyclerView.ItemDecoration {
        /**
         * @param outRect 边界
         * @param view    recyclerView ItemView
         * @param parent  recyclerView
         * @param state   recycler 内部数据管理
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = state.getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            //设定底部边距为1px
            outRect.set(0, 0, 0, pos == itemCount - 1 ? 0 :20);
        }

    }

}
