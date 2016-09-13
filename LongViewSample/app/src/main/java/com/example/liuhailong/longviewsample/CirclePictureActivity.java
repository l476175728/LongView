package com.example.liuhailong.longviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.liuhailong.longviewsample.adaper.view.CirclePictureView;

public class CirclePictureActivity extends AppCompatActivity {

    private CirclePictureView circlePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_picture);

        circlePictureView = (CirclePictureView) findViewById(R.id.draw_circle);

    }
}
