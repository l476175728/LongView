package com.example.liuhailong.longviewsample.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.liuhailong.longviewsample.R;
import com.example.liuhailong.longviewsample.animatorpackager.StrokeGradientDrawable;

import butterknife.ButterKnife;

/**
 * Created by Joe on 2016-06-09
 * Email: zhangjun166@pingan.com.cn
 * modify by liuhailong
 */
public class Fragment6 extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private StrokeGradientDrawable drawable;
    private GradientDrawable gradientDrawable;
    private float density;
    private boolean flag;

    public Fragment6() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment6 newInstance(int sectionNumber) {
        Fragment6 fragment = new Fragment6();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab6, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
