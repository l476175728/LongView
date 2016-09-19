package com.example.liuhailong.longviewsample;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.liuhailong.longviewsample.adaper.MainRecyclerAdapter;
import com.example.liuhailong.longviewsample.bean.Cities;
import com.example.liuhailong.longviewsample.bean.Provinces;
import com.example.liuhailong.longviewsample.customdialog.ChangeBirthDialog;
import com.example.liuhailong.longviewsample.interfaces.OnRecyclerItemClickListener;
import com.example.liuhailong.longviewsample.myutils.PullParse;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView;

    private List<String> name_list=new ArrayList<>();

    private PullParse pullParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView= (RecyclerView) findViewById(R.id.app_recyclerview);

        setData();
    }

    private void setData() {

        name_list.add("圆形图片");
        name_list.add("刮刮卡");
        name_list.add("日期选择控件");
        name_list.add("城市三级联动");
        name_list.add("固定头目的listView");
        name_list.add("支付宝咻一咻");
        name_list.add("购物车动画");
        name_list.add("侧滑删除的ListViw");
        name_list.add("QQ上可以拖动的小圆点");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        name_list.add("圆形图片");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        MainRecyclerAdapter mainRecyclerAdapter=new MainRecyclerAdapter(name_list,this);
        mRecyclerView.setAdapter(mainRecyclerAdapter);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

                int position=vh.getAdapterPosition();

                switch (position){
                    case 0:
                    startActivity(new Intent(MainActivity.this, CirclePictureActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, GuaGuaKaActivity.class));
                        break;
                    case 2:
                        showTimeChooseDialog();
                        break;
                    case 3:
                        pullParse=new PullParse();
                        setCityData();
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, PinnerHeaderActivity.class));
                        break;
                }

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
    }

    private void setCityData() {
        ArrayList<Provinces> province;
        ArrayList<Cities> citylist;
        ArrayList<String> pro;

        try {
            XmlResourceParser xrp=getResources().getXml(R.xml.citys_weather);
            province=pullParse.getCountey(xrp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showTimeChooseDialog() {

        ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(
                MainActivity.this);
        //获取当前的时间24小时制
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        String currentTime=sdf.format(new Date());

        String[] str=currentTime.split("-");
        mChangeBirthDialog.setDate(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]),Integer.parseInt(str[3])
                ,Integer.parseInt(str[4]),Integer.parseInt(str[5]));
        mChangeBirthDialog.show();
        //设置弹出时的动画
        mChangeBirthDialog.setCanceledOnTouchOutside(true);
        mChangeBirthDialog.setDialogAnimation();
        mChangeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day,String hour,String minute,String second) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this,
                        year + "-" + month + "-" + day+" "+hour+":"+minute+":"+second,
                        Toast.LENGTH_LONG).show();
            }
        });


    }


}
