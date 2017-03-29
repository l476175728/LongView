package com.example.liuhailong.longviewsample;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.List;

public class GaoDeMapActivity extends AppCompatActivity implements LocationSource,AMapLocationListener,View.OnClickListener,PoiSearch.OnPoiSearchListener {

    private MapView mapView;

    private AMap aMap;

    private Button nearBy;
    private Button poiRoute;
    private Button poiSearchbt;

    //当前城市的code
    private String  cityCode;


    //定位改变的监听
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    //当前的经度
    private double latitude;
    //当前的纬度
    private double longitude;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiResult poiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_gao_de_map);
        mapView= (MapView) findViewById(R.id.gaode_map);
        mapView.onCreate(savedInstanceState);
        initView();
        initMap();
    }
    private void initView() {
        if(null==aMap){
            //设置定位的一些属性
            aMap= mapView.getMap();

        }
        nearBy= (Button) findViewById(R.id.map_nearby);
        nearBy.setOnClickListener(this);
        poiSearchbt= (Button) findViewById(R.id.map_search);
        poiSearchbt.setOnClickListener(this);
        poiRoute= (Button) findViewById(R.id.map_poiline);
        poiRoute.setOnClickListener(this);
    }

    private void initMap() {

        setLocationAttribute();
        mlocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);

        //每一秒重新定位与一次,系统默认的为没两秒重新定位
        mLocationOption.setInterval(1000);

        mLocationOption.setNeedAddress(true);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }


    private void setLocationAttribute() {

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置显示指南针
        aMap.getUiSettings().setCompassEnabled(true);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gaode_3dmap:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.gaode_2dmap:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
//                aMapLocation.setVisibility(View.GONE);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //获取精确定位的信息
               int locationType=aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                //获取纬度
                latitude = aMapLocation.getLatitude();
                //获取经度
                longitude = aMapLocation.getLongitude();
               double accuracy= aMapLocation.getAccuracy();//获取精度信息
               String address= aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String country= aMapLocation.getCountry();//国家信息
               String province= aMapLocation.getProvince();//省信息
               String city= aMapLocation.getCity();//城市信息
               String district= aMapLocation.getDistrict();//城区信息
               String street= aMapLocation.getStreet();//街道信息
               String streetNum= aMapLocation.getStreetNum();//街道门牌号信息
              cityCode= aMapLocation.getCityCode();//城市编码
               String adCode= aMapLocation.getAdCode();//地区编码
               String aoiName= aMapLocation.getAoiName();//获取当前定位点的AOI信息
                //获取定位时间
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                String locationTime=df.format(date);
                Log.e("AmapErr",locationType+"..."+ latitude +"..."+ longitude +"..."+accuracy+"..."+address+"..."+country+"...."+province+"..."+city+"..."+district+"..."+street
                +"...."+streetNum+"..."+cityCode+"..."+adCode+"..."+aoiName+"...");

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
//                mLocationErrText.setVisibility(View.VISIBLE);
//                mLocationErrText.setText(errText);
            }
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

        mListener = onLocationChangedListener;

    }

    /*
    *
    * 停止定位
    * */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.map_nearby:
                final String[] chooseItems={"美食","酒店","景点","银行","公交地铁","电影院","网吧","学校"};
                new AlertDialog.Builder(GaoDeMapActivity.this).setTitle("选择搜索类型").setSingleChoiceItems(chooseItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String chooseItem=chooseItems[i];
                        Toast.makeText(GaoDeMapActivity.this,chooseItem,Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        //搜索周边
                        query = new PoiSearch.Query(chooseItem,"",cityCode);
                        poiSearch = new PoiSearch(GaoDeMapActivity.this, query);
                        //设置搜索的范围
                        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,longitude),5000));
                        poiSearch.setOnPoiSearchListener(GaoDeMapActivity.this);
                        poiSearch.searchPOIAsyn();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                break;
            case R.id.map_poiline:
                break;
            case R.id.map_search:
                break;
        }
    }


    //poi搜索回调
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                       // showSuggestCity(suggestionCities);
                    } else {

                    }
                }
            } else {

            }
        } else {

        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
