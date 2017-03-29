package com.example.liuhailong.longviewsample.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.liuhailong.longviewsample.R;
import com.example.liuhailong.longviewsample.bean.Cities;
import com.example.liuhailong.longviewsample.bean.Countries;
import com.example.liuhailong.longviewsample.bean.Provinces;
import com.example.liuhailong.longviewsample.widget.adapters.AbstractWheelTextAdapter;
import com.example.liuhailong.longviewsample.widget.views.OnWheelChangedListener;
import com.example.liuhailong.longviewsample.widget.views.OnWheelScrollListener;
import com.example.liuhailong.longviewsample.widget.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhailong on 16/9/19.
 */
public class ChooseCityDialog extends Dialog implements View.OnClickListener {


    private WheelView provinceWheel;
    private WheelView cityWheel;
    private WheelView countryWheel;
    private Context context;

    private ChooseCityAdapter provinceAdapter;
    private ChooseCityAdapter cityAdapter;
    private ChooseCityAdapter countryAdapter;



    public void setOnCityChooseListener(OnCityChooseListener onCityChooseListener) {
        this.onCityChooseListener = onCityChooseListener;
    }

    //确定按钮的事件回调
    private OnCityChooseListener onCityChooseListener;


    //选择的省份
    private String chooseProvince;
    //选择的城市
    private String chooseCity;
    //乡镇
    private String chooseCountry;

    //选择省份之后记录选择的条目
    private int provinceItem;



    private int maxTextSize = 28;
    private int minTextSize = 20;

    ArrayList<Provinces> lists;
    private Window mWindow;
    private TextView btnSure;
    private TextView btnCancel;

    public ChooseCityDialog(Context context,ArrayList<Provinces> lists) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.lists=lists;
    }

    public ChooseCityDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChooseCityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city_dialog);
        provinceWheel= (WheelView) findViewById(R.id.province);
        cityWheel= (WheelView) findViewById(R.id.city);
        countryWheel= (WheelView) findViewById(R.id.country);

        btnSure = (TextView) findViewById(R.id.btn_city_sure);
        btnCancel = (TextView) findViewById(R.id.btn_city_cancel);

        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        setData();


    }

    private void setData() {

        List<String> province= new ArrayList<>();
        for(Provinces provinces:lists){

            province.add(provinces.getName());
        }
        provinceAdapter=new ChooseCityAdapter(context,province,0,maxTextSize,minTextSize);
        //设置可见的条目数
        provinceWheel.setVisibleItems(5);
        provinceWheel.setViewAdapter(provinceAdapter);
        chooseProvince=lists.get(0).getName();
        List<String> city=new ArrayList<>();
        for(Cities citys:lists.get(0).getProvinces()){

            city.add(citys.getName());
        }

        cityAdapter=new ChooseCityAdapter(context,city,0,maxTextSize,minTextSize);
        cityWheel.setVisibleItems(5);
        cityWheel.setViewAdapter(cityAdapter);
        chooseCity=lists.get(0).getProvinces().get(0).getName();
        List<String> country=new ArrayList<>();
        for(Countries countyies:lists.get(0).getProvinces().get(0).getCities()){

            country.add(countyies.getName());
        }
        countryAdapter=new ChooseCityAdapter(context,country,0,maxTextSize,minTextSize);
        countryWheel.setVisibleItems(5);
        countryWheel.setViewAdapter(countryAdapter);
        chooseCountry=lists.get(0).getProvinces().get(0).getCities().get(0).getName();

        //设置省份的滑动监听事件
        provinceWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                int currentItem=wheel.getCurrentItem();
                String currentProvince= (String) provinceAdapter.getItemText(currentItem);
                chooseProvince=currentProvince;
                setTextviewSize(currentProvince,provinceAdapter);
                List<String> city=new ArrayList<>();
                for(Cities citys:lists.get(currentItem).getProvinces()){
                    city.add(citys.getName());
                }
                //滑动之后同时更新城市和乡镇的数据
                cityAdapter=new ChooseCityAdapter(context,city,0,maxTextSize,minTextSize);
                cityWheel.setVisibleItems(5);
                cityWheel.setViewAdapter(cityAdapter);
                cityWheel.setCurrentItem(0);
                chooseCity=lists.get(currentItem).getProvinces().get(0).getName();
                List<String> country=new ArrayList<>();
                for(Countries countries:lists.get(currentItem).getProvinces().get(0).getCities()){
                    country.add(countries.getName());
                }
                countryAdapter=new ChooseCityAdapter(context,country,0,maxTextSize,minTextSize);
                countryWheel.setVisibleItems(5);
                countryWheel.setViewAdapter(countryAdapter);
                countryWheel.setCurrentItem(0);
                chooseCountry=lists.get(currentItem).getProvinces().get(0).getCities().get(0).getName();
            }
        });
        provinceWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                provinceItem=wheel.getCurrentItem();
                String currentProvince= (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentProvince,provinceAdapter);

            }
        });


        cityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                //城市滑动之后也同时更新乡镇的数据
                int currentItem=wheel.getCurrentItem();
                String currentCity= (String) cityAdapter.getItemText(currentItem);
                chooseCity=currentCity;
                setTextviewSize(currentCity,cityAdapter);

                List<String> country=new ArrayList<>();
                for(Countries countries:lists.get(provinceItem).getProvinces().get(currentItem).getCities()){
                    country.add(countries.getName());
                }
                countryAdapter=new ChooseCityAdapter(context,country,0,maxTextSize,minTextSize);
                countryWheel.setViewAdapter(countryAdapter);
                countryWheel.setCurrentItem(0);
                chooseCountry=lists.get(provinceItem).getProvinces().get(currentItem).getCities().get(0).getName();
            }
        });
        cityWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentCity= (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentCity,cityAdapter);
            }
        });

        countryWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                int currentItem=wheel.getCurrentItem();
                String currentCountry= (String) countryAdapter.getItemText(currentItem);
                chooseCountry=currentCountry;
                setTextviewSize(currentCountry,countryAdapter);
            }
        });
        countryWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentCountre=(String) countryAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentCountre,countryAdapter);
            }
        });
    }
    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, ChooseCityAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

   public  interface  OnCityChooseListener{

        void onClick(String province,String city,String country);
    }

    //设置dialog弹出和消失时的动画
    public void  setDialogAnimation(){

        mWindow =getWindow();
        mWindow.setWindowAnimations(R.style.dialogstyle);

        WindowManager.LayoutParams params= mWindow.getAttributes();
        //设置动画的高度.
        //	Point point=new Point();
        //getWindow().getWindowManager().getDefaultDisplay().getSize(point);
        //int y=point.y;
        //params.height=y;
        params.gravity= Gravity.BOTTOM;
        mWindow.setAttributes(params);
    }

    @Override
    public void onClick(View view) {

        if(view==btnSure){
            if(onCityChooseListener!=null){
                onCityChooseListener.onClick(chooseProvince,chooseCity,chooseCountry);
            }
        }else{
            dismiss();
        }
        dismiss();
    }


    //适配器
    public class ChooseCityAdapter extends AbstractWheelTextAdapter{

      private List<String> list;


        public void setList(List<String> list) {
            this.list = list;
        }

        protected ChooseCityAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context,R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list=list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        protected CharSequence getItemText(int index) {

            return list.get(index) + "";
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            View view = super.getItem(index, convertView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list==null?0:list.size();
        }
    }

}
