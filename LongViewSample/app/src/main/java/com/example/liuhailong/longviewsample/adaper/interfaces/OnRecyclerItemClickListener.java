package com.example.liuhailong.longviewsample.adaper.interfaces;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liuhailong on 16/8/17.
 */

public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
          //  recyclerView.getAdapter().notifyDataSetChanged();
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);

                onItemClick(vh);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);

                onItemLongClick(vh);
            }
        }

    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);
    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
}
