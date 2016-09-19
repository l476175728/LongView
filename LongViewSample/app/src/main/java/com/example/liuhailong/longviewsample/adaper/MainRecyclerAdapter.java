package com.example.liuhailong.longviewsample.adaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liuhailong.longviewsample.R;

import java.util.List;

/**
 * Created by liuhailong on 16/9/12.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.Viewholder> {


    private List<String> name_List;
    private Context mContext;
    private LayoutInflater mInflater;

    public MainRecyclerAdapter(List<String> name_List, Context mContext) {
        this.name_List = name_List;
        this.mContext = mContext;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=mInflater.inflate(R.layout.main_recycler_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        holder.itemView.setOnClickListener(null);
        holder.textView.setText(name_List.get(position));

    }



    @Override
    public int getItemCount() {
        return name_List.size();
    }

    public static  class Viewholder extends RecyclerView.ViewHolder

    {

        public TextView textView;

        public Viewholder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.item_name);

        }
    }
}
