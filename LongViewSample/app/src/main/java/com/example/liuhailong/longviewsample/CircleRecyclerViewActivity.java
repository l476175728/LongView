package com.example.liuhailong.longviewsample;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.liuhailong.longviewsample.bean.ModeType;
import com.example.liuhailong.longviewsample.fragment.MultiModeFragment;

public class CircleRecyclerViewActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_recycler_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("TYPE_CIRCLE");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MultiModeFragment fragment = MultiModeFragment.newInstance(ModeType.TYPE_CIRCLE);
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                int modeType = ModeType.TYPE_CIRCLE;
                switch (item.getItemId()) {
                    case R.id.mode_1:
                        modeType = ModeType.TYPE_CIRCLE;
                        mToolbar.setTitle("TYPE_CIRCLE");
                        break;
                    case R.id.mode_2:
                        modeType = ModeType.TYPE_SCALEX;
                        mToolbar.setTitle("TYPE_SCALEX");
                        break;
                    case R.id.mode_3:
                        modeType = ModeType.TYPE_SCALEY;
                        mToolbar.setTitle("TYPE_SCALEY");
                        break;
                    case R.id.mode_4:
                        modeType = ModeType.TYPE_ROTATEXSCALEY;
                        mToolbar.setTitle("TYPE_ROTATEXSCALEY");
                        break;
                    case R.id.mode_5:
                        modeType = ModeType.TYPE_ROTETEYSCALEX;
                        mToolbar.setTitle("TYPE_ROTETEYSCALEX");
                        break;
                    case R.id.mode_6:
                        modeType = ModeType.TYPE_CIRCLE_NO_LOOP;
                        mToolbar.setTitle("NOLOOPBUTCENTER");
                    default:
                        break;
                }

                MultiModeFragment fragment = MultiModeFragment.newInstance(modeType);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
