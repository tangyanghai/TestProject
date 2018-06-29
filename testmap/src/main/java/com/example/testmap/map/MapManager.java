package com.example.testmap.map;

import android.content.Context;
import android.content.res.Resources;

import com.example.testmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/28</p>
 * <p>@for : 地图管理器</p>
 * <p>提供给外界调用的接口</p>
 */
public class MapManager {
    private static final String DEFALT_ICON_NAME = "ic_map_";

    private static final MapManager ourInstance = new MapManager();

    public static MapManager getInstance() {
        return ourInstance;
    }

    private MapManager() {

    }

    /**
     * 获取已经安装的地图软件
     */
    public List<MapBean> getInstalledMaps(Context context) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        String[] nameArr = resources.getStringArray(R.array.map_name);
        String[] packageArr = resources.getStringArray(R.array.map_package_name);
        String[] datArr = resources.getStringArray(R.array.map_dat);
        String[] navagationUriArr = resources.getStringArray(R.array.map_navigation);
        List<MapBean> maps = new ArrayList<>();
        for (int i = 0; i < nameArr.length; i++) {
            MapBean mapBean = new MapBean();
            mapBean.setPackageName(packageArr[i]);//地图包名
            if (mapBean.isInstalled(context)) {
                mapBean.setName(nameArr[i]);//地图名称
                mapBean.setUri(datArr[i]);//地图吊起时的uri
                mapBean.setNavagationUri(navagationUriArr[i]);//吊起导航的uri
                //获取图片缩略图地址  在项目的mipmap文件夹中寻找,按照地图顺序编号
                int drawableId = resources.getIdentifier(DEFALT_ICON_NAME+i, "mipmap", packageName);
                if (drawableId!=0) {
                    mapBean.setIcon_id(drawableId);
                }
            }
            maps.add(mapBean);
        }
        return maps;
    }
}
