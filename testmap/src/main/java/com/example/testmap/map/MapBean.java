package com.example.testmap.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/28</p>
 * <p>@for : 地图软件原型类</p>
 */
public class MapBean {
    String name;
    String packageName;
    String uri;//吊起地图需要传入的调用语句,使用时用String.format()方法来讲地址传入进去
    String navagationUri;//导航uri
    int icon_id;//缩略图的id

    public MapBean() {
    }

    public MapBean(String name, String packageName, String url, String navagationUri, int icon_id) {
        this.name = name;
        this.packageName = packageName;
        this.uri = url;
        this.navagationUri = navagationUri;
        this.icon_id = icon_id;
    }

    /**
     * @return 是否安装了该地图
     */
    public boolean isInstalled(Context context) {
        PackageInfo packageInfo;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo != null) {
            return true;
        }

        return false;
    }

    public void open(Context context, String keyWord) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format(uri, keyWord)));
        intent.setPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 路线规划到指定目的地
     *
     * @param context
     * @param toAddress
     */
    public void toNavigation(final Context context, final String toAddress) {

        if (name.equals("百度地图")) {//百度地图不需要经纬度,所以不要去定位
           navagation(0,0,toAddress,context);
            return;
        }

        getPositonThenNavigation(context, toAddress,new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult result, int i) {
                List<GeocodeAddress> geocodeAddressList;
                if (result != null) {
                    geocodeAddressList = result.getGeocodeAddressList();
                } else {
                    geocodeAddressList = null;
                }
                if (geocodeAddressList == null || geocodeAddressList.size() == 0) {
                    open(context, toAddress);
                    return;
                }

                //经纬度
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                double latitude = latLonPoint.getLatitude();
                double longitude = latLonPoint.getLongitude();

                //开始路线规划
                navagation(latitude, longitude, toAddress, context);
            }
        });
    }

    /**
     * 获取目标地址编码信息
     * @param toAddress 目标地址
     */
    private void getPositonThenNavigation(final Context context, final String toAddress,GeocodeSearch.OnGeocodeSearchListener listener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(listener);
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(toAddress, null);
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 路线规划到指定目的地
     * @param latitude  目的地经度
     * @param longitude 目的地纬度
     * @param toAddress 目的地地址
     */
    private void navagation(double latitude, double longitude, String toAddress, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format(navagationUri, latitude, longitude,toAddress)));
        intent.setPackage(packageName);
        context.startActivity(intent);
    }

    public String getNavagationUri() {
        return navagationUri;
    }

    public void setNavagationUri(String navagationUri) {
        this.navagationUri = navagationUri;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
