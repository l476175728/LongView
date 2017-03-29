package com.example.liuhailong.longviewsample;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.liuhailong.longviewsample.adaper.MainRecyclerAdapter;
import com.example.liuhailong.longviewsample.bean.Provinces;
import com.example.liuhailong.longviewsample.customdialog.ChangeBirthDialog;
import com.example.liuhailong.longviewsample.customdialog.ChooseCityDialog;
import com.example.liuhailong.longviewsample.interfaces.OnRecyclerItemClickListener;
import com.example.liuhailong.longviewsample.myutils.PullParse;


import org.kymjs.chat.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        name_list.add("一系列动画效果");
        name_list.add("购物车动画");
        name_list.add("侧滑删除的RecyclerView");
        name_list.add("右侧导航的listview");
        name_list.add("相册");
        name_list.add("弹幕效果");
        name_list.add("高德地图");
        name_list.add("圆形图片的RecyclerView");
        name_list.add("简单的聊天界面");
        name_list.add("OpenGL 3D效果");
        name_list.add("二维码生成和识别");
        name_list.add("生成验证码");
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
                    case 5:
                        startActivity(new Intent(MainActivity.this, ManyAnimationActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, ShopCartActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, RecyclerSlidingActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, SlidingSearchActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this, GalleryRecyActivity.class));
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this, DanMuActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(MainActivity.this, GaoDeMapActivity.class));
                        break;
                    case 12:
                        startActivity(new Intent(MainActivity.this, CircleRecyclerViewActivity.class));
                        break;
                    case 13:
                        startActivity(new Intent(MainActivity.this, ChatActivity.class));
                        break;




                }

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
    }

    private void setCityData() {

            ArrayList<Provinces> province=new ArrayList<>();
            XmlResourceParser xrp=getResources().getXml(R.xml.citys_weather);
        try {
            province=pullParse.getCountey(xrp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChooseCityDialog chooseCityDialog=new ChooseCityDialog(this,province);
            chooseCityDialog.show();
            //设置弹出时的动画
            chooseCityDialog.setCanceledOnTouchOutside(true);
            chooseCityDialog.setDialogAnimation();
            chooseCityDialog.setOnCityChooseListener(new ChooseCityDialog.OnCityChooseListener() {
                @Override
                public void onClick(String province, String city, String country) {
                    Toast.makeText(MainActivity.this,province+"..."+city+"...."+country,Toast.LENGTH_SHORT).show();
                }
            });



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
