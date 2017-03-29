package com.example.liuhailong.longviewsample;

import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuhailong.longviewsample.adaper.SortAdapter;
import com.example.liuhailong.longviewsample.bean.Provinces;
import com.example.liuhailong.longviewsample.myutils.PullParse;
import com.example.liuhailong.longviewsample.view.CharacterParser;
import com.example.liuhailong.longviewsample.view.CitySortModel;
import com.example.liuhailong.longviewsample.view.PinyinComparator;
import com.example.liuhailong.longviewsample.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SlidingSearchActivity extends AppCompatActivity {

    //这个listView可以继承固定头目的listView 让其滑动的时候头目可以固定,后期会加上这个功能
    //TODO
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_search);
        initViews();
    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(getApplication(), ((CitySortModel) adapter.getItem(position - 1)).getName(),
//                        Toast.LENGTH_SHORT).show();
                Toast.makeText(SlidingSearchActivity.this,position+"...",Toast.LENGTH_LONG).show();
            }
        });

        ArrayList<Provinces> province=new ArrayList<>();
        XmlResourceParser xrp=getResources().getXml(R.xml.citys_weather);
        PullParse pullParse=new PullParse();
        try {
            province=pullParse.getCountey(xrp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] city=new String[province.size()];

        for(int x=0;x<province.size();x++){
            city[x]=province.get(x).getName();
        }
        SourceDateList = filledData(city);
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }

    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.item_select_city, null);
        TextView tv_catagory = (TextView) headView.findViewById(R.id.tv_catagory);
        TextView tv_city_name = (TextView) headView.findViewById(R.id.tv_city_name);
        tv_catagory.setText("自动定位");
        tv_city_name.setText("北京");
        tv_city_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_city_location), null, null, null);
        tv_city_name.setCompoundDrawablePadding(24);
        return headView;
    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;

    }

}
