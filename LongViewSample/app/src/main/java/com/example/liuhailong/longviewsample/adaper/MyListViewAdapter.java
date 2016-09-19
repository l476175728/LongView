package com.example.liuhailong.longviewsample.adaper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.TextView;

import com.example.liuhailong.longviewsample.R;
import com.example.liuhailong.longviewsample.bean.ResultBean;
import com.example.liuhailong.longviewsample.view.CustomShowHeaderListView;

import java.util.List;


/**
 * Created by liuhailong on 16/6/29.
 */
public class MyListViewAdapter extends BaseAdapter implements CustomShowHeaderListView.PinnedSectionListAdapter,Filterable {

    private Context mContext;

    private LayoutInflater mInfalter;

    private final int type1=0;
    private final int type2=1;


    public MyListViewAdapter(Context mContext) {

        this.mContext = mContext;
       mInfalter= LayoutInflater.from(mContext);

    }

    private List<List<ResultBean.Photo>> listdateList;

    public void setData(List<List<ResultBean.Photo>> listdateList){

        this.listdateList=listdateList;
    }
    @Override
    public int getCount() {


        return listdateList==null?0:listdateList.size()*2;
    }

    @Override
    public Object getItem(int position) {


        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        ViewHolder2 viewHolder2=null;

        int type=getItemViewType(position);
        if(convertView==null){
            switch (type){

                case type1:
                    convertView=mInfalter.inflate(R.layout.textview_item,null);
                    viewHolder2=new ViewHolder2();
                    viewHolder2.show_time1= (TextView) convertView.findViewById(R.id.show_time1);
                    convertView.setTag(viewHolder2);
                    break;
                case type2:
                    convertView=mInfalter.inflate(R.layout.listview_item,null);
                    viewHolder=new ViewHolder();
                   // viewHolder.show_textView= (TextView) convertView.findViewById(R.id.show_time);
                    viewHolder.show_gridView= (GridView) convertView.findViewById(R.id.show_gridview);
                  //  viewHolder.show_recycleview= (RecyclerView) convertView.findViewById(R.id.show_recycleview);
                    convertView.setTag(viewHolder);
                    break;
            }

        }else{

            switch (type){

                case type1:
                    viewHolder2= (ViewHolder2) convertView.getTag();
                    break;
                case type2:
                    viewHolder= (ViewHolder) convertView.getTag();
                    break;
            }

        }

        switch (type){


            case type1:
                viewHolder2.show_time1.setText(listdateList.get(position/2).get(0).getCreateTime().split(" ")[0]);
//                viewHolder2.show_time1.setText("hahahhahaahahh");
                break;
            case type2:
                //viewHolder.show_textView.setText(listdateList.get(position).get(0).getCreateTime().split(" ")[0]);

                MyGridViewAdapter adapter=new MyGridViewAdapter(mContext,listdateList.get((position-1)/2));
                viewHolder.show_gridView.setAdapter(adapter);
//                viewHolder.show_recycleview.setLayoutManager(new GridLayoutManager(mContext,3));
//                CustonRecycleViewAdapter adapter=new CustonRecycleViewAdapter(mContext,listdateList.get((position-1)/2));
//                viewHolder.show_recycleview.setAdapter(adapter);
                break;
        }


        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position%2==0){

            return false;
        }else{
            return  true;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position%2==0){
            return type1;
        }else{
            return type2;
        }

    }


    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {


        Log.d("返回的结果",(viewType==type1)+"....."+viewType+"....");
        return viewType==type1;
    }

    @Override
    public Filter getFilter() {


        return null;
    }

    public class ViewHolder{

        public GridView show_gridView;
    }

    public class ViewHolder2{

        public TextView show_time1;

    }
}
