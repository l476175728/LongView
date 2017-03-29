package com.example.liuhailong.longviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liuhailong.longviewsample.adaper.MyListViewAdapter;
import com.example.liuhailong.longviewsample.bean.ResultBean;
import com.example.liuhailong.longviewsample.view.CustomShowHeaderListView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinnerHeaderActivity extends AppCompatActivity {

    //固定头目的listView
    private CustomShowHeaderListView mListView;

    private RequestQueue mRwquestQue;

    private List<ResultBean.Photo> list;

    private List<List<ResultBean.Photo>> sortList;
    private MyListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinner_header);

        mListView= (CustomShowHeaderListView) findViewById(R.id.pinner_header_listView);

        mRwquestQue= Volley.newRequestQueue(this);
        //发送网络请求获取数据
        getData();

        adapter = new MyListViewAdapter(this);
        mListView.setAdapter(adapter);


    }
    public void getData() {

        String url="http://112.33.7.21:8081/Telemedicine/biz/emefile/getFileList.action";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("返回的字符串",s);
                Gson gson=new Gson();
                ResultBean bean=gson.fromJson(s,ResultBean.class);
                list=bean.getData();

                //对照片按照时间先后进行排序
                Collections.sort(list, new Comparator<ResultBean.Photo>() {
                    @Override
                    public int compare(ResultBean.Photo photo1, ResultBean.Photo photo2) {

                        Long time1=getSureTime(photo1.getCreateTime());
                        Long time2=getSureTime(photo2.getCreateTime());

                        return time2.compareTo(time1);
                    }
                });

                sortList=new ArrayList<>();
                String tempTime=list.get(0).getCreateTime().split(" ")[0];
                List<ResultBean.Photo> newList=new ArrayList<>();
                for(ResultBean.Photo photo:list){
                    //将时间进行切割
                    String[] creatTime=photo.getCreateTime().split(" ");
                    if(creatTime[0].equals(tempTime)){
                        newList.add(photo);
                        newList.add(photo);
                    }else{
                        sortList.add(newList);
                        tempTime=creatTime[0];
                        newList=new ArrayList<>();
                        newList.add(photo);
                        newList.add(photo);
                    }

                }
                adapter.setData(sortList);

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Toast.makeText(PinnerHeaderActivity.this,"数据请求失败",Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("param.token","0BDC1525766F42E6A6E07358FE02BBD7");
                map.put("param.emeId","24E1A77C3EB64B72B3B10EED11654A3E");
                map.put("param.fileType","0");
                return map;

            }
        };
        mRwquestQue.add(stringRequest);
    }



    //获取long类型的时间
    public Long getSureTime(String time){

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date= dateFormat.parse(time);
            Long newTime=date.getTime();
            return newTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
}
