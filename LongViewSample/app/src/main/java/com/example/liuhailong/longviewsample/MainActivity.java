package com.example.liuhailong.longviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.liuhailong.longviewsample.adaper.MainRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView;

    private List<String> name_list=new ArrayList<>();

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
        name_list.add("圆形图片");
        name_list.add("圆形图片");
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
    }
}
