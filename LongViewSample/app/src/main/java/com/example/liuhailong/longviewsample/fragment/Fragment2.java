package com.example.liuhailong.longviewsample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;


import com.example.liuhailong.longviewsample.R;
import com.example.liuhailong.longviewsample.widget.DragBubbleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Joe on 2016-06-09
 * Email: zhangjun166@pingan.com.cn
 * modify by liuhailong
 */
public class Fragment2 extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    @Bind(R.id.cb_fill)
    CheckBox mCbFill;
    @Bind(R.id.cb_circle)
    CheckBox mCbCircle;
    @Bind(R.id.pb_cirRadius)
    SeekBar mPbRadio;
    @Bind(R.id.dbv)
    DragBubbleView mBubble;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Fragment2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment2 newInstance(int sectionNumber) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        ButterKnife.bind(this, rootView);
        mCbFill.setChecked(mBubble.getFillDraw());
        mCbCircle.setChecked(mBubble.isShowCircle());
//        mBubble.setEnabled(false);
        mCbFill.setOnCheckedChangeListener(this);
        mPbRadio.setOnSeekBarChangeListener(this);
        mCbCircle.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_fill:
                mBubble.setFillDraw(isChecked);
                break;
            case R.id.cb_circle:
                mBubble.setShowCircle(isChecked);
                break;

        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mBubble.setOriginR(progress + 30);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
